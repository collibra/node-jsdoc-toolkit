package com.collibra.dgc.core.component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.collibra.dgc.core.exceptions.AuthorizationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.security.authorization.Permissions;

public interface ConfigurationComponent {

	/**
	 * Get the String value of the given path.
	 * @path The path of the configuration value to get.
	 * @return The string value or null if it does not exist.
	 * @throws DGCErrorCodes.AUTHORIZATION_FAILED if current users doesn't have Permissions.ADMIN
	 */
	String getString(String path);

	/**
	 * Get the String value of the given path.
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @return The string value or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	String getString(String path, String rId);

	/**
	 * Get the String value of the given path.
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @userName the {@link UserData} to which the configuration should be localized. Can be null if the configuration
	 *           is not local to a given user.
	 * @return The string value or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	String getString(String path, String rId, String userName);

	/**
	 * Get the String value of the given path. The method will try to return the most localized String:</br> It uses the
	 * following fallback mechanism </br>
	 * <ul>
	 * <li>Find the property for the Path + User + Resource, if not found</li>
	 * <li>Find the property for the Path + User, if not found</li>
	 * <li>Find the property for the Path + Resource, if not found</li>
	 * <li>Find the property for the Path</li>
	 * <ul>
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @userName the {@link UserData} to which the configuration should be localized. Can be null if the configuration
	 *           is not local to a given user.
	 * @return The string value or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	String getLocalizedString(String path, String rId, String userName);

	/**
	 * Clear the value at the given path
	 * @param path The path of the value to clear.
	 * @throws DGCErrorCodes.AUTHORIZATION_FAILED if current users doesn't have Permissions.ADMIN
	 */
	void clear(String path);

	/**
	 * Clear the value at the given path
	 * @param path The path of the value to clear.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @userName the {@link UserData} to which the configuration should be localized. Can be null if the configuration
	 *           is not local to a given user.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	void clear(String path, String rId, String userName);

	/**
	 * Set the value of the given path. The value can be a {@link String}, {@link Integer}, {@link List} of Strings, or
	 * a {@link Boolean}. A {@link AuthorizationException} will be thrown with the message key
	 * {@link DGCErrorCodes.AUTHORIZATION_FAILED} if the current user does not have the {@link Permissions.ADMIN} right.
	 * 
	 * @param path The path of the property to change
	 * @param value The new value for the property
	 */
	void setProperty(String path, Object value);

	/**
	 * Set the value of the given path. The value can be a {@link String}, {@link Integer}, {@link List} of Strings, or
	 * a {@link Boolean}. A {@link AuthorizationException} will be thrown with the message key
	 * {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the current user does not have the
	 * {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * 
	 * @param path The path of the property to change
	 * @param value The new value for the property
	 * @param resource The resource id {@link String} for which to set the property
	 */
	void setProperty(String path, Object value, String rId);

	/**
	 * Set the value of the given path. The value can be a {@link String}, {@link Integer}, {@link List} of Strings, or
	 * a {@link Boolean}. A {@link AuthorizationException} will be thrown with the message key
	 * {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the current user does not equal the provided
	 * user.
	 * 
	 * @param path The path of the property to change
	 * @param value The new value for the property
	 * @param resource the resource id {@link String} for which the set the property
	 * @param {@link UserData} The username of the user for which to set the property
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 */
	void setProperty(String path, Object value, String rId, String userName);

	/**
	 * Get the list of string values of the given path.
	 * @path The path of the configuration value to get.
	 * @return The list of string values or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key {@link DGCErrorCodes.AUTHORIZATION_FAILED} if the
	 *         current user does not have the {@link Permissions.ADMIN} right.
	 */
	Collection<String> getStringList(String path);

	/**
	 * Get the list of string values of the given path.
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @return The list of string values or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Collection<String> getStringList(String path, String rId);

	/**
	 * Get the list of string values of the given path.
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @userName the {@link UserData} to which the configuration should be localized. Can be null if the configuration
	 *           is not local to a given user.
	 * @return The list of string values or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Collection<String> getStringList(String path, String rId, String userName);

	/**
	 * Get the list of string values of the given path. The method will try to return the most localized string
	 * values:</br> It uses the following fallback mechanism </br>
	 * <ul>
	 * <li>Find the property for the Path + User + Resource, if not found</li>
	 * <li>Find the property for the Path + User, if not found</li>
	 * <li>Find the property for the Path + Resource, if not found</li>
	 * <li>Find the property for the Path</li>
	 * <ul>
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @userName the {@link UserData} to which the configuration should be localized. Can be null if the configuration
	 *           is not local to a given user.
	 * @return The list of string values or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Collection<String> getLocalizedStringList(String path, String rId, String userName);

	/**
	 * Get the Integer value of the given path.
	 * @path The path of the configuration value to get.
	 * @return The integer value or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key {@link DGCErrorCodes.AUTHORIZATION_FAILED} if the
	 *         current user does not have the {@link Permissions.ADMIN} right.
	 */
	Integer getInteger(String path);

	/**
	 * Get the Integer value of the given path.
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @return The integer value or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 * @throws {@link AuthorizationException} with the message key
	 */
	Integer getInteger(String path, String rId);

	/**
	 * Get the Integer value of the given path.
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @userName the {@link UserData} to which the configuration should be localized. Can be null if the configuration
	 *           is not local to a given user.
	 * @return The integer value or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 */
	Integer getInteger(String path, String rId, String userName);

