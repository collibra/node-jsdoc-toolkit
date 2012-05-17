/**
 * 
 */
package com.collibra.dgc.ui.core.modules;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for classes providing modules.
 * @author dieterwachters
 */
public interface IModuleProvider {

	/**
	 * Get the module provided by the given name.
	 * @param name The name of the module to search for.
	 * @return the module data or null if this module is not known to this provider.
	 */
	Module getModule(String path);

	/**
	 * Retrieve the last modified date for the given file.
	 * @param path The path of the file
	 * @return The last modified date of the given file. Will return a negative number if this path is unknown.
	 */
	long getLastModified(String path);

	/**
	 * Retrieve the input stream for the given file.
	 * @param path The path of the file
	 * @return The {@link InputStream} date of the given file or null if the file is not known.
	 * @throws IOException When the input could not be read.
	 */
	InputStream getInputStream(String path) throws IOException;

	/**
	 * Used for priorities when going through the different providers.
	 * @return The number used to determine which provider to look at first.
	 */
	int getOrder();
}
