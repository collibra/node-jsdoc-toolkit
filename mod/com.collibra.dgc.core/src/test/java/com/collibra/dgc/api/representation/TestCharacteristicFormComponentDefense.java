package com.collibra.dgc.api.representation;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCharacteristicFormComponentDefense extends AbstractDGCBootstrappedApiTest {

	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";
	private static final Collection<String> NULL_COLLECTION = null;
	private static final Collection<String> EMPTY_COLLECTION = new ArrayList<String>();

	@Test
	public void testAddCharacteristicFormDefenseVocabularyRIdNull() {
		try {
			characteristicFormComponent.addCharacteristicForm(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCharacteristicFormDefenseVocabularyRIdEmpty() {
		try {
			characteristicFormComponent.addCharacteristicForm(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCharacteristicFormDefenseTermSignifierNull() {
		try {
			characteristicFormComponent.addCharacteristicForm(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_TERM_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCharacteristicFormDefenseTermSignifierEmpty() {
		try {
			characteristicFormComponent.addCharacteristicForm(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_TERM_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCharacteristicFormDefenseRoleNull() {
		try {
			characteristicFormComponent.addCharacteristicForm(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_ROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCharacteristicFormDefenseRoleEmpty() {
		try {
			characteristicFormComponent.addCharacteristicForm(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_ROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCharacteristicFormDefenseRIdNull() {
		try {
			characteristicFormComponent.getCharacteristicForm(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCharacteristicFormDefenseRIdEmpty() {
		try {
			characteristicFormComponent.getCharacteristicForm(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCharacteristicFormsContainingTermDefenseTermRIdNull() {
		try {
			characteristicFormComponent.getCharacteristicFormsContainingTerm(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCharacteristicFormsContainingTermDefenseTermRIdEmpty() {
		try {
			characteristicFormComponent.getCharacteristicFormsContainingTerm(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeCharacteristicFormDefenseRIdNull() {
		try {
			characteristicFormComponent.changeCharacteristicForm(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeCharacteristicFormDefenseRIdEmpty() {
		try {
			characteristicFormComponent.changeCharacteristicForm(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeCharacteristicFormDefenseNewtermSignifierNull() {
		try {
			characteristicFormComponent.changeCharacteristicForm(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_TERM_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeCharacteristicFormDefenseNewtermSignifierEmpty() {
		try {
			characteristicFormComponent.changeCharacteristicForm(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_TERM_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeCharacteristicFormDefenseNewtermNewRoleNull() {
		try {
			characteristicFormComponent.changeCharacteristicForm(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_ROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeCharacteristicFormDefenseNewtermNewRoleEmpty() {
		try {
			characteristicFormComponent.changeCharacteristicForm(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CF_ROLE_EMPTY, ex.getErrorCode());
		}
	}
}
