/**
 * 
 */
package com.collibra.dgc.core.component;

import java.util.List;
import java.util.Map;

/**
 * @author fvdmaele
 * 
 */
public interface SystemConfigurationComponent {

	/**
	 * Get the String value of the given path.
	 * @path The path of the configuration value to get.
	 * @return The string value or null if it does not exist.
	 */
	public String getString(String path);

	/**
	 * Get the boolean value of the given path.
	 * @path The path of the configuration value to get.
	 * @return The boolean value or null if it does not exist.
	 */
	public Boolean getBoolean(String path);

	/**
	 * Get the Integer value of the given path.
	 * @path The path of the configuration value to get.
	 * @return The integer value or null if it does not exist.
	 */
	public Integer getInteger(String path);

	/**
	 * Get the value at the given path as properties
	 * @param path The path of the configuration value to get.
	 * @return the map with the property values (empty if no properties found).
	 */
	public Map<String, String> getProperties(String path);

	/**
	 * Get a {@link Map<String, String>} that maps property keys on their value. The keys will start with the given
	 * startPath {@link String}.
	 * 
	 * @param startPath the {@link String} with which the returned keys must start with.
	 * @return a {@link Map<String, String>} that maps a property key on its value
	 */
	public Map<String, String> getConfigurationSection(String startPath);

	/**
	 * Get the list of string values of the given path.
	 * @path The path of the configuration value to get.
	 * @return The list of string values or null if it does not exist.
	 */
	public List<String> getStringList(String path);

	/**
	 * Get timestamp the configuration was last changed (or startup).
	 * @return The timestamp the configuration was last changed (or startup).
	 */
	public long getLastModified();

	/**
	 * Set the value of the given path. The value can be a {@link String}, {@link Integer}, {@link List} of Strings, or
	 * a {@link Boolean}.
	 * 
	 * @param path The path of the property to change
	 * @param value The new value for the property
	 */
	public void setProperty(String path, Object value);

	/**
	 * Clear the value at the given path
	 * @param path The path of the value to clear.
	 */
	public void clear(String path);
}
