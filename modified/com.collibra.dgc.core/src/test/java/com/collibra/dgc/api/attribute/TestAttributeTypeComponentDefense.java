package com.collibra.dgc.api.attribute;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.attribute.AttributeTypeComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

/**
 * {@link AttributeTypeComponent} API tests.
 * 
 * @author pmalarme
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestAttributeTypeComponentDefense extends AbstractDGCBootstrappedApiTest {

	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";

	private static final Collection EMPTY_COLLECTION = new ArrayList();
	private static final Collection NOT_EMPTY_COLLECTION = Arrays.asList("value", "value");
	private static final Collection NULL_COLLETION = null;

	@Test
	public void testAddStringAttributeTypeDefenseNull() {

		try {
			attributeTypeComponent.addStringAttributeType(NULL, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddStringAttributeTypeDefenseEmpty() {

		try {
			attributeTypeComponent.addStringAttributeType(EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddStringAttributeTypeDefenseNotFound() {

		try {
			attributeTypeComponent.addStringAttributeType(EMPTY, null);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddValueListAttributeTypeDefenseSignifierNull() {

		try {
			attributeTypeComponent.addValueListAttributeType(NULL, null, false, NOT_EMPTY_COLLECTION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddValueListAttributeTypeDefenseSignifierEmpty() {

		try {
			attributeTypeComponent.addValueListAttributeType(EMPTY, null, false, NOT_EMPTY_COLLECTION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddValueListAttributeTypeDefenseAllowedValuesNull() {

		try {
			attributeTypeComponent.addValueListAttributeType(NOT_EMPTY, null, false, NULL_COLLETION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_VALUE_LIST_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddValueListAttributeTypeDefenseAllowedValuesEmpty() {

		try {
			attributeTypeComponent.addValueListAttributeType(NOT_EMPTY, null, false, EMPTY_COLLECTION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_VALUE_LIST_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeAllowedValuesDefenseRIdNull() {

		try {
			attributeTypeComponent.changeAllowedValues(NULL, NOT_EMPTY_COLLECTION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeAllowedValuesDefenseRIdEmpty() {

		try {
			attributeTypeComponent.changeAllowedValues(EMPTY, NOT_EMPTY_COLLECTION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeAllowedValuesDefenseRIdNotFound() {

		try {
			attributeTypeComponent.changeAllowedValues(NON_EXISTANT, NOT_EMPTY_COLLECTION);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeAllowedValueseDefenseNewAllowedValuesNull() {

		try {
			attributeTypeComponent.changeAllowedValues(NON_EXISTANT, NULL_COLLETION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_VALUE_LIST_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeAllowedValueseDefenseNewAllowedValuesEmpty() {

		try {
			attributeTypeComponent.changeAllowedValues(NON_EXISTANT, EMPTY_COLLECTION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_VALUE_LIST_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeDescriptionDefenseRIdNull() {

		try {
			attributeTypeComponent.changeDescription(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeDescriptionDefenseRIdEmpty() {

		try {
			attributeTypeComponent.changeDescription(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeDescriptionDefenseRIdNotFound() {

		try {
			attributeTypeComponent.changeDescription(NON_EXISTANT, NOT_EMPTY);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSignifierDefenseRIdNull() {

		try {
			attributeTypeComponent.changeSignifier(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSignifierDefenseRIdEmpty() {

		try {
			attributeTypeComponent.changeSignifier(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSignifierDefenseRIdNotFound() {

		try {
			attributeTypeComponent.changeSignifier(NON_EXISTANT, NOT_EMPTY);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAllowedValuesDefenseRIdNull() {

		try {
			attributeTypeComponent.getAllowedValues(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAllowedValuesDefenseRIdEmpty() {

		try {
			attributeTypeComponent.getAllowedValues(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAllowedValuesDefenseRIdNotFound() {

		try {
			attributeTypeComponent.getAllowedValues(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeDefenseRIdNull() {

		try {
			attributeTypeComponent.getAttributeType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeDefenseRIdEmpty() {

		try {
			attributeTypeComponent.getAttributeType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeDefenseRIdNotFound() {

		try {
			attributeTypeComponent.getAttributeType(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeBySignifierDefenseSignifierNull() {

		try {
			attributeTypeComponent.getAttributeTypeBySignifier(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeBySignifierDefenseSignifierEmpty() {

		try {
			attributeTypeComponent.getAttributeTypeBySignifier(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeBySignifierDefenseSignifierNotFound() {

		try {
			attributeTypeComponent.getAttributeTypeBySignifier(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_NOT_FOUND_SIGNIFIER, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeDescriptionDefenseDescriptionNull() {

		try {
			attributeTypeComponent.getAttributeTypeDescription(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeDescriptionDefenseDescriptionEmpty() {

		try {
			attributeTypeComponent.getAttributeTypeDescription(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeDescriptionDefenseDescriptionNotFound() {

		try {
			attributeTypeComponent.getAttributeTypeDescription(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeKindDefenseRIdNull() {

		try {
			attributeTypeComponent.getAttributeTypeKind(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeKindDefenseRIdEmpty() {

		try {
			attributeTypeComponent.getAttributeTypeKind(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetAttributeTypeKindDefenseRIdNotFound() {

		try {
			attributeTypeComponent.getAttributeTypeKind(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveAttributeTypeDefenseRIdNull() {

		try {
			attributeTypeComponent.removeAttributeType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveAttributeTypeDefenseRIdEmpty() {

		try {
			attributeTypeComponent.removeAttributeType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			assertEquals(DGCErrorCodes.ATTRIBUTE_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveAttributeTypeDefenseRIdNotFound() {

		try {
			attributeTypeComponent.removeAttributeType(NON_EXISTANT);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			assertEquals(DGCErrorCodes.TERM_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

}
