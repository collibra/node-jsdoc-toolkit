/**
 * 
 */
package com.collibra.dgc.core.service.job.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.model.job.Job;
import com.collibra.dgc.core.model.job.Job.EState;
import com.collibra.dgc.core.model.job.impl.JobImpl;
import com.collibra.dgc.core.service.impl.AbstractService;
import com.collibra.dgc.core.service.job.IJobProgressHandler;
import com.collibra.dgc.core.service.job.IJobRunner;
import com.collibra.dgc.core.service.job.JobService;

/**
 * Implementation of the {@link JobService}.
 * @author dieterwachters
 */
@Service
public class JobServiceImpl extends AbstractService implements JobService, InitializingBean {
	private final Map<String, JobStruct> jobs = new HashMap<String, JobStruct>();
	private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

	private ExecutorService executor;

	@Override
	public Job runJob(IJobRunner job) {
		return runJob(job, null);
	}

	@Override
	public Job runJob(final IJobRunner job, final IJobProgressHandler progressHandler) {
		final Job newJob = new JobImpl(job.isCancelable(), getCurrentUser());
		final ProgressHandler ph = new ProgressHandler(newJob, progressHandler);

		jobs.put(newJob.getId(), new JobStruct(newJob, ph));

		executor.submit(new Runnable() {
			@Override
			public void run() {
				newJob.setState(EState.RUNNING);
				try {
					job.run(ph);

					// Make sure the job is set to finished at the end.
					if (newJob.getState() != EState.COMPLETED && newJob.getState() != EState.ERROR
							&& newJob.getState() != EState.CANCELED) {
						newJob.setCompleted(null);
					}
				} catch (Exception e) {
					newJob.setError(e.getLocalizedMessage());
				}
			}
		});
		return newJob;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		executor = Executors.newFixedThreadPool(1);
	}

	@Override
	public Job cancelJob(String id, String message) {
		final JobStruct jobStruct = jobs.get(id);
		if (jobStruct != null) {
			jobStruct.job.setCanceled(message);
			jobStruct.progressHandler.isStopped = true;
			return jobStruct.job;
		}
		log.info("Job " + id + " to cancel wasn't found.");
		return null;
	}

	@Override
	public Job getJob(String id) {
		final JobStruct jobStruct = jobs.get(id);
		if (jobStruct != null) {
			return jobStruct.job;
		}

		return (Job) getCurrentSession().get(JobImpl.class, id);
	}

	/**
	 * This job is finished (complete, error or canceled), so we save it to database and remove it from the local list.
	 */
	@Transactional
	private void finishJob(final Job job) {
		log.info("Job " + job.getId() + " finished in " + (job.getEndDate() - job.getStartDate()) + "ms.");
		try {
			getCurrentSession().save(job);
			jobs.remove(job.getId());
		} catch (HibernateException e) {
			log.error("Error while persisting job '" + job.getId() + "' after it finished.");
		}
	}

	@Override
	public Collection<Job> getRunningJobs() {
		final Set<Job> jobs = new HashSet<Job>();
		for (final JobStruct js : this.jobs.values()) {
			if (js.job.getState() == EState.RUNNING) {
				jobs.add(js.job);
			}
		}
		return jobs;
	}

	@Override
	public Collection<Job> getWaitingJobs() {
		final Set<Job> jobs = new HashSet<Job>();
		for (final JobStruct js : this.jobs.values()) {
			if (js.job.getState() == EState.WAITING) {
				jobs.add(js.job);
			}
		}
		return jobs;
	}

	/**
	 * Internal structure to keep all job information.
	 */
	private static class JobStruct {
		public Job job;
		public ProgressHandler progressHandler;

		public JobStruct(final Job job, final ProgressHandler progressHandler) {
			this.job = job;
			this.progressHandler = progressHandler;
		}
	}

	/**
	 * Internal progress handler.
	 */
	private class ProgressHandler implements IJobProgressHandler {
		private final Job job;
		private boolean isStopped = false;
		private final IJobProgressHandler delegate;

		public ProgressHandler(final Job job, final IJobProgressHandler delegate) {
			this.job = job;
			this.delegate = delegate;
		}

		@Override
		public boolean isCanceled() {
			return isStopped;
		}

		@Override
		public void reportProgress(double progress, String message) {
			job.setProgress(progress, message);

			if (delegate != null) {
				delegate.reportProgress(progress, message);
			}
		}

		@Override
		public void reportCompleted(String message) {
			job.setCompleted(message);

			if (delegate != null) {
				delegate.reportCompleted(message);
			}

			finishJob(job);
		}

		@Override
		public void reportError(String message) {
			job.setError(message);

			if (delegate != null) {
				delegate.reportError(message);
			}

			finishJob(job);
		}
	}
}
