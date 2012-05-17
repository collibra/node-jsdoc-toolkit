package com.collibra.dgc.api.status;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestStatusComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";

	@Test
	public void testAddStatusDefenseSignifierNull() {
		try {
			statusComponent.addStatus(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.STATUS_TERM_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddStatusDefenseSignifierEmpty() {
		try {
			statusComponent.addStatus(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.STATUS_TERM_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSignifierDefenseRIdNull() {
		try {
			statusComponent.changeSignifier(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.STATUS_TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSignifierDefenseRIdEmpty() {
		try {
			statusComponent.changeSignifier(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.STATUS_TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSignifierDefenseSignifierNull() {
		try {
			statusComponent.changeSignifier(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.STATUS_TERM_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSignifierDefenseSignifierEmpty() {
		try {
			statusComponent.changeSignifier(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.STATUS_TERM_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStatusDefenseRIdNull() {
		try {
			statusComponent.getStatus(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.STATUS_TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStatusDefenseRIdEmpty() {
		try {
			statusComponent.getStatus(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.STATUS_TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveStatusDefenseRIdNull() {
		try {
			statusComponent.removeStatus(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.STATUS_TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveStatusDefenseRIdEmpty() {
		try {
			statusComponent.removeStatus(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.STATUS_TERM_ID_EMPTY, ex.getErrorCode());
		}
	}
}
