/**
 * 
 */
package com.collibra.dgc.core.component;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.user.AddressType;
import com.collibra.dgc.core.model.user.InstantMessagingAccountType;
import com.collibra.dgc.core.model.user.PhoneType;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.UserData;

/**
 * With the UserComponent you can handle all actions on users: create, update, remove, log out ...
 */
public interface UserComponent {
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
	 * Add a new user with the given user name and password.
	 * @param userName The user name for the new user.
	 * @param password The password for the new user.
	 * @return The newly created {@link User} object.
	 * @throws IllegalArgumentException When a user with this user name already exists.
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
	 * @throws IllegalArgumentException When a user with this user name already exists.
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
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 */
	public void removeUser(String userName);

	/**
	 * Change the basic information of the given user.
	 * @param userName The user name for the user to change
	 * @param firstName The new first name.
	 * @param lastName The new last name.
	 * @param email The new email address.
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 */
	public UserData changeUser(String userName, String firstName, String lastName, String email);

	/**
	 * Change the password for the given user
	 * @param userName The user name of the user to change the password for.
	 * @param password The new password for the user.
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 */
	public UserData changePassword(String userName, String password);

	/**
	 * Change the preferred language for the given user
	 * @param userName The user name of the user to change the preferred language for.
	 * @param language The new preferred language for the user.
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 */
	public UserData changeLanguage(String userName, String language);

	/**
	 * Adds an instant messaging account to the user
	 * @param userName the user
	 * @param account the account
	 * @param accountType the type of account
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws com.collibra.dgc.core.exceptions.IllegalArgumentException with errorcode
	 *             {@link DGCErrorCodes#USER_NAME_NULL}, {@link DGCErrorCodes#USER_NAME_EMPTY},
	 *             {@link DGCErrorCodes#USER_IM_ACCOUNT_NULL}, {@link DGCErrorCodes#USER_IM_ACCOUNT_EMPTY},
	 *             {@link DGCErrorCodes#USER_IM_ACCOUNT_TYPE_NULL}, {@link DGCErrorCodes#USER_IM_ACCOUNT_TYPE_EMPTY}
	 */
	UserData addInstantMessagingAccount(String userName, String account, String accountType);

	/**
	 * Removes an instant messaging account
	 * @param userName the user
	 * @param rId the resource id of the IM account
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws EntityNotFoundException When the instant messaging account could not be found
	 */
	UserData removeInstantMessagingAccount(String userName, String rId);

	/**
	 * Changes an instantmessingaccount
	 * @param userName the user
	 * @param rId the resource id of the IM account
	 * @param account the new accountname
	 * @param accountType the new {@link InstantMessagingAccountType} in string form
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws EntityNotFoundException When the instant messaging account could not be found
	 * @throws com.collibra.dgc.core.exceptions.IllegalArgumentException with error codes
	 *             {@link DGCErrorCodes#USER_NAME_NULL}, {@link DGCErrorCodes#USER_NAME_EMPTY},
	 */
	UserData changeInstantMessagingAccount(String userName, String rId, String account, String accountType);

	/**
	 * Removes a phone number from a user
	 * @param userName the user
	 * @param rId the resource id of the phone
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws EntityNotFoundException When the phone number could not be found.
	 */
	UserData removePhone(String userName, String rId);

	/**
	 * Changes a phone number
	 * @param userName the user
	 * @param rId the resource id of the phone
	 * @param phoneNumber the new phone number
	 * @param phoneType the new {@link PhoneType} in string representation
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws EntityNotFoundException When the phone number could not be found.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#USER_NAME_EMPTY}
	 */
	UserData changePhone(String userName, String rId, String phoneNumber, String phoneType);

	/**
	 * Adds a phone number to a user
	 * @param userName the user
	 * @param phoneNumber the phone number
	 * @param phoneType the new {@link PhoneType} in string representation
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws EntityNotFoundException When the phone number could not be found.
	 * @throws IllegalArgumentException with error code {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#USER_NAME_EMPTY}, {@link DGCErrorCodes#USER_PHONE_NULL} ,
	 *             {@link DGCErrorCodes#USER_PHONE_EMPTY}, {@link DGCErrorCodes#USER_PHONE_TYPE_NULL} ,
	 *             {@link DGCErrorCodes#USER_PHONE_TYPE_EMPTY}
	 */
	UserData addPhone(String userName, String phoneNumber, String phoneType);

