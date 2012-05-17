package com.collibra.dgc.api.representation;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestBinaryFactTypeFormComponentDefense extends AbstractDGCBootstrappedApiTest {

	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";
	private static final Collection<String> NULL_COLLECTION = null;
	private static final Collection<String> EMPTY_COLLECTION = new ArrayList<String>();

	@Test
	public void testAddBinaryFactTypeFormDefenseVocabularyRIdNull() {
		try {
			binaryFactTypeFormComponent.addBinaryFactTypeForm(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormDefenseVocabularyRIdEmpty() {
		try {
			binaryFactTypeFormComponent.addBinaryFactTypeForm(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormDefenseHeadSignifierNull() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.addBinaryFactTypeForm(v.getId(), NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormDefenseHeadSignifierEmpty() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent
					.addBinaryFactTypeForm(v.getId(), EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormDefenseRoleNull() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.addBinaryFactTypeForm(v.getId(), NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormDefenseRoleEmpty() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent
					.addBinaryFactTypeForm(v.getId(), NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormDefenseCoRoleNull() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.addBinaryFactTypeForm(v.getId(), NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormDefenseCoRoleEmpty() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent
					.addBinaryFactTypeForm(v.getId(), NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormDefenseTailSignifierNull() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.addBinaryFactTypeForm(v.getId(), NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormDefenseTailSignifierEmpty() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent
					.addBinaryFactTypeForm(v.getId(), NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTermsDefenseVocabularyRIdNull() {
		try {
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTerms(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY,
					NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTermsDefenseVocabularyRIdEmpty() {
		try {
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTerms(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY,
					NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTermsDefenseHeadSignifierNull() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTerms(v.getId(), NULL, NOT_EMPTY,
					NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTermsDefenseHeadSignifierEmpty() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTerms(v.getId(), EMPTY, NOT_EMPTY,
					NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTermsDefenseRoleNull() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTerms(v.getId(), head.getId(),
					NULL, NOT_EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTermsDefenseRoleEmpty() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTerms(v.getId(), head.getId(),
					EMPTY, NOT_EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTermsDefenseCoRoleNull() {
		try {

			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");

			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTerms(v.getId(), head.getId(),
					NOT_EMPTY, NULL, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTermsDefenseCoRoleEmpty() {
		try {

			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");

			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTerms(v.getId(), head.getId(),
					NOT_EMPTY, EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTermsDefenseTailSignifierNull() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTerms(v.getId(), head.getId(),
					NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTermsDefenseTailSignifierEmpty() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTerms(v.getId(), head.getId(),
					NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingHeadTermDefenseVocabularyRIdNull() {
		try {
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingHeadTerm(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY,
					NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingHeadTermDefenseVocabularyRIdEmpty() {
		try {
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingHeadTerm(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY,
					NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingHeadTermDefenseHeadSignifierNull() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingHeadTerm(v.getId(), NULL, NOT_EMPTY,
					NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingHeadTermDefenseHeadSignifierEmpty() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingHeadTerm(v.getId(), EMPTY, NOT_EMPTY,
					NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingHeadTermDefenseRoleNull() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingHeadTerm(v.getId(),
					head.getId(), NULL, NOT_EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingHeadTermDefenseRoleEmpty() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingHeadTerm(v.getId(),
					head.getId(), EMPTY, NOT_EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingHeadTermDefenseCoRoleNull() {
		try {

			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");

			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingHeadTerm(v.getId(),
					head.getId(), NOT_EMPTY, NULL, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingHeadTermDefenseCoRoleEmpty() {
		try {

			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");

			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingHeadTerm(v.getId(),
					head.getId(), NOT_EMPTY, EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingHeadTermDefenseTailSignifierNull() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingHeadTerm(v.getId(),
					head.getId(), NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingHeadTermDefenseTailSignifierEmpty() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingHeadTerm(v.getId(),
					head.getId(), NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTailTermDefenseVocabularyRIdNull() {
		try {
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTailTerm(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY,
					NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTailTermDefenseVocabularyRIdEmpty() {
		try {
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTailTerm(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY,
					NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTailTermDefenseHeadSignifierNull() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTailTerm(v.getId(), NULL, NOT_EMPTY,
					NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTailTermDefenseHeadSignifierEmpty() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTailTerm(v.getId(), EMPTY, NOT_EMPTY,
					NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTailTermDefenseRoleNull() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTailTerm(v.getId(),
					head.getId(), NULL, NOT_EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTailTermDefenseRoleEmpty() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTailTerm(v.getId(),
					head.getId(), EMPTY, NOT_EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTailTermDefenseCoRoleNull() {
		try {

			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");

			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTailTerm(v.getId(),
					head.getId(), NOT_EMPTY, NULL, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTailTermDefenseCoRoleEmpty() {
		try {

			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");

			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTailTerm(v.getId(),
					head.getId(), NOT_EMPTY, EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTailTermDefenseTailSignifierNull() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTailTerm(v.getId(),
					head.getId(), NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddBinaryFactTypeFormOnExistingTailTermDefenseTailSignifierEmpty() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			binaryFactTypeFormComponent.addBinaryFactTypeFormOnExistingTailTerm(v.getId(),
					head.getId(), NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBinaryFactTypeFormDefenseRIdNull() {
		try {

			binaryFactTypeFormComponent.getBinaryFactTypeForm(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBinaryFactTypeFormDefenseRIdEmpty() {
		try {

			binaryFactTypeFormComponent.getBinaryFactTypeForm(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBinaryFactTypeFormsContainingHeadTermDefenseHeadTermRIdNull() {
		try {

			binaryFactTypeFormComponent.getBinaryFactTypeFormsContainingHeadTerm(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBinaryFactTypeFormsContainingHeadTermDefenseHeadTermRIdEmpty() {
		try {

			binaryFactTypeFormComponent.getBinaryFactTypeFormsContainingHeadTerm(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBinaryFactTypeFormsContainingTailTermDefenseTailTermRIdNull() {
		try {

			binaryFactTypeFormComponent.getBinaryFactTypeFormsContainingTailTerm(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBinaryFactTypeFormsContainingTailTermDefenseTailTermRIdEmpty() {
		try {

			binaryFactTypeFormComponent.getBinaryFactTypeFormsContainingTailTerm(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBinaryFactTypeFormsContainingTermDefenseTermRIdNull() {
		try {

			binaryFactTypeFormComponent.getBinaryFactTypeFormsContainingTerm(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetBinaryFactTypeFormsContainingTermDefenseTermRIdEmpty() {
		try {

			binaryFactTypeFormComponent.getBinaryFactTypeFormsContainingTerm(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetDerivedFactsDefenseTermRIdNull() {
		try {

			binaryFactTypeFormComponent.getDerivedFacts(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetDerivedFactsDefenseTermRIdEmpty() {
		try {

			binaryFactTypeFormComponent.getDerivedFacts(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeBinaryFactTypeFormDefenseVocabularyRIdNull() {
		try {
			binaryFactTypeFormComponent.changeBinaryFactTypeForm(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeBinaryFactTypeFormDefenseVocabularyRIdEmpty() {
		try {
			binaryFactTypeFormComponent.changeBinaryFactTypeForm(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeBinaryFactTypeFormDefenseHeadSignifierNull() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.changeBinaryFactTypeForm(v.getId(), NULL, NOT_EMPTY, NOT_EMPTY,
					NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeBinaryFactTypeFormDefenseHeadSignifierEmpty() {
		try {
			Vocabulary v = createVocabulary();
			binaryFactTypeFormComponent.changeBinaryFactTypeForm(v.getId(), EMPTY, NOT_EMPTY, NOT_EMPTY,
					NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_HEAD_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeBinaryFactTypeFormDefenseRoleNull() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");
			binaryFactTypeFormComponent.changeBinaryFactTypeForm(v.getId(), head.getId(), NULL,
					NOT_EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeBinaryFactTypeFormDefenseRoleEmpty() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");
			binaryFactTypeFormComponent.changeBinaryFactTypeForm(v.getId(), head.getId(), EMPTY,
					NOT_EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeBinaryFactTypeFormDefenseCoRoleNull() {
		try {

			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");

			binaryFactTypeFormComponent.changeBinaryFactTypeForm(v.getId(), head.getId(), NOT_EMPTY,
					NULL, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeBinaryFactTypeFormDefenseCoRoleEmpty() {
		try {

			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			Term tail = termComponent.addTerm(v.getId().toString(), "Tail");

			binaryFactTypeFormComponent.changeBinaryFactTypeForm(v.getId(), head.getId(), NOT_EMPTY,
					EMPTY, tail.getId());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_COROLE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeBinaryFactTypeFormDefenseTailSignifierNull() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			binaryFactTypeFormComponent.changeBinaryFactTypeForm(v.getId(), head.getId(), NOT_EMPTY,
					NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_SIGNIFIER_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeBinaryFactTypeFormDefenseTailSignifierEmpty() {
		try {
			Vocabulary v = createVocabulary();
			Term head = termComponent.addTerm(v.getId().toString(), "Head");
			binaryFactTypeFormComponent.changeBinaryFactTypeForm(v.getId(), head.getId(), NOT_EMPTY,
					NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_TAIL_SIGNIFIER_EMPTY, ex.getErrorCode());
		}
	}
}
