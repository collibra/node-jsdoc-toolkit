/**
 * 
 */
package com.collibra.dgc.core.component.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.UserComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.user.AddressType;
import com.collibra.dgc.core.model.user.InstantMessagingAccountType;
import com.collibra.dgc.core.model.user.PhoneType;
import com.collibra.dgc.core.model.user.UserData;
import com.collibra.dgc.core.model.user.WebsiteType;
import com.collibra.dgc.core.service.UserService;
import com.collibra.dgc.core.util.Defense;

/**
 * @author dieterwachters
 */
@Service
@Transactional
public class UserComponentImpl implements UserComponent {
	@Autowired
	private UserService userService;

	@Override
	@Transactional
	public UserData addUser(String userName, String password) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		Defense.notEmpty(password, DGCErrorCodes.PASSWORD_NULL, DGCErrorCodes.PASSWORD_EMPTY, "password");

		return userService.addUser(userName, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.UserComponent#addUser(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public UserData addUser(String userName, String password, String firstName, String lastName, String email) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		Defense.notEmpty(password, DGCErrorCodes.PASSWORD_NULL, DGCErrorCodes.PASSWORD_EMPTY, "password");

		Defense.notEmpty(firstName, DGCErrorCodes.USER_FIRST_NAME_NULL, DGCErrorCodes.USER_FIRST_NAME_EMPTY,
				"firstName");
		Defense.notEmpty(lastName, DGCErrorCodes.USER_LAST_NAME_NULL, DGCErrorCodes.USER_LAST_NAME_EMPTY, "lastName");
		Defense.notEmpty(email, DGCErrorCodes.USER_EMAIL_NULL, DGCErrorCodes.USER_EMAIL_EMPTY, "email");

		return userService.addUser(userName, password, firstName, lastName, email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.UserComponent#isPublicRegistrationAllowed()
	 */
	@Override
	public boolean isPublicRegistrationAllowed() {
		return userService.isPublicRegistrationAllowed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.UserComponent#removeUser(java.lang.String)
	 */
	@Override
	@Transactional
	public void removeUser(String userName) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		userService.removeUser(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.UserComponent#changeUser(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public UserData changeUser(String userName, String firstName, String lastName, String email) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		return userService.changeUser(userName, firstName, lastName, email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.UserComponent#changePassword(java.lang.String, java.lang.String)
	 */
	@Transactional
	@Override
	public UserData changePassword(String userName, String password) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		Defense.notEmpty(password, DGCErrorCodes.PASSWORD_NULL, DGCErrorCodes.PASSWORD_EMPTY, "password");
		return userService.changePassword(userName, password);
	}

	@Transactional
	@Override
	public UserData changeLanguage(String userName, String language) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		Defense.notEmpty(language, DGCErrorCodes.USER_LANGUAGE_NULL, DGCErrorCodes.USER_LANGUAGE_EMPTY, "language");

		return userService.changeLanguage(userName, language);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.UserComponent#getUser(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public UserData getUser(String userName) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		return userService.getUser(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.UserComponent#getCurrentUser()
	 */
	@Override
	public UserData getCurrentUser() {
		return userService.getCurrentUser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.component.UserComponent#logout()
	 */
	@Override
	public void logout() {
		userService.logout();
	}

	@Override
	public UserData addWebsite(String userName, String url, String websiteType) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		Defense.notEmpty(url, DGCErrorCodes.USER_URL_NULL, DGCErrorCodes.USER_URL_EMPTY, "url");
		Defense.notEmpty(websiteType, DGCErrorCodes.USER_WEBSITE_TYPE_NULL, DGCErrorCodes.USER_WEBSITE_TYPE_EMPTY,
				"websiteType");

		return userService.addWebsite(userName, url, WebsiteType.valueOf(websiteType));
	}

	@Override
	public UserData changeWebsite(String userName, String rId, String url, String websiteType) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");

		return userService.changeWebsite(userName, rId, url, WebsiteType.valueOf(websiteType));
	}

	@Override
	public UserData removeWebsite(String userName, String rId) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");

		return userService.removeWebsite(userName, rId);
	}

	@Override
	public UserData addPhone(String userName, String phoneNumber, String phoneType) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		Defense.notEmpty(phoneNumber, DGCErrorCodes.USER_PHONE_NULL, DGCErrorCodes.USER_PHONE_EMPTY, "phoneNumber");
		Defense.notEmpty(phoneType, DGCErrorCodes.USER_PHONE_TYPE_NULL, DGCErrorCodes.USER_PHONE_TYPE_EMPTY,
				"phoneType");

		return userService.addPhone(userName, phoneNumber, PhoneType.valueOf(phoneType));
	}