	/**
	 * Removes a website
	 * @param userName the user
	 * @param rId the resource id of the website
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws EntityNotFoundException When a website was not found
	 */
	UserData removeWebsite(String userName, String rId);

	/**
	 * Changes a website
	 * @param userName the user
	 * @param rId the resource id of the website
	 * @param url the new url
	 * @param websiteType the type of the website
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws EntityNotFoundException When a website was not found
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_WEBSITE_TYPE_NULL},
	 *             {@link DGCErrorCodes#USER_WEBSITE_TYPE_EMPTY}, {@link DGCErrorCodes#USER_URL_NULL},
	 *             {@link DGCErrorCodes#USER_WEBSITE_TYPE_EMPTY} {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#USER_NAME_EMPTY},
	 */
	UserData changeWebsite(String userName, String rId, String url, String websiteType);

	/**
	 * Adds a website
	 * @param userName the user
	 * @param url the url of the website
	 * @param websiteType the type of the website
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws EntityNotFoundException When a website was not found
	 * @throws IllegalArgumentException with error codes {@link DGCErrorCodes#USER_WEBSITE_TYPE_NULL},
	 *             {@link DGCErrorCodes#USER_WEBSITE_TYPE_EMPTY}, {@link DGCErrorCodes#USER_URL_NULL},
	 *             {@link DGCErrorCodes#USER_WEBSITE_TYPE_EMPTY} {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#USER_NAME_EMPTY},
	 */
	UserData addWebsite(String userName, String url, String websiteType);

	/**
	 * Adds an address
	 * @param userName the user
	 * @param city the city
	 * @param street the street
	 * @param number the number
	 * @param province the province or state
	 * @param country the country
	 * @param addressType the {@link AddressType}
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws IllegalArgumentException With the following error codes {@link DGCErrorCodes#USER_ADDRESS_CITY_EMPTY},
	 *             {@link DGCErrorCodes#USER_ADDRESS_CITY_NULL}, {@link DGCErrorCodes#USER_ADDRESS_STREET_NULL}
	 *             {@link DGCErrorCodes#USER_ADDRESS_STREET_EMPTY}, {@link DGCErrorCodes#USER_ADDRESS_NUMBER_NULL},
	 *             {@link DGCErrorCodes#USER_ADDRESS_NUMBER_EMPTY},{@link DGCErrorCodes#USER_ADDRESS_PROVINCE_NULL},
	 *             {@link DGCErrorCodes#USER_ADDRESS_PROVINCE_EMPTY}, {@link DGCErrorCodes#USER_ADDRESS_COUNTRY_NULL},
	 *             {@link DGCErrorCodes#USER_ADDRESS_COUNTRY_EMPTY}, {@link DGCErrorCodes#USER_ADDRESS_TYPE_NULL},
	 *             {@link DGCErrorCodes#USER_ADDRESS_TYPE_NULL}, {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#USER_NAME_EMPTY}
	 */
	UserData addAddress(String userName, String city, String street, String number, String province, String country,
			String addressType);

	/**
	 * removes an address
	 * @param userName the user
	 * @param rId the resource id of the address
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws EntityNotFoundException When a address was not found
	 */
	UserData removeAddress(String userName, String rId);

	/**
	 * Changes an address
	 * @param userName the user
	 * @param rId the resource id of the address
	 * @param city the city
	 * @param street the street
	 * @param number the house number
	 * @param province the province or state
	 * @param country the country
	 * @param addressType the {@link AddressType}
	 * @return The updated {@link User} object.
	 * @throws EntityNotFoundException When a user with this user name is not found.
	 * @throws EntityNotFoundException When a address was not found
	 * @throws IllegalArgumentException With the following error codes {@link DGCErrorCodes#USER_NAME_NULL},
	 *             {@link DGCErrorCodes#USER_NAME_EMPTY}.
	 */
	UserData changeAddress(String userName, String rId, String city, String street, String number, String province,
			String country, String addressType);
}
