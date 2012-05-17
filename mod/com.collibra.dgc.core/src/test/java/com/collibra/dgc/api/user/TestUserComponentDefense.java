package com.collibra.dgc.api.user;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestUserComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";

	@Test
	public void testAddUserDefenseUserNameNull() {
		try {
			userComponent.addUser(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUserDefenseUserNameEmpty() {
		try {
			userComponent.addUser(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUserDefensePasswordNull() {
		try {
			userComponent.addUser(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.PASSWORD_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUserDefensePasswordEmpty() {
		try {
			userComponent.addUser(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.PASSWORD_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUser2DefenseUserNameNull() {
		try {
			userComponent.addUser(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUser2DefenseUserNameEmpty() {
		try {
			userComponent.addUser(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUser2DefensePasswordNull() {
		try {
			userComponent.addUser(NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.PASSWORD_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUser2DefensePasswordEmpty() {
		try {
			userComponent.addUser(NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.PASSWORD_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUser2DefenseFirstNameNull() {
		try {
			userComponent.addUser(NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_FIRST_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUser2DefenseFirstNameEmpty() {
		try {
			userComponent.addUser(NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_FIRST_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUser2DefenseLastNameNull() {
		try {
			userComponent.addUser(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_LAST_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUser2DefenseLastNameEmpty() {
		try {
			userComponent.addUser(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_LAST_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUser2DefenseEmailNull() {
		try {
			userComponent.addUser(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_EMAIL_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddUser2DefenseEmailEmpty() {
		try {
			userComponent.addUser(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_EMAIL_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeLanguageDefenseUserNameNull() {
		try {
			userComponent.changeLanguage(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeLanguageDefenseUserNameEmpty() {
		try {
			userComponent.changeLanguage(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeLanguageDefenseLanguageNull() {
		try {
			userComponent.changeLanguage(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_LANGUAGE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeLanguageDefenseLanguageEmpty() {
		try {
			userComponent.changeLanguage(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_LANGUAGE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangePasswordDefenseUserNameNull() {
		try {
			userComponent.changePassword(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangePasswordDefenseUserNameEmpty() {
		try {
			userComponent.changePassword(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangePasswordDefensePasswordNull() {
		try {
			userComponent.changePassword(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.PASSWORD_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangePasswordDefensePasswordEmpty() {
		try {
			userComponent.changePassword(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.PASSWORD_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeUserDefenseUserNameNull() {
		try {
			userComponent.changeUser(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeUserDefenseUserNameEmpty() {
		try {
			userComponent.changeUser(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetUserDefenseUserNameNull() {
		try {
			userComponent.getUser(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetUserDefenseUserNameEmpty() {
		try {
			userComponent.getUser(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveUserDefenseUserNameNull() {
		try {
			userComponent.removeUser(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveUserDefenseUserNameEmpty() {
		try {
			userComponent.removeUser(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddInstantMessagingAccountDefenseUserNameNull() {
		try {
			userComponent.addInstantMessagingAccount(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddInstantMessagingAccountDefenseUserNameEmpty() {
		try {
			userComponent.addInstantMessagingAccount(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddInstantMessagingAccountDefenseAccountNull() {
		try {
			userComponent.addInstantMessagingAccount(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_IM_ACCOUNT_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddInstantMessagingAccountDefenseAccountEmpty() {
		try {
			userComponent.addInstantMessagingAccount(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_IM_ACCOUNT_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddInstantMessagingAccountDefenseAccountTypeNull() {
		try {
			userComponent.addInstantMessagingAccount(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_IM_ACCOUNT_TYPE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddInstantMessagingAccountDefenseAccountTypeEmpty() {
		try {
			userComponent.addInstantMessagingAccount(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_IM_ACCOUNT_TYPE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddPhoneDefenseUserNameNull() {
		try {
			userComponent.addPhone(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddPhoneDefenseUserNameEmpty() {
		try {
			userComponent.addPhone(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddPhoneDefenseAccountNull() {
		try {
			userComponent.addPhone(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_PHONE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddPhoneDefenseAccountEmpty() {
		try {
			userComponent.addPhone(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_PHONE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddPhoneDefenseAccountTypeNull() {
		try {
			userComponent.addPhone(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_PHONE_TYPE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddPhoneDefenseAccountTypeEmpty() {
		try {
			userComponent.addPhone(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_PHONE_TYPE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddWebsiteDefenseUserNameNull() {
		try {
			userComponent.addWebsite(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddWebsiteDefenseUserNameEmpty() {
		try {
			userComponent.addWebsite(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddWebsiteDefenseAccountNull() {
		try {
			userComponent.addWebsite(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_URL_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddWebsiteDefenseAccountEmpty() {
		try {
			userComponent.addWebsite(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_URL_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddWebsiteDefenseAccountTypeNull() {
		try {
			userComponent.addWebsite(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_WEBSITE_TYPE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddWebsiteDefenseAccountTypeEmpty() {
		try {
			userComponent.addWebsite(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_WEBSITE_TYPE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeInstantMessagingAccountDefenseUserNameNull() {
		try {
			userComponent.changeInstantMessagingAccount(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeInstantMessagingAccountDefenseUserNameEmpty() {
		try {
			userComponent.changeInstantMessagingAccount(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangePhoneDefenseUserNameNull() {
		try {
			userComponent.changePhone(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangePhoneDefenseUserNameEmpty() {
		try {
			userComponent.changePhone(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeWebsiteDefenseUserNameNull() {
		try {
			userComponent.changeWebsite(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeWebsiteDefenseUserNameEmpty() {
		try {
			userComponent.changeWebsite(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveInstantMessagingAccountDefenseUserNameNull() {
		try {
			userComponent.removeInstantMessagingAccount(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveInstantMessagingAccountDefenseUserNameEmpty() {
		try {
			userComponent.removeInstantMessagingAccount(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveWebsiteDefenseUserNameNull() {
		try {
			userComponent.removeWebsite(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveWebsiteDefenseUserNameEmpty() {
		try {
			userComponent.removeWebsite(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseUserNameNull() {
		try {
			userComponent.addAddress(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseUserNameEmpty() {
		try {
			userComponent.addAddress(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseCityNull() {
		try {
			userComponent.addAddress(NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_CITY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseCityEmpty() {
		try {
			userComponent.addAddress(NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_CITY_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseStreetNull() {
		try {
			userComponent.addAddress(NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_STREET_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseStreetEmpty() {
		try {
			userComponent.addAddress(NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_STREET_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseNumberNull() {
		try {
			userComponent.addAddress(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_NUMBER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseNumberEmpty() {
		try {
			userComponent.addAddress(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_NUMBER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseProvinceNull() {
		try {
			userComponent.addAddress(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_PROVINCE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseProvinceEmpty() {
		try {
			userComponent.addAddress(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_PROVINCE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseCountryNull() {
		try {
			userComponent.addAddress(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_COUNTRY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseCountryEmpty() {
		try {
			userComponent.addAddress(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_COUNTRY_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseAddressTypeNull() {
		try {
			userComponent.addAddress(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_TYPE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddAddressDefenseAddressTypeEmpty() {
		try {
			userComponent.addAddress(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.USER_ADDRESS_TYPE_EMPTY, ex.getErrorCode());
		}
	}
}
