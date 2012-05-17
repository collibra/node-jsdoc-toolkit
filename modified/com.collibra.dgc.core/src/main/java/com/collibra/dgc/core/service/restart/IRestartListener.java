/**
 * 
 */
package com.collibra.dgc.core.service.restart;

/**
 * Interface for classes that to be notified of a restart request in order to do its part of the restart.
 * @author dieterwachters
 */
public interface IRestartListener {
	/**
	 * Restart was triggered.
	 */
	public void restart();
}
