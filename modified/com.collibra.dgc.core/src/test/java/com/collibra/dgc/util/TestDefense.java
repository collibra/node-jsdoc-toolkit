package com.collibra.dgc.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.util.Defense;

public class TestDefense {

	@Test(expected = DGCException.class)
	public void testNotNullFail() {
		Defense.notNull(null, DGCErrorCodes.ARGUMENT_NULL, "Object");
	}

	@Test
	public void testNotNull() {
		Defense.notNull("notnull", DGCErrorCodes.ARGUMENT_NULL, "Object");
	}

	@Test(expected = DGCException.class)
	public void testNotEmptyFail() {
		Defense.notEmpty(new ArrayList<Object>(), DGCErrorCodes.ARGUMENT_NULL, DGCErrorCodes.ARGUMENT_INVALID,
				"ArrayList");
	}

	@Test
	public void testNotEmpty() {
		List<Object> l = new ArrayList<Object>();
		l.add(new Object());
		Defense.notEmpty(l, DGCErrorCodes.ARGUMENT_NULL, DGCErrorCodes.ARGUMENT_INVALID, "ArrayList");
	}

	@Test(expected = DGCException.class)
	public void testAssertTrueFail() {
		Defense.assertTrue(false, DGCErrorCodes.ARGUMENT_INVALID, "");
	}

	@Test
	public void testAssertTrue() {
		Defense.assertTrue(true, DGCErrorCodes.ARGUMENT_INVALID, "");
	}

	@Test(expected = DGCException.class)
	public void testAssertFalseFail() {
		Defense.assertFalse(true, DGCErrorCodes.ARGUMENT_INVALID, "");
	}

	@Test
	public void testAssertFalse() {
		Defense.assertFalse(false, DGCErrorCodes.ARGUMENT_INVALID, "");
	}

	@Test(expected = DGCException.class)
	public void testNotEmptyStringFailWithEmptyString() {
		Defense.notEmpty("", DGCErrorCodes.ARGUMENT_NULL, DGCErrorCodes.ARGUMENT_INVALID, "ArrayList");
	}

	@Test(expected = DGCException.class)
	public void testNotEmptyStringFailWithNull() {
		String s = null;
		Defense.notEmpty(s, DGCErrorCodes.ARGUMENT_NULL, DGCErrorCodes.ARGUMENT_INVALID, "ArrayList");
	}

	@Test
	public void testNotEmptyString() {
		Defense.notEmpty("String", DGCErrorCodes.ARGUMENT_NULL, DGCErrorCodes.ARGUMENT_INVALID, "ArrayList");
	}
}
