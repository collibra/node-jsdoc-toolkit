/**
 * 
 */
package com.collibra.dgc.core.service.job;

/**
 * An interface for handling the progress of the job. We use this mechanism to be able implement multiple progress
 * listener systems later if needed.
 * @author dieterwachters
 */
public interface IJobProgressHandler {
	/**
	 * Returns true if a 'cancel' has been called for this job. The job implementation is responsible for checking this
	 * at certain points to see if it should stop its execution.
	 * @return True if this job must stop.
	 */
	public boolean isCanceled();

	/**
	 * The progress percentage of this job.
	 * @param progress The percentage indicating the progress of the job.
	 * @param message An optional message indicating what the job is currently doing.
	 */
	public void reportProgress(double progress, String message);

	/**
	 * Must be called by the job implementation (or reportError method).
	 * 
	 * Note: the jobservice will automatically also call this method after a run (an not completed, error or canceled)
	 * @param message An optional message indicating the state.
	 */
	public void reportCompleted(String message);

	/**
	 * Called by the job implementation if something goes wrong.
	 * @param message An optional message indicating the error.
	 */
	public void reportError(String message);
}