	/**
	 * Get the Integer value of the given path. The method will try to return the most localized integer values:</br> It
	 * uses the following fallback mechanism </br>
	 * <ul>
	 * <li>Find the property for the Path + User + Resource, if not found</li>
	 * <li>Find the property for the Path + User, if not found</li>
	 * <li>Find the property for the Path + Resource, if not found</li>
	 * <li>Find the property for the Path</li>
	 * <ul>
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @userName the {@link UserData} to which the configuration should be localized. Can be null if the configuration
	 *           is not local to a given user.
	 * @return The integer value or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Integer getLocalizedInteger(String path, String rId, String userName);

	/**
	 * Get the boolean value of the given path.
	 * @path The path of the configuration value to get.
	 * @return The boolean value or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key {@link DGCErrorCodes.AUTHORIZATION_FAILED} if the
	 *         current user does not have the {@link Permissions.ADMIN} right.
	 */
	Boolean getBoolean(String path);

	/**
	 * Get the boolean value of the given path.
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @return The boolean value or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Boolean getBoolean(String path, String rId, String userName);

	/**
	 * Get the boolean value of the given path. The method will try to return the most localized boolean values:</br> It
	 * uses the following fallback mechanism </br>
	 * <ul>
	 * <li>Find the property for the Path + User + Resource, if not found</li>
	 * <li>Find the property for the Path + User, if not found</li>
	 * <li>Find the property for the Path + Resource, if not found</li>
	 * <li>Find the property for the Path</li>
	 * <ul>
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @return The boolean value or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Boolean getLocalizedBoolean(String path, String rId, String userName);

	/**
	 * Get the boolean value of the given path.
	 * @path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @userName the {@link UserData} to which the configuration should be localized. Can be null if the configuration
	 *           is not local to a given user.
	 * @return The boolean value or null if it does not exist.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Boolean getBoolean(String path, String rId);

	/**
	 * Get the value at the given path as properties
	 * @param path The path of the configuration value to get.
	 * @return the map with the property values (empty if no properties found).
	 * @throws {@link AuthorizationException} with the message key {@link DGCErrorCodes.AUTHORIZATION_FAILED} if the
	 *         current user does not have the {@link Permissions.ADMIN} right.
	 */
	Map<String, String> getProperties(String path);

	/**
	 * Get the value at the given path as properties
	 * @param path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @return the map with the property values (empty if no properties found).
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Map<String, String> getProperties(String path, String rId);

	/**
	 * Get the value at the given path as properties
	 * @param path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @userName the {@link UserData} to which the configuration should be localized. Can be null if the configuration
	 *           is not local to a given user.
	 * @return the map with the property values (empty if no properties found).
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Map<String, String> getProperties(String path, String rId, String userName);

	/**
	 * Get the value at the given path as properties. The method will try to return the most localized properties:</br>
	 * It uses the following fallback mechanism </br>
	 * <ul>
	 * <li>Find the properties for the Path + User + Resource, if not found</li>
	 * <li>Find the properties for the Path + User, if not found</li>
	 * <li>Find the properties for the Path + Resource, if not found</li>
	 * <li>Find the properties for the Path</li>
	 * <ul>
	 * @param path The path of the configuration value to get.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @userName the {@link UserData} to which the configuration should be localized. Can be null if the configuration
	 *           is not local to a given user.
	 * @return the map with the property values (empty if no properties found).
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Map<String, String> getLocalizedProperties(String path, String rId, String userName);

	/**
	 * Get timestamp the configuration was last changed (or startup).
	 * @return The timestamp the configuration was last changed (or startup).
	 */
	long getLastModified();

	/**
	 * Get a {@link Map<String, String>} that maps property keys on their value. The keys will start with the given
	 * startPath {@link String}.
	 * 
	 * @param startPath the {@link String} with which the returned keys must start with.
	 * @return a {@link Map<String, String>} that maps a property key on its value
	 * @throws {@link AuthorizationException} with the message key {@link DGCErrorCodes.AUTHORIZATION_FAILED} if the
	 *         current user does not have the {@link Permissions.ADMIN} right.
	 */
	Map<String, String> getConfigurationSection(String startPath);

	/**
	 * Get a {@link Map<String, String>} that maps property keys on their contextualized value. The keys will start with
	 * the given startPath {@link String}.
	 * 
	 * @param startPath the {@link String} with which the returned keys must start with.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @return a {@link Map<String, String>} that maps a property key on its contextualized value
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Map<String, String> getConfigurationSection(String startPath, String rId);

	/**
	 * Get a {@link Map<String, String>} that maps property keys on their contextualized value. The keys will start with
	 * the given startPath {@link String}.
	 * 
	 * @param startPath the {@link String} with which the returned keys must start with.
	 * @rId the resource id {@link String} to which the configuration should be localized. Can be null if the
	 *      configuration is not local to a given resource.
	 * @userName the {@link UserData} to which the configuration should be localized. Can be null if the configuration
	 *           is not local to a given user.
	 * @return a {@link Map<String, String>} that maps a property key on its contextualized value
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the userName doesn't equal the current
	 *         user or if the user does not have the {@link Permissions.CUSTOMIZE_CONFIG} right.
	 * @throws {@link AuthorizationException} with the message key
	 *         {@link DGCErrorCodes.GLOSSARY_CONFIGURATION_EDIT_NO_PERMISSION} if the user does not have the
	 *         {@link Permissions.CUSTOMIZE_CONFIG} on the resource or the {@link Permissions.ADMIN} right.
	 */
	Map<String, String> getConfigurationSection(String startPath, String rId, String userName);

}
