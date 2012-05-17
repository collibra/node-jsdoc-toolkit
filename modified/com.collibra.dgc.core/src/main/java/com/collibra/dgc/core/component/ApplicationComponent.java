/**
 * 
 */
package com.collibra.dgc.core.component;

/**
 * @author dieterwachters
 * 
 */
public interface ApplicationComponent {
	/**
	 * Get the application version number.
	 * @return the application version number.
	 */
	public String getVersion();

	/**
	 * Get the application build number.
	 * @return the application build number
	 */
	public String getBuildNumber();
}
