/**
 * 
 */
package com.collibra.dgc.core.service;

import java.net.URL;
import java.util.List;
import java.util.Map;

import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.security.authorization.Permissions;

/**
 * Service for accessing the configuration.
 * 
 * Note: this won't do any authorization checking. This needs to be handled in the components.
 * @author dieterwachters
 */
public interface ConfigurationService {
	/**
	 * Contribute default configuration values by passing the URL of the file containing the defaults.
	 * @param url The URL of the file containing the default.
	 */
	void contributeDefaults(final URL url);

	/**
	 * Get the String value of the given path.
	 * @path The path of the configuration value to get.
	 * @return The string value or null if it does not exist.
	 */
	String getString(String path);

	/**
	 * Get the String value of the given path.
	 * @path The path of the configuration value to get.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 * @return The string value or null if it does not exist.
	 */
	String getString(String path, String resourceId, UserData user);

	/**
	 * Get the String value of the given path. The method will try to return the most localized String:</br> It uses the following fallback mechanism </br>
	 * <ul>
	 * 	<li>Find the property for the Path + User + Resource, if not found</li>
	 * 	<li>Find the property for the Path + User, if not found</li>
	 * 	<li>Find the property for the Path + Resource, if not found</li>
	 * 	<li>Find the property for the Path</li>
	 * <ul>
	 * @path The path of the configuration value to get.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 * @return The string value or null if it does not exist.
	 */
	String getLocalizedString(String path, String resourceId, UserData user);

	/**
	 * Clear the value at the given path
	 * @param path The path of the value to clear.
	 */
	void clear(String path);

	/**
	 * Clear the value at the given path
	 * @param path The path of the value to clear.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 */
	void clear(String path, String resourceId, UserData user);

	/**
	 * Set the value of the given path. The value can be a {@link String}, {@link Integer}, {@link List} of Strings, or
	 * a {@link Boolean}. A {@link AuthorizationException} will be thrown with the message key
	 * {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the current user does not have the
	 * {@link Permissions.ADMIN} right.
	 * 
	 * @param path The path of the property to change
	 * @param value The new value for the property
	 */
	void setProperty(String path, Object value);

	/**
	 * Set the value of the given path. The value can be a {@link String}, {@link Integer}, {@link List} of Strings, or
	 * a {@link Boolean}. A {@link AuthorizationException} will be thrown with the message key
	 * {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the current user does not equal the provided
	 * user.
	 * 
	 * @param path The path of the property to change
	 * @param value The new value for the property
	 * @param {@link UserData} The username of the user for which to set the property
	 */
	void setProperty(String path, Object value, UserData user);

	/**
	 * Set the value of the given path. The value can be a {@link String}, {@link Integer}, {@link List} of Strings, or
	 * a {@link Boolean}. A {@link AuthorizationException} will be thrown with the message key
	 * {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the current user does not have the
	 * {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * 
	 * @param path The path of the property to change
	 * @param value The new value for the property
	 * @param resource The resource id {@link String} for which to set the property
	 */
	void setProperty(String path, Object value, String resourceId);

	/**
	 * Set the value of the given path. The value can be a {@link String}, {@link Integer}, {@link List} of Strings, or
	 * a {@link Boolean}. A {@link AuthorizationException} will be thrown with the message key
	 * {@link MessageKeys.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the current user does not equal the provided
	 * user.
	 * 
	 * @param path The path of the property to change
	 * @param value The new value for the property
	 * @param resource the resource id {@link String} for which the set the property
	 * @param {@link UserData} The username of the user for which to set the property
	 */
	void setProperty(String path, Object value, String resourceId, UserData user);

	/**
	 * Get the list of string values of the given path.
	 * @path The path of the configuration value to get.
	 * @return The list of string values or null if it does not exist.
	 */
	List<String> getStringList(String path);

	/**
	 * Get the list of string values of the given path.
	 * @path The path of the configuration value to get.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 * @return The list of string values or null if it does not exist.
	 */
	List<String> getStringList(String path, String resourceId, UserData user);

	/**
	 * Get the list of string values of the given path. The method will try to return the most localized string values:</br> It uses the following fallback mechanism </br>
	 * <ul>
	 * 	<li>Find the property for the Path + User + Resource, if not found</li>
	 * 	<li>Find the property for the Path + User, if not found</li>
	 * 	<li>Find the property for the Path + Resource, if not found</li>
	 * 	<li>Find the property for the Path</li>
	 * <ul>
	 * @path The path of the configuration value to get.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 * @return The list of string values or null if it does not exist.
	 */
	List<String> getLocalizedStringList(String path, String resourceId, UserData user);

