package com.collibra.dgc.core.service.bootstrapper;

import java.io.InputStream;

import com.collibra.dgc.core.service.job.IJobProgressHandler;

/**
 * Bootstrapper interface
 * @author dtrog
 * 
 */
public interface BootstrapService {

	/**
	 * This will do a basic bootstrap from the xmi files.
	 * @param progressHandler The progress handler to use for running the job.
	 */
	public void bootstrap(IJobProgressHandler progressHandler);

	/**
	 * This will bootstrap the requested bootstrap script
	 * @param bootstrap The bootstrap script to run.
	 * @param progressHandler The progress handler to use for running the job.
	 */
	public void bootstrap(String bootstrap, IJobProgressHandler progressHandler);

	/**
	 * This will bootstrap the requested bootstrap script.
	 * @param bootstrap The bootstrap script to run
	 * @param dataInput The inputstream with the actual data
	 * @param dtdInput The inputstream with the dtd structure
	 * @param progressHandler The progress handler to use for running the job.
	 */
	public void bootstrap(String bootstrap, InputStream dataInput, InputStream dtdInput,
			IJobProgressHandler progressHandler);
}