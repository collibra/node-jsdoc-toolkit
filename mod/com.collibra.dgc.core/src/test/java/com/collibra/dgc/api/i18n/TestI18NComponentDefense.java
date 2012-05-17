package com.collibra.dgc.api.i18n;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestI18NComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";

	@Test
	public void testCacheDefenseLocaleStringNull() {
		try {
			i18nComponent.cache(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.I18N_LOCALE_STRING_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testCacheDefenseLocaleStringEmpty() {
		try {
			i18nComponent.cache(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.I18N_LOCALE_STRING_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetDefaultLocalizedMessageDefenseKeyNull() {
		try {
			i18nComponent.getDefaultLocalizedMessage(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.I18N_KEY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetDefaultLocalizedMessageDefenseKeyEmpty() {
		try {
			i18nComponent.getDefaultLocalizedMessage(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.I18N_KEY_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetMessageDefenseLocaleStringNull() {
		try {
			i18nComponent.getMessage(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.I18N_LOCALE_STRING_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetMessageDefenseLocaleStringEmpty() {
		try {
			i18nComponent.getMessage(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.I18N_LOCALE_STRING_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetMessageDefenseKeyNull() {
		try {
			i18nComponent.getMessage(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.I18N_KEY_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetMessageDefenseKeyEmpty() {
		try {
			i18nComponent.getMessage(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.I18N_KEY_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetUserLocalizationDefenseReqNull() {
		try {
			i18nComponent.getUserLocalization(null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.I18N_HTTP_SERVLET_REQUEST_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testSetDefaultLocalizationDefenseDefaultLocaleStringNull() {
		try {
			i18nComponent.setDefaultLocalization(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.I18N_LOCALE_STRING_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testSetDefaultLocalizationDefenseDefaultLocaleStringEmpty() {
		try {
			i18nComponent.setDefaultLocalization(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.I18N_LOCALE_STRING_EMPTY, ex.getErrorCode());
		}
	}

}
