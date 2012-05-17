package com.collibra.dgc.api.bootstrap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestBootstrapComponentDefense extends AbstractDGCBootstrappedApiTest {

	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";

	@Test
	public void testInitializeDefenceDriverNull() {
		try {
			bootstrapComponent.initialize(NULL, NOT_EMPTY, NOT_EMPTY, null, NOT_EMPTY);
			fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.BOOTSTRAP_DRIVER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testInitializeDefenceDriverEmpty() {
		try {
			bootstrapComponent.initialize(EMPTY, NOT_EMPTY, NOT_EMPTY, null, NOT_EMPTY);
			fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.BOOTSTRAP_DRIVER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testInitializeDefenceUrlNull() {
		try {
			bootstrapComponent.initialize(NOT_EMPTY, NULL, NOT_EMPTY, null, NOT_EMPTY);
			fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.BOOTSTRAP_URL_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testInitializeDefenceUrlEmpty() {
		try {
			bootstrapComponent.initialize(NOT_EMPTY, EMPTY, NOT_EMPTY, null, NOT_EMPTY);
			fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.BOOTSTRAP_URL_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testInitializeDefenceUsernameNull() {
		try {
			bootstrapComponent.initialize(NOT_EMPTY, NOT_EMPTY, NULL, null, NOT_EMPTY);
			fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.BOOTSTRAP_USERNAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testInitializeDefenceUsernameEmpty() {
		try {
			bootstrapComponent.initialize(NOT_EMPTY, NOT_EMPTY, EMPTY, null, NOT_EMPTY);
			fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.BOOTSTRAP_USERNAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testInitializeDefenceDatabaseNull() {
		try {
			bootstrapComponent.initialize(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, null, NULL);
			fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.BOOTSTRAP_DATABASE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testInitializeDefenceDatabaseEmpty() {
		try {
			bootstrapComponent.initialize(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, null, EMPTY);
			fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.BOOTSTRAP_DATABASE_EMPTY, ex.getErrorCode());
		}
	}
}