	@Override
	public UserData changePhone(String userName, String rId, String phoneNumber, String phoneType) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");

		return userService.changePhone(userName, rId, phoneNumber, PhoneType.valueOf(phoneType));
	}

	@Override
	public UserData removePhone(String userName, String rId) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		return userService.removePhone(userName, rId);
	}

	@Override
	public UserData addInstantMessagingAccount(String userName, String account, String accountType) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		Defense.notEmpty(account, DGCErrorCodes.USER_IM_ACCOUNT_NULL, DGCErrorCodes.USER_IM_ACCOUNT_EMPTY, "account");
		Defense.notEmpty(accountType, DGCErrorCodes.USER_IM_ACCOUNT_TYPE_NULL,
				DGCErrorCodes.USER_IM_ACCOUNT_TYPE_EMPTY, "accountType");

		return userService.addInstantMessagingAccount(userName, account,
				InstantMessagingAccountType.valueOf(accountType));
	}

	@Override
	public UserData removeInstantMessagingAccount(String userName, String rId) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		return userService.removeInstantMessagingAccount(userName, rId);
	}

	@Override
	public UserData changeInstantMessagingAccount(String userName, String rId, String account, String accountType) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		return userService.changeInstantMessagingAccount(userName, rId, account,
				InstantMessagingAccountType.valueOf(accountType));
	}

	@Override
	public UserData addAddress(String userName, String city, String street, String number, String province,
			String country, String addressType) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		Defense.notEmpty(city, DGCErrorCodes.USER_ADDRESS_CITY_NULL, DGCErrorCodes.USER_ADDRESS_CITY_EMPTY, "city");
		Defense.notEmpty(street, DGCErrorCodes.USER_ADDRESS_STREET_NULL, DGCErrorCodes.USER_ADDRESS_STREET_EMPTY,
				"street");
		Defense.notEmpty(number, DGCErrorCodes.USER_ADDRESS_NUMBER_NULL, DGCErrorCodes.USER_ADDRESS_NUMBER_EMPTY,
				"number");
		Defense.notEmpty(province, DGCErrorCodes.USER_ADDRESS_PROVINCE_NULL, DGCErrorCodes.USER_ADDRESS_PROVINCE_EMPTY,
				"province");
		Defense.notEmpty(country, DGCErrorCodes.USER_ADDRESS_COUNTRY_NULL, DGCErrorCodes.USER_ADDRESS_COUNTRY_EMPTY,
				"country");
		Defense.notEmpty(addressType, DGCErrorCodes.USER_ADDRESS_TYPE_NULL, DGCErrorCodes.USER_ADDRESS_TYPE_EMPTY,
				"addressType");

		return userService.addAddress(userName, city, street, number, province, country,
				AddressType.valueOf(addressType));
	}

	@Override
	public UserData removeAddress(String userName, String rId) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		return userService.removeAddress(userName, rId);
	}

	@Override
	public UserData changeAddress(String userName, String rId, String city, String street, String number,
			String province, String country, String addressType) {
		Defense.notEmpty(userName, DGCErrorCodes.USER_NAME_NULL, DGCErrorCodes.USER_NAME_EMPTY, "userName");
		return userService.changeAddress(userName, rId, city, street, number, province, country,
				AddressType.valueOf(addressType));
	}
}
