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
public class TestTermComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";
	private static final Collection<String> NULL_COLLECTION = null;
	private static final Collection<String> EMPTY_COLLECTION = new ArrayList<String>();

	@Test
	public void testAddTermDefenseVocabularyRIdNull() {
		try {
			termComponent.addTerm(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddTermDefenseVocabularyRIdEmpty() {
		try {
			termComponent.addTerm(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddTermDefenseSignifierNull() {
		try {
			termComponent.addTerm(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddTermDefenseSignifierEmpty() {
		try {
			termComponent.addTerm(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddTerm2DefenseVocabularyRIdNull() {
		try {
			termComponent.addTerm(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddTerm2DefenseVocabularyRIdEmpty() {
		try {
			termComponent.addTerm(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddTerm2DefenseSignifierNull() {
		try {
			termComponent.addTerm(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddTerm2DefenseSignifierEmpty() {
		try {
			termComponent.addTerm(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddTerm2DefenseObjectTypeRIdNull() {
		try {
			termComponent.addTerm(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.OBJECT_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddTerm2DefenseObjectTypeRIdEmpty() {
		try {
			termComponent.addTerm(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.OBJECT_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSignifierDefenseRIdNull() {
		try {
			termComponent.changeSignifier(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSignifierDefenseRIdEmpty() {
		try {
			termComponent.changeSignifier(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSignifierDefenseSignifierNull() {
		try {
			termComponent.changeSignifier(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeSignifierDefenseSignifierEmpty() {
		try {
			termComponent.changeSignifier(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindTermsContainingSignifierDefenseSearchSignifierNull() {
		try {
			termComponent.findTermsContainingSignifier(NULL, 1, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindTermsContainingSignifierDefenseSearchSignifierEmpty() {
		try {
			termComponent.findTermsContainingSignifier(EMPTY, 1, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetTermDefenseRIdNull() {
		try {
			termComponent.getTerm(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetTermDefenseRIdEmpty() {
		try {
			termComponent.getTerm(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetTermBySignifierDefenseVocabularyRIdNull() {
		try {
			termComponent.getTermBySignifier(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetTermBySignifierDefenseVocabularyRIdEmpty() {
		try {
			termComponent.getTermBySignifier(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetTermBySignifierDefenseSignifierNull() {
		try {
			termComponent.getTermBySignifier(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetTermBySignifierDefenseSignifierEmpty() {
		try {
			termComponent.getTermBySignifier(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetTermBySignifierInIncorporatedVocabulariesDefenseVocabularyRIdNull() {
		try {
			termComponent.getTermBySignifierInIncorporatedVocabularies(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetTermBySignifierInIncorporatedVocabulariesDefenseVocabularyRIdEmpty() {
		try {
			termComponent.getTermBySignifierInIncorporatedVocabularies(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetTermBySignifierInIncorporatedVocabulariesDefenseSignifierNull() {
		try {
			termComponent.getTermBySignifierInIncorporatedVocabularies(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetTermBySignifierInIncorporatedVocabulariesDefenseSignifierEmpty() {
		try {
			termComponent.getTermBySignifierInIncorporatedVocabularies(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveTermsDefenseTermIdsNull() {
		try {
			termComponent.removeTerms(NULL_COLLECTION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveTermsDefenseTermIdsEmpty() {
		try {
			termComponent.removeTerms(EMPTY_COLLECTION);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

}
