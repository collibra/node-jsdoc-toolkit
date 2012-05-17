package com.collibra.dgc.dao;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.model.user.Email;
import com.collibra.dgc.core.model.user.PhoneNumber;
import com.collibra.dgc.core.model.user.PhoneType;
import com.collibra.dgc.core.model.user.User;
import com.collibra.dgc.core.model.user.impl.EmailImpl;
import com.collibra.dgc.core.model.user.impl.PhoneNumberImpl;
import com.collibra.dgc.core.model.user.impl.UserImpl;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
@Transactional
public class TestUserDao extends AbstractDGCBootstrappedApiTest {

	@Test
	public void testSaveWithEmailAdresses() {
		List<Email> emails = new ArrayList<Email>();
		emails.add(new EmailImpl("JosVanOs@belgacom.com"));
		emails.add(new EmailImpl("Jos@gmail.com"));

		User u = new UserImpl("User", "password", "Jos", "Van Os", "JosVanOs@OsEnCo.com");
		u.setAditionalEmailAddresses(emails);
		userDao.save(u);
		User jos = userDao.get(u.getId());
		Assert.assertEquals(2, jos.getAditionalEmailAddresses().size());
	}

	@Test
	public void testSaveWithPhones() {
		List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
		phoneNumbers.add(new PhoneNumberImpl("0476 12 34 56", PhoneType.MOBILE));
		phoneNumbers.add(new PhoneNumberImpl("03 12 34 56", PhoneType.WORK));

		User u = new UserImpl("User", "password", "Jos", "Van Os", "JosVanOs@OsEnCo.com");
		u.setPhoneNumbers(phoneNumbers);
		userDao.save(u);
		User jos = userDao.get(u.getId());
		Assert.assertEquals(2, jos.getPhoneNumbers().size());
	}
}