	/**
	 * Get the Integer value of the given path.
	 * @path The path of the configuration value to get.
	 * @return The integer value or null if it does not exist.
	 */
	Integer getInteger(String path);

	/**
	 * Get the Integer value of the given path.
	 * @path The path of the configuration value to get.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 * @return The integer value or null if it does not exist.
	 */
	Integer getInteger(String path, String resourceId, UserData user);

	/**
	 * Get the Integer value of the given path. The method will try to return the most localized integer values:</br> It uses the following fallback mechanism </br>
	 * <ul>
	 * 	<li>Find the property for the Path + User + Resource, if not found</li>
	 * 	<li>Find the property for the Path + User, if not found</li>
	 * 	<li>Find the property for the Path + Resource, if not found</li>
	 * 	<li>Find the property for the Path</li>
	 * <ul>
	 * @path The path of the configuration value to get.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 * @return The integer value or null if it does not exist.
	 */
	Integer getLocalizedInteger(String path, String resourceId, UserData user);

	/**
	 * Get the boolean value of the given path.
	 * @path The path of the configuration value to get.
	 * @return The boolean value or null if it does not exist.
	 */
	Boolean getBoolean(String path);

	/**
	 * Get the boolean value of the given path.
	 * @path The path of the configuration value to get.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 * @return The boolean value or null if it does not exist.
	 */
	Boolean getBoolean(String path, String resourceId, UserData user);

	/**
	 * Get the boolean value of the given path. The method will try to return the most localized boolean values:</br> It uses the following fallback mechanism </br>
	 * <ul>
	 * 	<li>Find the property for the Path + User + Resource, if not found</li>
	 * 	<li>Find the property for the Path + User, if not found</li>
	 * 	<li>Find the property for the Path + Resource, if not found</li>
	 * 	<li>Find the property for the Path</li>
	 * <ul>
	 * @path The path of the configuration value to get.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 * @return The boolean value or null if it does not exist.
	 */
	Boolean getLocalizedBoolean(String path, String resourceId, UserData user);

	/**
	 * Get the value at the given path as properties
	 * @param path The path of the configuration value to get.
	 * @return the map with the property values (empty if no properties found).
	 */
	Map<String, String> getProperties(String path);

	/**
	 * Get the value at the given path as properties
	 * @param path The path of the configuration value to get.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 * @return the map with the property values (empty if no properties found).
	 */
	Map<String, String> getProperties(String path, String resourceId, UserData user);

	/**
	 * Get the value at the given path as properties. The method will try to return the most localized properties:</br> It uses the following fallback mechanism </br>
	 * <ul>
	 * 	<li>Find the properties for the Path + User + Resource, if not found</li>
	 * 	<li>Find the properties for the Path + User, if not found</li>
	 * 	<li>Find the properties for the Path + Resource, if not found</li>
	 * 	<li>Find the properties for the Path</li>
	 * <ul>
	 * @param path The path of the configuration value to get.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 * @return the map with the property values (empty if no properties found).
	 */
	Map<String, String> getLocalizedProperties(String path, String resourceId, UserData user);

	/**
	 * Get timestamp the configuration was last changed (or startup).
	 * @return The timestamp the configuration was last changed (or startup).
	 */
	long getLastModified();

	/**
	 * Flushes the configuration. This needs to be called after calling setter methods.
	 */
	void flush();

	/**
	 * Get a {@link Map<String, String>} that maps property keys on their value. The keys will start with the given
	 * startPath {@link String}.
	 * 
	 * @param startPath the {@link String} with which the returned keys must start with.
	 * @return a {@link Map<String, String>} that maps a property key on its value
	 */
	Map<String, String> getConfigurationSection(String startPath);

	/**
	 * Get a {@link Map<String, String>} that maps property keys on their contextualized value. The keys will start with
	 * the given startPath {@link String}.
	 * 
	 * @param startPath the {@link String} with which the returned keys must start with.
	 * @resourceId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *             configuration is not local to a given resource.
	 * @user the {@link UserData} to which the configuration should be localized. Can be null if the configuration is
	 *       not local to a given user.
	 * @return a {@link Map<String, String>} that maps a property key on its contextualized value
	 */
	Map<String, String> getConfigurationSection(String startPath, String resourceId, UserData user);
}
