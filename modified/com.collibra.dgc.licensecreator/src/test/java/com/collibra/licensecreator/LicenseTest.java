package com.collibra.licensecreator;

import static org.junit.Assert.fail;

import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.smardec.license4j.License;
import com.smardec.license4j.LicenseManager;
import com.smardec.license4j.LicenseNotFoundException;

public class LicenseTest {

	private static final String VALID_LICENSE = "src/test/resources/validlicense.lic";
	private static final String EXPIRED_LICENSE = "src/test/resources/expiredlicense.lic";
	private static final String EXPIRE_KEY = "Expire";

	private static final String PUBLIC_KEY = "308201B73082012C06072A8648CE3804013082011F02818100FD7F53811D75122952DF4A9C2EECE4E7F611B7523CEF4400C31E3F80B6512669455D402251FB593D8D58FABFC5F5BA30F6CB9B556CD7813B801D346FF26660B76B9950A5A49F9FE8047B1022C24FBBA9D7FEB7C61BF83B57E7C6A8A6150F04FB83F6D3C51EC3023554135A169132F675F3AE2B61D72AEFF22203199DD14801C70215009760508F15230BCCB292B982A2EB840BF0581CF502818100F7E1A085D69B3DDECBBCAB5C36B857B97994AFBBFA3AEA82F9574C0B3D0782675159578EBAD4594FE67107108180B449167123E84C281613B7CF09328CC8A6E13C167A8B547C8D28E0A3AE1E2BB3A675916EA37F0BFA213562F1FB627A01243BCCA4F1BEA8519089A883DFE15AE59F06928B665E807B552564014C3BFECF492A0381840002818023E617CB70309559F3FCE97341F99FE0857A7344613477ACBFAF0F4327EE5F9797597C7E45AD718FB1345E30DE099F96B5069193C2280F5D4D9F696304FD255C85E096F560EE0826FE761D4B7A18672539D6D1ACBD51F9CDF461EA0EB7820D76B7B3A42BD2E5246729350B970E731FABAB259E94C6E5C73F61917B74D9685D29";

	@Before
	public void setUp() {
		LicenseManager.setPublicKey(PUBLIC_KEY);
	}

	/**
	 * Tests the validation of a license that is valid
	 */
	@Test
	public void testValidLicense() {
		License license;
		try {
			license = LicenseManager.loadLicense(VALID_LICENSE);
			if (LicenseManager.isValid(license)) {
				String expire = (String) license.getFeature(EXPIRE_KEY);
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
				Date expireDate = dateFormatter.parse(expire);
				if (expireDate.compareTo(new Date()) < 0) {
					fail("License is Expired");
				} else {
					System.out.println("Valid license");
				}
			} else {
				fail("License is not valid");
			}
		} catch (LicenseNotFoundException e) {
			fail("License not found");
		} catch (GeneralSecurityException e) {
			fail("Permission problem: " + e);
		} catch (ParseException e) {
			fail("Date parsing failed");
		}
	}

	/**
	 * Tests whether a license has expired
	 */
	@Test
	public void testExpiredLicense() {
		License license;
		try {
			license = LicenseManager.loadLicense(EXPIRED_LICENSE);
			if (LicenseManager.isValid(license)) {
				String expire = (String) license.getFeature(EXPIRE_KEY);
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
				Date expireDate = dateFormatter.parse(expire);

				// check whether expiration date is smaller than current date
				if (expireDate.compareTo(new Date()) < 0) {
					System.out.println("License is Expired");
				} else {
					fail("License should be expired");
				}
			} else {
				fail("License is not valid");
			}
		} catch (LicenseNotFoundException e) {
			fail("License not found");
		} catch (GeneralSecurityException e) {
			fail("Permission problem: " + e);
		} catch (ParseException e) {
			fail("Date parsing failed");
		}
	}
}
