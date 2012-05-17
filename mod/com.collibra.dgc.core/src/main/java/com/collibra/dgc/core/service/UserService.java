/**
 * 
 */
package com.collibra.dgc.core.service;

import java.util.Collection;

import com.collibra.dgc.core.dto.filters.ResourceFilter;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.user.AddressType;
import com.collibra.dgc.core.model.user.InstantMessagingAccountType;
import com.collibra.dgc.core.model.user.PhoneType;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.model.user.WebsiteType;

/**
 * @author dieterwachters
 * 
 */
public interface UserService {
	/**
	 * Log out the current user.
	 */
	public void logout();

	/**
	 * Returns the current user.
	 * @return The current user or null if not logged in.
	 */
	public UserData getCurrentUser();

	/**
	 * Find a user by its user name.
	 * @param userName The user name to look for.
	 * @return The found {@link User} object or null if not found.
	 */
	public UserData getUser(String userName);

	/**
	 * Get a user by its user name. if not found, throw a {@link EntityNotFoundException}.
	 * 
	 * @param userName the username of the user
	 * @return The found {@link UserData}
	 */
	public UserData getUserWithError(String userName);

	/**
	 * Add a new user with the given user name and password.
	 * @param userName The user name for the new user.
	 * @param password The password for the new user.
	 * @return The newly created {@link User} object.
	 */
	public UserData addUser(String userName, String password);

	/**
	 * Add a new user with the given data.
	 * @param userName The user name for the new user.
	 * @param password The password for the new user.
	 * @param firstName The first name of the new user.
	 * @param lastName The last name of the new user.
	 * @param email The email address of the new user.
	 * @return The newly created {@link User} object.
	 */
	public UserData addUser(String userName, String password, String firstName, String lastName, String email);

	/**
	 * Check if public registrations are allowed or not.
	 * @return True if any user or guest can register a new user. If false, only administrators can create new users.
	 */
	public boolean isPublicRegistrationAllowed();

	/**
	 * Remove the user with the given user name.
	 * @param userName The user name of the user to remove.
	 */
	public void removeUser(String userName);

	/**
	 * Change the basic information of the given user.
	 * @param userName The user name for the user to change
	 * @param firstName The new first name.
	 * @param lastName The new last name.
	 * @param email The new email address.
	 * @return The updated {@link User} object.
	 */
	public UserData changeUser(String userName, String firstName, String lastName, String email);

	/**
	 * Change the password for the given user
	 * @param userName The user name of the user to change the password for.
	 * @param password The new password for the user.
	 * @return The updated {@link User} object.
	 */
	public UserData changePassword(String userName, String password);

	/**
	 * Change the preferred language for the given user
	 * @param userName The user name of the user to change the preferred language for.
	 * @param language The new preferred language for the user.
	 * @return The updated {@link User} object.
	 */
	public UserData changeLanguage(String userName, String language);

	/**
	 * Find the users that satisfy the given filter.
	 * @param filter The filter defining which users to get.
	 * @param offset The offset (for paging)
	 * @param max The maximum number of results to return (paging)
	 * @return The collection of found users.
	 */
	public Collection<UserData> findUsers(ResourceFilter filter, int offset, int max);

	/**
	 * Add an email address to the user
	 * @param userName The user name for the user to change
	 * @param email The new email address.
	 * @return The updated {@link User} object.
	 */
	public UserData addAditionalEmailaddress(String userName, String email);

	/**
	 * Remove an email adress from a user
	 * @param userName The user name for the user to change
	 * @param rId the resource id of the phone
	 * @return The updated {@link User} object.
	 */
	public UserData removeAditionalEmailaddress(String userName, String rId);

	/**
	 * change an email adress from a user
	 * @param userName The user name for the user to change
	 * @param rId the resource id of the additional email adddress
	 * @param email The new email address.
	 * @return The updated {@link User} object.
	 */
	public UserData changeAdditionalEmailaddress(String userName, String rId, String email);

