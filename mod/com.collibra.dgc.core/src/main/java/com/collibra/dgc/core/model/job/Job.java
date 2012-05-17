/**
 * 
 */
package com.collibra.dgc.core.model.job;

/**
 * Represents a job in the database.
 * 
 * Note: jobs are only persisted when finished or in error.
 * @author dieterwachters
 */
public interface Job {
	public enum EState {
		WAITING, RUNNING, CANCELED, COMPLETED, ERROR
	};

	/**
	 * Returns the unique id of this job.
	 */
	String getId();

	/**
	 * @return The person who triggered this job.
	 */
	String getOwner();

	/**
	 * @return True if this job can be canceled. False otherwise.
	 */
	boolean isCancelable();

	/**
	 * The date this job was created.
	 * @return The unit time date of when this job created.
	 */
	long getCreationDate();

	/**
	 * The date this job started.
	 * @return The unit time date of when this job started. 0 if this job is still waiting.
	 */
	long getStartDate();

	/**
	 * The date this job ended.
	 * @return The unit time date of when this job ended. 0 if this job is not finished yet.
	 */
	long getEndDate();

	/**
	 * The current state of this job.
	 * @return The status of this job.
	 */
	EState getState();

	/**
	 * The current status message of this job or the error when in ERROR state.
	 * @return The current status message of this job or the error when in ERROR state.
	 */
	String getMessage();

	/**
	 * The progress percentage of this job.
	 * 
	 * Note: this is not stored to the database, as jobs are only persisted when finished or in error.
	 * @return The progress percentage of this job.
	 */
	double getProgressPercentage();

	/**
	 * Set the state of this job.
	 * 
	 * Note: this should only be called internally by the job service and NOT by the callers.
	 */
	void setState(EState state);

	/**
	 * Update the progress percentage and message.
	 * 
	 * Note: this should only be called internally by the job service and NOT by the callers.
	 */
	void setProgress(double progress, String message);

	/**
	 * Set the state to ERROR, specify the error message and implicitly set the end date.
	 * 
	 * Note: this should only be called internally by the job service and NOT by the callers.
	 */
	void setError(String message);

	/**
	 * Set the status to COMPLETE and implicitly set the end date.
	 * 
	 * Note: this should only be called internally by the job service and NOT by the callers.
	 */
	void setCompleted(String message);

	/**
	 * Set the status to CANCELED and implicitly set the end date.
	 * 
	 * Note: this should only be called internally by the job service and NOT by the callers.
	 */
	void setCanceled(String message);
}
