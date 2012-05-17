/**
 * 
 */
package com.collibra.dgc.ui.core;

import java.io.File;

import com.collibra.dgc.core.application.Application;

/**
 * Some general UI core functionality and configuration.
 * @author dieterwachters
 */
public class UICore {
	public static final String CONFIG_PATH = "ui";
	private static File cacheFolder = null;
	private static File cacheGeneratedFolder = null;
	private static File modulesFolder = null;

	/**
	 * Retrieve the cache folder in the user home directory.
	 */
	public static synchronized final File getCacheDirectory() {
		if (cacheFolder == null) {
			cacheFolder = new File(Application.USER_HOME, "cache");
			if (!cacheFolder.exists())
				cacheFolder.mkdirs();
		}
		return cacheFolder;
	}

	/**
	 * Retrieve the cache folder for the generated (optimized) javascript and css files.
	 */
	public static synchronized final File getCacheGeneratedDirectory() {
		if (cacheGeneratedFolder == null) {
			cacheGeneratedFolder = new File(getCacheDirectory(), "generated");
			if (!cacheGeneratedFolder.exists())
				cacheGeneratedFolder.mkdirs();
		}
		return cacheGeneratedFolder;
	}

	/**
	 * Retrieve the directory containing the module extensions.
	 */
	public static synchronized final File getModulesDirectory() {
		if (modulesFolder == null) {
			modulesFolder = new File(Application.USER_HOME, "modules");
		}
		return modulesFolder;
	}
}