	/**
	 * Add an phone to a user
	 * @param userName The user name for the user to change
	 * @param phone The phone number
	 * @param phoneType the type of phone
	 * @return The updated {@link User} object.
	 */
	public UserData addPhone(String userName, String phone, PhoneType phoneType);

	/**
	 * @param userName The user name for the user to change
	 * @param rId the resource id of the phone
	 * @return The updated {@link User} object.
	 */
	public UserData removePhone(String userName, String rId);

	/**
	 * change a phone number from a user
	 * @param userName The user name for the user to change
	 * @param rId the resource id of the phone
	 * @param phone The new phone number. (null if unchanged)
	 * @param phone The new phone type. (null if unchanged)
	 * @return The updated {@link User} object.
	 */
	UserData changePhone(String userName, String rId, String phone, PhoneType phoneType);

	/**
	 * Adds an instant messaging account to a user.
	 * @param userName The user name of the user to change
	 * @param account The name of the instant messaging account
	 * @param accountType The type of account
	 * @return The updated {@link User} object.
	 */
	UserData addInstantMessagingAccount(String userName, String account, InstantMessagingAccountType accountType);

	/**
	 * Removes in instant messaging account from a user
	 * @param userName the user name of the user to change
	 * @param rId the resource id of the IM account
	 * @return The updated {@link User} object.
	 */
	UserData removeInstantMessagingAccount(String userName, String rId);

	/**
	 * changes the accounttype and the account name of an instantmessagingaccount
	 * @param userName the user name of the user to change
	 * @param rId the resource id of the IM account
	 * @param account the new account name (pass null if unchanged)
	 * @param accountType the new account type (pass null if unchanged)
	 * @return The updated {@link User} object.
	 */
	UserData changeInstantMessagingAccount(String userName, String rId, String account,
			InstantMessagingAccountType accountType);

	/**
	 * Changes a users website
	 * @param userName the user whose website to change
	 * @param rId the resource id of the website
	 * @param url the new url (null for unchanged)
	 * @param websiteType the new type (null for unchanged)
	 * @return The updated {@link User} object.
	 */
	UserData changeWebsite(String userName, String rId, String url, WebsiteType websiteType);

	/**
	 * Removes a user's website
	 * @param userName The user whose website to remove
	 * @param rId the resource id of the website
	 * @return The updated {@link User} object.
	 */
	UserData removeWebsite(String userName, String rId);

	/**
	 * Adds a user's website to the user
	 * @param userName the user to add the website to
	 * @param url the url of the webiste
	 * @param websiteType the type of the website
	 * @return The updated {@link User} object.
	 */
	UserData addWebsite(String userName, String url, WebsiteType websiteType);

	/**
	 * changes a user's adress
	 * @param userName the user
	 * @param rId the resource id of the adress
	 * @param city the new city (null if unedited)
	 * @param street the new street (null if unedited)
	 * @param number the new house number (null if unedited)
	 * @param province the new province (null if unedited)
	 * @param country the new country (null if unedited)
	 * @param addressType the new {@link AddressType} (null if unedited)
	 * @return The updated {@link User} object.
	 * 
	 */
	UserData changeAddress(String userName, String rId, String city, String street, String number, String province,
			String country, AddressType addressType);

	/**
	 * removes an adress
	 * @param userName the user
	 * @param rId the resource id of the adress
	 * @return
	 */
	UserData removeAddress(String userName, String rId);

	/**
	 * adds an adress to a user
	 * @param userName the user
	 * @param city the city
	 * @param street the street
	 * @param number the number
	 * @param province the province or state
	 * @param country the country
	 * @param addressType the {@link AddressType}
	 * @return
	 */
	UserData addAddress(String userName, String city, String street, String number, String province, String country,
			AddressType addressType);

	/**
	 * Retrieves a user based on it's id
	 * @param userRId the user's Rid
	 * @return the user, or null
	 */
	public User getUserById(String userRId);

	/**
	 * Retrieves a user based on it's id
	 * @param userRId the user's id
	 * @return the user
	 * @throws EntityNotFoundException
	 */
	User getUserByIdWithError(String userRId);

}
