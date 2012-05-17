/**
 * 
 */
package com.collibra.dgc.core.component.bootstrap;

import com.collibra.dgc.core.component.job.JobComponent;

/**
 * @author dieterwachters
 * 
 */
public interface BootstrapComponent {
	/**
	 * Initialize the application with the given database connection options. This will restart the spring context.
	 * @param driver
	 * @param url
	 * @param userName
	 * @param password
	 * @param database
	 */
	public void initialize(String driver, String url, String userName, String password, String database);

	/**
	 * Bootstrap the database with some basic data.
	 * 
	 * Note: this method will return immediately.
	 * @param bootstrapper The bootstrapper to run.
	 * @return The unique identifier of the job running the bootstrapping. Progress information can be accessed through
	 *         the {@link JobComponent}.
	 */
	public String bootstrap(String bootstrapper);
}
