package com.collibra.dgc.api.categorization;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCategorizationComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";

	@Test
	public void testAddCategorizationTypeDefenseVocabularyRIdNull() {
		try {
			categorizationComponent.addCategorizationType(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategorizationTypeDefenseVocabularyRIdEmpty() {
		try {
			categorizationComponent.addCategorizationType(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategorizationTypeDefenseSignifierNull() {
		try {
			categorizationComponent.addCategorizationType(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategorizationTypeDefenseSignifierEmpty() {
		try {
			categorizationComponent.addCategorizationType(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategorizationTypeDefenseIsForConceptTermRIdNull() {
		try {
			categorizationComponent.addCategorizationType(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategorizationTypeDefenseIsForConceptTermRIdEmpty() {
		try {
			categorizationComponent.addCategorizationType(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategorizedConceptToCategorizationSchemeDefenseCategorizationSchemeConceptRIdNull() {
		try {
			categorizationComponent.addCategorizedConceptToCategorizationScheme(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategorizedConceptToCategorizationSchemeDefenseCategorizationSchemeConceptRIdEmpty() {
		try {
			categorizationComponent.addCategorizedConceptToCategorizationScheme(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategorizedConceptToCategorizationSchemeDefenseCategorizedConceptRIdNull() {
		try {
			categorizationComponent.addCategorizedConceptToCategorizationScheme(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategorizedConceptToCategorizationSchemeDefenseCategorizedConceptRIdEmpty() {
		try {
			categorizationComponent.addCategorizedConceptToCategorizationScheme(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryDefenseVocabularyRIdNull() {
		try {
			categorizationComponent.addCategory(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryDefenseVocabularyRIdEmpty() {
		try {
			categorizationComponent.addCategory(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryDefenseSignifierNull() {
		try {
			categorizationComponent.addCategory(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryDefenseSignifierEmpty() {
		try {
			categorizationComponent.addCategory(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryDefenseCategorizationTypeTermRIdNull() {
		try {
			categorizationComponent.addCategory(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_TERM_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryDefenseCategorizationTypeTermRIdEmpty() {
		try {
			categorizationComponent.addCategory(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_TERM_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategory2DefenseSignifierNull() {
		try {
			categorizationComponent.addCategory(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategory2DefenseSignifierEmpty() {
		try {
			categorizationComponent.addCategory(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategory2DefenseCategorizationSchemeRIdNull() {
		try {
			categorizationComponent.addCategory(NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategory2DefenseCategorizationSchemeRIdEmpty() {
		try {
			categorizationComponent.addCategory(NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategory2DefenseCategorizationTypeTermRIdNull() {
		try {
			categorizationComponent.addCategory(NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_TERM_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategory2DefenseCategorizationTypeTermRIdEmpty() {
		try {
			categorizationComponent.addCategory(NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_TERM_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategory2DefenseVocabularyRIdNull() {
		try {
			categorizationComponent.addCategory(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategory2DefenseVocabularyRIdEmpty() {
		try {
			categorizationComponent.addCategory(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryForConceptDefenseSignifierNull() {
		try {
			categorizationComponent.addCategoryForConcept(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORY_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryForConceptDefenseSignifierEmpty() {
		try {
			categorizationComponent.addCategoryForConcept(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORY_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryForConceptDefenseForConceptRIdNull() {
		try {
			categorizationComponent.addCategoryForConcept(NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryForConceptDefenseForConceptRIdEmpty() {
		try {
			categorizationComponent.addCategoryForConcept(NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryForConceptDefenseCategorizationTypeTermRIdNull() {
		try {
			categorizationComponent.addCategoryForConcept(NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_TERM_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryForConceptDefenseCategorizationTypeTermRIdEmpty() {
		try {
			categorizationComponent.addCategoryForConcept(NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_TERM_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryForConceptDefenseVocabularyRIdNull() {
		try {
			categorizationComponent.addCategoryForConcept(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryForConceptDefenseVocabularyRIdEmpty() {
		try {
			categorizationComponent.addCategoryForConcept(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryToCategorizationSchemeDefenseCategorizationSchemeConceptRIdNull() {
		try {
			categorizationComponent.addCategoryToCategorizationScheme(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryToCategorizationSchemeDefenseCategorizationSchemeConceptRIdEmpty() {
		try {
			categorizationComponent.addCategoryToCategorizationScheme(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryToCategorizationSchemeDefenseCategoryRIdNull() {
		try {
			categorizationComponent.addCategoryToCategorizationScheme(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddCategoryToCategorizationSchemeDefenseCategoryRIdEmpty() {
		try {
			categorizationComponent.addCategoryToCategorizationScheme(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testDemoteCategorizationTypeToObjectTypeDefenseCategorizationTypeRIdNull() {
		try {
			categorizationComponent.demoteCategorizationTypeToObjectType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testDemoteCategorizationTypeToObjectTypeDefenseCategorizationTypeRIdEmpty() {
		try {
			categorizationComponent.demoteCategorizationTypeToObjectType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testDemoteCategoryToObjectTypeDefenseCategoryRIdNull() {
		try {
			categorizationComponent.demoteCategoryToObjectType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testDemoteCategoryToObjectTypeDefenseCategoryRIdEmpty() {
		try {
			categorizationComponent.demoteCategoryToObjectType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void tesGetCategoriesDefenseCategorizationTypeRIdNull() {
		try {
			categorizationComponent.getCategories(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void tesGetCategoriesDefenseCategorizationTypeRIdEmpty() {
		try {
			categorizationComponent.getCategories(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCategorizationTypeDefenseRIdNull() {
		try {
			categorizationComponent.getCategorizationType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCategorizationTypeDefenseRIdEmpty() {
		try {
			categorizationComponent.getCategorizationType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCategorizationTypesDefenseConceptRIdNull() {
		try {
			categorizationComponent.getCategorizationTypes(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCategorizationTypesDefenseConceptRIdEmpty() {
		try {
			categorizationComponent.getCategorizationTypes(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCategorizationTypeTermDefenseVocabularyRIdNull() {
		try {
			categorizationComponent.getCategorizationTypeTerm(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCategorizationTypeTermDefenseVocabularyRIdEmpty() {
		try {
			categorizationComponent.getCategorizationTypeTerm(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCategorizationTypeTermDefenseCategorizationTypeRIdNull() {
		try {
			categorizationComponent.getCategorizationTypeTerm(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCategorizationTypeTermDefenseCategorizationTypeRIdEmpty() {
		try {
			categorizationComponent.getCategorizationTypeTerm(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCategorizationTypeTermsForConceptDefenseConceptRIdNull() {
		try {
			categorizationComponent.getCategorizationTypeTermsForConcept(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetCategorizationTypeTermsForConceptDefenseConceptRIdEmpty() {
		try {
			categorizationComponent.getCategorizationTypeTermsForConcept(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRootCategorizedConceptsForCategorizationSchemeDefenseCategorizationSchemeRIdNull() {
		try {
			categorizationComponent.getRootCategorizedConceptsForCategorizationScheme(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_SCHEME_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRootCategorizedConceptsForCategorizationSchemeDefenseCategorizationSchemeRIdEmpty() {
		try {
			categorizationComponent.getRootCategorizedConceptsForCategorizationScheme(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_SCHEME_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRootCategorizedConceptsForCategorizationSchemeDefenseVocabularyRIdNull() {
		try {
			categorizationComponent.getRootCategorizedConceptsForCategorizationScheme(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRootCategorizedConceptsForCategorizationSchemeDefenseVocabularyRIdEmpty() {
		try {
			categorizationComponent.getRootCategorizedConceptsForCategorizationScheme(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetSchemesForCategoryDefenseCategoryRIdNull() {
		try {
			categorizationComponent.getSchemesForCategory(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetSchemesForCategoryDefenseCategoryRIdEmpty() {
		try {
			categorizationComponent.getSchemesForCategory(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetSchemesForConceptDefenseCategorizedConceptRIdNull() {
		try {
			categorizationComponent.getSchemesForConcept(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetSchemesForConceptDefenseCategorizedConceptRIdEmpty() {
		try {
			categorizationComponent.getSchemesForConcept(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testIsCategorizationTypeDefenseObjectTypeRIdNull() {
		try {
			categorizationComponent.isCategorizationType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testIsCategorizationTypeDefenseObjectTypeRIdEmpty() {
		try {
			categorizationComponent.isCategorizationType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testIsCategoryDefenseObjectTypeRIdNull() {
		try {
			categorizationComponent.isCategory(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testIsCategoryDefenseObjectTypeRIdEmpty() {
		try {
			categorizationComponent.isCategory(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategorizationTypeDefenseObjectTypeRIdNull() {
		try {
			categorizationComponent.promoteObjectTypeToCategorizationType(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategorizationTypeDefenseObjectTypeRIdEmpty() {
		try {
			categorizationComponent.promoteObjectTypeToCategorizationType(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategorizationTypeDefenseForConceptRIdNull() {
		try {
			categorizationComponent.promoteObjectTypeToCategorizationType(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategorizationTypeDefenseForConceptRIdEmpty() {
		try {
			categorizationComponent.promoteObjectTypeToCategorizationType(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryDefenseObjectTypeRIdNull() {
		try {
			categorizationComponent.promoteObjectTypeToCategory(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryDefenseObjectTypeRIdEmpty() {
		try {
			categorizationComponent.promoteObjectTypeToCategory(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryDefenseCategorizationTypeRIdNull() {
		try {
			categorizationComponent.promoteObjectTypeToCategory(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryDefenseCategorizationTypeRIdEmpty() {
		try {
			categorizationComponent.promoteObjectTypeToCategory(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategory2DefenseObjectTypeRIdNull() {
		try {
			categorizationComponent.promoteObjectTypeToCategory(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategory2DefenseObjectTypeRIdEmpty() {
		try {
			categorizationComponent.promoteObjectTypeToCategory(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategory2DefenseCategorizationSchemeRIdNull() {
		try {
			categorizationComponent.promoteObjectTypeToCategory(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategory2DefenseCategorizationSchemeRIdEmpty() {
		try {
			categorizationComponent.promoteObjectTypeToCategory(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategory2DefenseCategorizationTypeRIdNull() {
		try {
			categorizationComponent.promoteObjectTypeToCategory(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategory2DefenseCategorizationTypeRIdEmpty() {
		try {
			categorizationComponent.promoteObjectTypeToCategory(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryForConceptDefenseObjectTypeRIdNull() {
		try {
			categorizationComponent.promoteObjectTypeToCategoryForConcept(NULL, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryForConceptDefenseObjectTypeRIdEmpty() {
		try {
			categorizationComponent.promoteObjectTypeToCategoryForConcept(EMPTY, NOT_EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryForConceptDefenseForConceptRIdNull() {
		try {
			categorizationComponent.promoteObjectTypeToCategoryForConcept(NOT_EMPTY, NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryForConceptDefenseForConceptRIdEmpty() {
		try {
			categorizationComponent.promoteObjectTypeToCategoryForConcept(NOT_EMPTY, EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryForConceptDefenseCategorizationTypeRIdNull() {
		try {
			categorizationComponent.promoteObjectTypeToCategoryForConcept(NOT_EMPTY, NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryForConceptDefenseCategorizationTypeRIdEmpty() {
		try {
			categorizationComponent.promoteObjectTypeToCategoryForConcept(NOT_EMPTY, NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategorizationTypeDefenseCategorizationTypeRIdNull() {
		try {
			categorizationComponent.removeCategorizationType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategorizationTypeDefenseCategorizationTypeRIdEmpty() {
		try {
			categorizationComponent.removeCategorizationType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategorizedConceptFromCategorizationSchemeDefenseCategorizationSchemeConceptRIdNull() {
		try {
			categorizationComponent.removeCategorizedConceptFromCategorizationScheme(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategorizedConceptFromCategorizationSchemeDefenseCategorizationSchemeConceptRIdEmpty() {
		try {
			categorizationComponent.removeCategorizedConceptFromCategorizationScheme(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategorizedConceptFromCategorizationSchemeDefenseCategorizedConceptRIdNull() {
		try {
			categorizationComponent.removeCategorizedConceptFromCategorizationScheme(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategorizedConceptFromCategorizationSchemeDefenseCategorizedConceptRIdEmpty() {
		try {
			categorizationComponent.removeCategorizedConceptFromCategorizationScheme(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategoryDefenseCategoryRIdNull() {
		try {
			categorizationComponent.removeCategory(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategoryDefenseCategoryRIdEmpty() {
		try {
			categorizationComponent.removeCategory(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategoryFromCategorizationSchemeDefenseCategorizationSchemeConceptRIdNull() {
		try {
			categorizationComponent.removeCategoryFromCategorizationScheme(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategoryFromCategorizationSchemeDefenseCategorizationSchemeConceptRIdEmpty() {
		try {
			categorizationComponent.removeCategoryFromCategorizationScheme(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategoryFromCategorizationSchemeDefenseCategoryRIdNull() {
		try {
			categorizationComponent.removeCategoryFromCategorizationScheme(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveCategoryFromCategorizationSchemeDefenseCategoryRIdEmpty() {
		try {
			categorizationComponent.removeCategoryFromCategorizationScheme(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}
}
