/**
 * 
 */
package com.collibra.dgc.core.service.restart;

/**
 * @author dieterwachters
 */
public interface RestartService {
	/**
	 * Add a new restart listener.
	 * @param listener
	 */
	void addRestartListener(final IRestartListener listener);

	/**
	 * Remove the given restart listener
	 * @param listener
	 */
	void removeRestartListener(final IRestartListener listener);

	/**
	 * Restart the server (no full restart, but a spring context restart).
	 */
	void restart();
}
