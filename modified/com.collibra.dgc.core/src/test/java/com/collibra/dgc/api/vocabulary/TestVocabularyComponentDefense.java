package com.collibra.dgc.api.vocabulary;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.MeaningConstants;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.impl.ObjectTypeImpl;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestVocabularyComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";

	@Test
	public void testAddIncorporatedVocabularyDefenseRIdNull() {
		try {
			vocabularyComponent.addIncorporatedVocabulary(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddIncorporatedVocabularyDefenseRIdEmpty() {
		try {
			vocabularyComponent.addIncorporatedVocabulary(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddIncorporatedVocabularyDefenseVocabularyToIncorporateRIdNull() {
		try {
			Vocabulary voc = createVocabulary();
			vocabularyComponent.addIncorporatedVocabulary(voc.getId(), NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddIncorporatedVocabularyDefenseVocabularyToIncorporateRIdEmpty() {
		try {
			Vocabulary voc = createVocabulary();
			vocabularyComponent.addIncorporatedVocabulary(voc.getId(), EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabularyDefenseCommunityRIdNull() {
		try {
			vocabularyComponent.addVocabulary(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabularyDefenseCommunityRIdEmpty() {
		try {
			vocabularyComponent.addVocabulary(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabularyDefenseNameNull() {
		try {
			vocabularyComponent.addVocabulary(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabularyDefenseNameEmpty() {
		try {
			vocabularyComponent.addVocabulary(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabularyDefenseUriNull() {
		try {
			vocabularyComponent.addVocabulary(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_URI_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabularyDefenseUriEmpty() {
		try {
			vocabularyComponent.addVocabulary(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_URI_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabulary2DefenseCommunityRIdNull() {
		try {
			vocabularyComponent.addVocabulary(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabulary2DefenseCommunityRIdEmpty() {
		try {
			vocabularyComponent.addVocabulary(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabulary2DefenseNameNull() {
		try {
			vocabularyComponent.addVocabulary(NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabulary2DefenseNameEmpty() {
		try {
			vocabularyComponent.addVocabulary(NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabulary2DefenseUriNull() {
		try {
			vocabularyComponent.addVocabulary(NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_URI_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabulary2DefenseUriEmpty() {
		try {
			vocabularyComponent.addVocabulary(NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_URI_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabulary2DefenseTypeRIdNull() {
		try {
			vocabularyComponent.addVocabulary(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddVocabulary2DefenseTypeRIdEmpty() {
		try {
			vocabularyComponent.addVocabulary(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeCommunityDefenseRIdNull() {
		try {
			vocabularyComponent.changeCommunity(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeCommunityDefenseRIdEmpty() {
		try {
			vocabularyComponent.changeCommunity(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeCommunityDefenseNewCommunityRIdNull() {
		try {
			vocabularyComponent.changeCommunity(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void tesChangeCommunityDefenseNewCommunityRIdEmpty() {
		try {
			vocabularyComponent.changeCommunity(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.COMMUNITY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeNameDefenseRIdNull() {
		try {
			vocabularyComponent.changeName(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeNameDefenseRIdEmpty() {
		try {
			vocabularyComponent.changeName(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeNameDefenseNewNameNull() {
		try {
			vocabularyComponent.changeName(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeNameDefenseNewNameEmpty() {
		try {
			vocabularyComponent.changeName(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeTypeDefenseRIdNull() {
		try {
			vocabularyComponent.changeType(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeTypeDefenseRIdEmpty() {
		try {
			vocabularyComponent.changeType(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeTypeDefenseTypeRIdNull() {
		try {
			vocabularyComponent.changeType(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeTypeDefenseTypeRIdEmpty() {
		try {
			vocabularyComponent.changeType(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeUriDefenseRIdNull() {
		try {
			vocabularyComponent.changeUri(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeUriDefenseRIdEmpty() {
		try {
			vocabularyComponent.changeType(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeUriDefenseUriNull() {
		try {
			vocabularyComponent.changeUri(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_URI_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testChangeUriDefenseUriEmpty() {
		try {
			vocabularyComponent.changeUri(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_URI_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleVocabulariesToInCorporateDefenseRIdNull() {
		try {
			vocabularyComponent.findPossibleVocabulariesToInCorporate(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindPossibleVocabulariesToInCorporateDefenseRIdEmpty() {
		try {
			vocabularyComponent.findPossibleVocabulariesToInCorporate(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindVocabulariesContainingNameDefenseSearchNameNull() {
		try {
			vocabularyComponent.findVocabulariesContainingName(NULL, 0, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindVocabulariesContainingNameDefenseSearchNameEmpty() {
		try {
			vocabularyComponent.findVocabulariesContainingName(EMPTY, 0, 1);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetIncorporatedVocabulariesDefenseRIdNull() {
		try {
			vocabularyComponent.getIncorporatedVocabularies(NULL, true);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetIncorporatedVocabulariesDefenseRIdEmpty() {
		try {
			vocabularyComponent.getIncorporatedVocabularies(EMPTY, true);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetIncorporatingVocabularies2DefenseRIdNull() {
		try {
			vocabularyComponent.getIncorporatingVocabularies(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetIncorporatingVocabularies2DefenseRIdEmpty() {
		try {
			vocabularyComponent.getIncorporatingVocabularies(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredRepresentationDefenseRIdNull() {
		try {
			vocabularyComponent.getPreferredRepresentation(NULL, MeaningConstants.META_ACCEPTED_STATUS_TYPE_UUID);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredRepresentationDefenseRIdEmpty() {
		try {
			vocabularyComponent.getPreferredRepresentation(EMPTY, MeaningConstants.META_ACCEPTED_STATUS_TYPE_UUID);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredRepresentationDefenseConceptRIdNull() {
		try {
			vocabularyComponent.getPreferredRepresentation(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONCEPT_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredRepresentationDefenseConceptRIdEmpty() {
		try {
			vocabularyComponent.getPreferredRepresentation(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONCEPT_ID_EMPTY, ex.getErrorCode());
		}
	}

	protected ObjectType createObjectType(String voc) {
		return objectTypeDao.save(new ObjectTypeImpl());
	}

	@Test
	public void testGetPreferredRepresentationInIncorporatedVocabulariesDefenseRIdNull() {
		try {
			vocabularyComponent.getPreferredRepresentationInIncorporatedVocabularies(NULL,
					MeaningConstants.META_ACCEPTED_STATUS_TYPE_UUID);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredRepresentationInIncorporatedVocabulariesDefenseRIdEmpty() {
		try {
			vocabularyComponent.getPreferredRepresentationInIncorporatedVocabularies(EMPTY,
					MeaningConstants.META_ACCEPTED_STATUS_TYPE_UUID);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredRepresentationInIncorporatedVocabulariesDefenseConceptRIdNull() {
		try {
			vocabularyComponent.getPreferredRepresentationInIncorporatedVocabularies(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONCEPT_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredRepresentationInIncorporatedVocabulariesDefenseConceptRIdEmpty() {
		try {
			vocabularyComponent.getPreferredRepresentationInIncorporatedVocabularies(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CONCEPT_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredTermDefenseRIdNull() {
		try {
			vocabularyComponent.getPreferredTerm(NULL, MeaningConstants.META_ACCEPTED_STATUS_TYPE_UUID);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredTermDefenseRIdEmpty() {
		try {
			vocabularyComponent.getPreferredTerm(EMPTY, MeaningConstants.META_ACCEPTED_STATUS_TYPE_UUID);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredTermDefenseObjectTypeRIdNull() {
		try {
			vocabularyComponent.getPreferredTerm(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.OBJECT_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredTermDefenseObjectTypeRIdEmpty() {
		try {
			vocabularyComponent.getPreferredTerm(NULL, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.OBJECT_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredTermInIncorporatedVocabulariesDefenseRIdNull() {
		try {
			Community community = createCommunity();
			Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
					VOCABULARY_URI);
			Term A = termComponent.addTerm(voc.getId().toString(), "A");

			Vocabulary voc2 = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME + "2",
					VOCABULARY_URI + "2");

			vocabularyComponent.getPreferredTermInIncorporatedVocabularies(NULL, A.getMeaning().getId().toString());

			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredTermInIncorporatedVocabulariesDefenseRIdEmpty() {
		try {
			Community community = createCommunity();
			Vocabulary voc = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME,
					VOCABULARY_URI);
			Term A = termComponent.addTerm(voc.getId().toString(), "A");

			Vocabulary voc2 = vocabularyComponent.addVocabulary(community.getId().toString(), VOCABULARY_NAME + "2",
					VOCABULARY_URI + "2");
			vocabularyComponent.addIncorporatedVocabulary(voc2.getId().toString(), voc.getId().toString());

			vocabularyComponent.getPreferredTermInIncorporatedVocabularies(EMPTY, A.getMeaning().getId().toString());
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredTermInIncorporatedVocabulariesDefenseObjectTypeRIdNull() {
		try {
			vocabularyComponent.getPreferredTermInIncorporatedVocabularies(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.OBJECT_TYPE_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetPreferredTermInIncorporatedVocabulariesDefenseObjectTypeRIdEmpty() {
		try {
			vocabularyComponent.getPreferredTermInIncorporatedVocabularies(NULL, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.OBJECT_TYPE_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetVocabularyDefenseRIdNull() {
		try {
			vocabularyComponent.getVocabulary(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetVocabularyDefenseRIdEmpty() {
		try {
			vocabularyComponent.getVocabulary(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetVocabularyByNameDefenseNameNull() {
		try {
			vocabularyComponent.getVocabularyByName(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetVocabularyByNameDefenseNameEmpty() {
		try {
			vocabularyComponent.getVocabularyByName(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetVocabularyByUriDefenseUriNull() {
		try {
			vocabularyComponent.getVocabularyByUri(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_URI_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetVocabularyByUriDefenseUriEmpty() {
		try {
			vocabularyComponent.getVocabularyByUri(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_URI_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveIncorporatedVocabularyDefenseRIdNull() {
		try {
			vocabularyComponent.removeIncorporatedVocabulary(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveIncorporatedVocabularyDefenseRIdEmpty() {
		try {
			vocabularyComponent.removeIncorporatedVocabulary(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveIncorporatedVocabularyDefenseIncorporatedVocabularyRIdNull() {
		try {
			Vocabulary v = createVocabulary();
			vocabularyComponent.removeIncorporatedVocabulary(v.getId(), NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveIncorporatedVocabularyDefenseIncorporatedVocabularyRIdEmpty() {
		try {
			Vocabulary v = createVocabulary();
			vocabularyComponent.removeIncorporatedVocabulary(v.getId(), EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveVocabularyDefenseRIdNull() {
		try {
			vocabularyComponent.removeVocabulary(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveVocabularyDefenseRIdEmpty() {
		try {
			vocabularyComponent.removeVocabulary(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}
}
