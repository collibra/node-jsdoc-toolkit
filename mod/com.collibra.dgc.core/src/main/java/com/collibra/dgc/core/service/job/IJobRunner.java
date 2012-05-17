/**
 * 
 */
package com.collibra.dgc.core.service.job;

/**
 * Interface that jobs should implements in order to be ran with the {@link JobService}
 * @author dieterwachters
 */
public interface IJobRunner {
	/**
	 * Run the given job.
	 * @param progressHandler The {@link IProgressHandler} to use
	 */
	public void run(IJobProgressHandler progressHandler);

	/**
	 * @return True if this job can be canceled.
	 */
	public boolean isCancelable();
}
