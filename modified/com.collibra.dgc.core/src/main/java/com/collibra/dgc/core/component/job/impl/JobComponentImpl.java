/**
 * 
 */
package com.collibra.dgc.core.component.job.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.job.JobComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.job.Job;
import com.collibra.dgc.core.service.job.JobService;
import com.collibra.dgc.core.util.Defense;

/**
 * @author dieterwachters
 * 
 */
@Service
public class JobComponentImpl implements JobComponent {
	@Autowired
	private JobService jobService;

	@Transactional
	@Override
	public void cancelJob(String id, String message) {
		Defense.notEmpty(id, DGCErrorCodes.JOB_ID_NULL, DGCErrorCodes.JOB_ID_EMPTY, "id");
		Defense.notEmpty(message, DGCErrorCodes.JOB_MESSAGE_NULL, DGCErrorCodes.JOB_MESSAGE_EMPTY, "message");

		final Job job = jobService.cancelJob(id, message);
		if (job == null) {
			throw new EntityNotFoundException(DGCErrorCodes.JOB_NOT_FOUND, id);
		}
	}

	@Transactional
	@Override
	public Job getJob(String id) {
		Defense.notEmpty(id, DGCErrorCodes.JOB_ID_NULL, DGCErrorCodes.JOB_ID_EMPTY, "id");
		final Job job = jobService.getJob(id);
		if (job == null) {
			throw new EntityNotFoundException(DGCErrorCodes.JOB_NOT_FOUND, id);
		}
		return job;
	}

	@Transactional
	@Override
	public Collection<Job> getRunningJobs() {
		return jobService.getRunningJobs();
	}

	@Transactional
	@Override
	public Collection<Job> getWaitingJobs() {
		return jobService.getWaitingJobs();
	}
}
