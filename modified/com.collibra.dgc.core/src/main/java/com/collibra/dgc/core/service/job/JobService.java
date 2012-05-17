/**
 * 
 */
package com.collibra.dgc.core.service.job;

import java.util.Collection;

import com.collibra.dgc.core.model.job.Job;

/**
 * Service for handling long running tasks.
 * @author dieterwachters
 */
public interface JobService {
	/**
	 * Start the given job.
	 * @param job The job to run
	 * @return The unique identifier for this job.
	 */
	public Job runJob(IJobRunner job);

	/**
	 * Start the given job with a specific progressHandler.
	 * @param job The job to run
	 * @param progressHandler The optional progress handler to report to.
	 * @return The unique identifier for this job.
	 */
	public Job runJob(IJobRunner job, IJobProgressHandler progressHandler);

	/**
	 * Cancel the given job. Note: this method will return immediately and not wait until the job is actually stopped.
	 * @param id The unique identifier of the job to stop.
	 * @param cancel An optional message for the cancelation.
	 * @return The canceled job.
	 */
	public Job cancelJob(String id, String message);

	/**
	 * Get the job with the given id.
	 * @param id The unique identifier of the job to get.
	 * @return The job object.
	 */
	public Job getJob(String id);

	/**
	 * Get all the currently running jobs.
	 */
	public Collection<Job> getRunningJobs();

	/**
	 * Get all the job waiting to run in the future.
	 */
	public Collection<Job> getWaitingJobs();
}
