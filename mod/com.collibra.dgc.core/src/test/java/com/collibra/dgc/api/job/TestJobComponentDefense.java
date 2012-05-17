package com.collibra.dgc.api.job;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestJobComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";

	@Test
	public void testCancelJobDefenseIdNull() {
		try {
			jobComponent.cancelJob(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.JOB_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testCancelJobDefenseIdEmpty() {
		try {
			jobComponent.cancelJob(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.JOB_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testCancelJobDefenseMessageNull() {
		try {
			jobComponent.cancelJob(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.JOB_MESSAGE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testCancelJobDefenseMessageEmpty() {
		try {
			jobComponent.cancelJob(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.JOB_MESSAGE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testgetJobJobDefenseIdNull() {
		try {
			jobComponent.getJob(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.JOB_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testgetJobJobDefenseIdEmpty() {
		try {
			jobComponent.getJob(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.JOB_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testgetJobJobDefenseIdNotFound() {
		try {
			jobComponent.getJob(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			Assert.assertEquals(DGCErrorCodes.JOB_NOT_FOUND, ex.getErrorCode());
		}
	}

}
