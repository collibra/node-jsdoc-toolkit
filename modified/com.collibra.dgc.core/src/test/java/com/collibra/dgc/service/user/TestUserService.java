package com.collibra.dgc.service.user;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.user.AddressType;
import com.collibra.dgc.core.model.user.InstantMessagingAccountType;
import com.collibra.dgc.core.model.user.PhoneType;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.WebsiteType;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
@Transactional
public class TestUserService extends AbstractDGCBootstrappedApiTest {
	@Test
	public void testAddAditionalEmailaddress() {
		User u = (User) userService.addAditionalEmailaddress("Admin", "admin@mail.com");
		Assert.assertEquals(1, u.getAditionalEmailAddresses().size());
	}

	@Test
	public void testRemoveAditionalEmailaddress() {
		User u = (User) userService.addAditionalEmailaddress("Admin", "admin@mail.com");
		Assert.assertEquals(1, u.getAditionalEmailAddresses().size());
		u = (User) userService.removeAditionalEmailaddress("Admin", u.getAditionalEmailAddresses().iterator().next()
				.getId());
		Assert.assertEquals(0, u.getAditionalEmailAddresses().size());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testRemoveAditionalEmailaddressNotFound() {
		userService.removeAditionalEmailaddress("Admin", "");
	}

	@Test
	public void testChangeAditionalEmailaddress() {
		User u = (User) userService.addAditionalEmailaddress("Admin", "admin@mail.com");
		u = (User) userService.changeAdditionalEmailaddress("Admin", u.getAditionalEmailAddresses().iterator().next()
				.getId(), "admin@e-mail.com");
		Assert.assertEquals("admin@e-mail.com", u.getAditionalEmailAddresses().iterator().next().getEmailAddress());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testChangeAditionalEmailaddressNotFound() {
		userService.changeAdditionalEmailaddress("Admin", "", null);
	}

	@Test
	public void testAddPhone() {
		User u = (User) userService.addPhone("Admin", "0494/12 34 56", PhoneType.MOBILE);
		u = (User) userService.addPhone("Admin", "0494/12 34 57", PhoneType.MOBILE);
		Assert.assertEquals(2, u.getPhoneNumbers().size());
	}

	@Test
	public void testRemovePhone() {
		User u = (User) userService.addPhone("Admin", "0494/12 34 57", PhoneType.MOBILE);
		Assert.assertEquals(1, u.getPhoneNumbers().size());
		u = (User) userService.removePhone("Admin", u.getPhoneNumbers().iterator().next().getId());
		Assert.assertEquals(0, u.getPhoneNumbers().size());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testRemovePhoneNotFound() {
		userService.removePhone("Admin", "");
	}

	@Test
	public void testChangePhone() {
		User u = (User) userService.addPhone("Admin", "0494/12 34 56", PhoneType.MOBILE);
		u = (User) userService.changePhone("Admin", u.getPhoneNumbers().iterator().next().getId(), "0494/12 34 57",
				null);
		Assert.assertEquals("0494/12 34 57", u.getPhoneNumbers().iterator().next().getPhoneNumber());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testChangePhoneNotFound() {
		userService.changePhone("Admin", "", null, null);
	}

	@Test
	public void testAddInstantMessagingAccount() {
		User u = (User) userService
				.addInstantMessagingAccount("Admin", "adminskype", InstantMessagingAccountType.SKYPE);
		Assert.assertEquals(1, u.getInstantMessagingAccounts().size());
	}

	@Test
	public void testRemoveInstantMessagingAccount() {
		User u = (User) userService
				.addInstantMessagingAccount("Admin", "adminskype", InstantMessagingAccountType.SKYPE);
		Assert.assertEquals(1, u.getInstantMessagingAccounts().size());
		u = (User) userService.removeInstantMessagingAccount("Admin", u.getInstantMessagingAccounts().iterator().next()
				.getId());
		Assert.assertEquals(0, u.getInstantMessagingAccounts().size());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testRemoveInstantMessagingAccountNotFound() {
		userService.removeInstantMessagingAccount("Admin", "");
	}

	@Test
	public void testChangeInstantMessagingAccount() {
		User u = (User) userService
				.addInstantMessagingAccount("Admin", "adminskype", InstantMessagingAccountType.SKYPE);
		u = (User) userService.changeInstantMessagingAccount("Admin", u.getInstantMessagingAccounts().iterator().next()
				.getId(), "AdminSkype32", null);
		Assert.assertEquals("AdminSkype32", u.getInstantMessagingAccounts().iterator().next().getAccount());
		Assert.assertEquals(InstantMessagingAccountType.SKYPE, u.getInstantMessagingAccounts().iterator().next()
				.getInstantMessagingAccountType());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testChangeInstantMessagingAccountNotFound() {
		userService.changeInstantMessagingAccount("Admin", "", null, null);
	}

	@Test
	public void testAddWebsite() {
		User u = (User) userService.addWebsite("Admin", "www.example.com", WebsiteType.WEBSITE);
		Assert.assertEquals(1, u.getWebsites().size());
	}

	@Test
	public void testRemoveWebsite() {
		User u = (User) userService.addWebsite("Admin", "http://www.facebook.com/admin", WebsiteType.FACEBOOK);
		Assert.assertEquals(1, u.getWebsites().size());
		u = (User) userService.removeWebsite("Admin", u.getWebsites().iterator().next().getId());
		Assert.assertEquals(0, u.getWebsites().size());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testRemoveWebsiteNotFound() {
		userService.removeWebsite("Admin", "");
	}

	@Test
	public void testChangeWebsite() {
		User u = (User) userService.addWebsite("Admin", "http://www.facebook.com/admin", WebsiteType.FACEBOOK);
		u = (User) userService.changeWebsite("Admin", u.getWebsites().iterator().next().getId(),
				"http://www.facebook.com/admin32", null);
		Assert.assertEquals("http://www.facebook.com/admin32", u.getWebsites().iterator().next().getUrl());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testChangeWebsiteNotFound() {
		userService.changeWebsite("Admin", "", null, null);
	}

	@Test
	public void testAddAddress() {
		User u = (User) userService.addAddress("Admin", "Brussel", "ransbeekstraat", "230", "Neder over heembeek",
				"Belgie", AddressType.WORK);
		Assert.assertEquals(1, u.getAddresses().size());
	}

	@Test
	public void testRemoveAddress() {
		User u = (User) userService.addAddress("Admin", "Brussel", "ransbeekstraat", "230", "Neder over heembeek",
				"Belgie", AddressType.WORK);
		Assert.assertEquals(1, u.getAddresses().size());
		u = (User) userService.removeAddress("Admin", u.getAddresses().iterator().next().getId());
		Assert.assertEquals(0, u.getAddresses().size());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testRemoveAddressNotFound() {
		userService.removeAddress("Admin", "");
	}

	@Test
	public void testChangeAddress() {
		User u = (User) userService.addAddress("Admin", "Brussel", "ransbeekstraat", "230", "Neder over heembeek",
				"Belgie", AddressType.WORK);
		u = (User) userService.changeAddress("Admin", u.getAddresses().iterator().next().getId(), null, null, null,
				null, null, AddressType.HOME);
		Assert.assertEquals(AddressType.HOME, u.getAddresses().iterator().next().getAddressType());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testChangeAddressNotFound() {
		userService.changeAddress("Admin", "", null, null, null, null, null, null);
	}
}
