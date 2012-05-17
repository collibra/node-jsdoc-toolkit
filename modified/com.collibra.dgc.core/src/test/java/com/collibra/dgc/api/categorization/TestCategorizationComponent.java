package com.collibra.dgc.api.categorization;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.CategorizationComponent;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * {@link CategorizationComponent} API tests.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCategorizationComponent extends AbstractDGCBootstrappedApiTest {
	private Term scheme;
	private Vocabulary voc;
	private Term category;
	private Term catTypeTerm;
	private Term concept;

	@Before
	public void initialize() {
		voc = createVocabulary();
		concept = termComponent.addTerm(voc.getId(), "Concept");
		catTypeTerm = categorizationComponent.addCategorizationType(voc.getId(), "CatType", concept.getId().toString());
		scheme = termComponent.addTerm(voc.getId(), "Scheme");
		category = categorizationComponent.addCategory(voc.getId(), "Category", catTypeTerm.getId().toString());
	}

	@Test
	public void testAddCategorizationType() {
		Assert.assertNotNull(catTypeTerm);
		Assert.assertTrue(catTypeTerm.getObjectType() instanceof CategorizationType);
	}

	@Test
	public void testAddCategory() {
		Assert.assertNotNull(category);
		Assert.assertTrue(category.getObjectType() instanceof Category);

		categorizationComponent.addCategoryToCategorizationScheme(scheme.getId(), category.getId());
		category = categorizationComponent.addCategory("Category2", scheme.getId(), catTypeTerm.getId(), voc.getId());
		Assert.assertNotNull(category);
		Assert.assertTrue(category.getObjectType() instanceof Category);
	}

	@Test
	public void testAddCategoryForConcept() {
		Term child = termComponent.addTerm(voc.getId(), "Child");
		representationComponent.changeGeneralConcept(child.getId(), concept.getId());

		Term category = categorizationComponent.addCategoryForConcept("Category2", child.getId(), catTypeTerm.getId(),
				voc.getId());
		Assert.assertNotNull(category);
		Assert.assertTrue(category.getObjectType() instanceof Category);
	}

	@Test
	public void testGetCategorizationTypeTerm() {
		Term result = categorizationComponent.getCategorizationTypeTerm(voc.getId(), catTypeTerm.getObjectType()
				.getId());
		Assert.assertNotNull(result);
		Assert.assertEquals(catTypeTerm.getId(), result.getId());
	}

	@Test
	public void testGetCategorizationTypeTermsForConcept() {
		Collection<Term> terms = categorizationComponent.getCategorizationTypeTermsForConcept(concept.getId());
		Assert.assertEquals(1, terms.size());
		Assert.assertEquals(catTypeTerm.getId(), terms.iterator().next().getId());
	}

	@Test
	public void testGetCategoryTerms() {
		Collection<Term> terms = categorizationComponent.getCategoryTerms(catTypeTerm.getId(), voc.getId());
		Assert.assertEquals(1, terms.size());
		Assert.assertEquals(category.getId(), terms.iterator().next().getId());

		categorizationComponent.addCategory(voc.getId(), "Category2", catTypeTerm.getId());
		terms = categorizationComponent.getCategoryTerms(catTypeTerm.getId(), voc.getId().toString());
		Assert.assertEquals(2, terms.size());
	}

	@Test
	public void testGetCategoryTermsForConcept() {
		List<Term> terms = categorizationComponent.getCategoryTermsForConcept(concept.getId().toString());
		Assert.assertEquals(1, terms.size());
		Assert.assertEquals(category.getId(), terms.iterator().next().getId());

		categorizationComponent.addCategory(voc.getId(), "Category2", catTypeTerm.getId());
		terms = categorizationComponent.getCategoryTermsForConcept(concept.getId());
		Assert.assertEquals(2, terms.size());
	}

	@Test
	public void testGetCategorizationTypeTermToCategoriesTermMapForConcept() {
		Map<Term, List<Term>> result = categorizationComponent
				.getCategorizationTypeTermToCategoriesTermMapForConcept(concept.getId());
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(catTypeTerm.getId(), result.keySet().iterator().next().getId());
		Assert.assertEquals(category.getId(), result.values().iterator().next().iterator().next().getId());
	}

	@Test
	public void testGetCategorizationType() {
		CategorizationType type = categorizationComponent.getCategorizationType(catTypeTerm.getObjectType().getId()
				.toString());
		Assert.assertNotNull(type);
		Assert.assertEquals(catTypeTerm.getObjectType().getId(), type.getId());
	}

	@Test
	public void testGetCategoryTerm() {
		Term result = categorizationComponent.getCategoryTerm(voc.getId(), category.getObjectType().getId().toString());
		Assert.assertEquals(category.getId(), result.getId());
	}

	@Test
	public void testGetCategory() {
		ObjectType result = categorizationComponent.getCategory(category.getObjectType().getId());
		Assert.assertNotNull(result);
	}

	@Test
	public void testIsCategory() {
		Assert.assertTrue(categorizationComponent.isCategory(category.getId()));

		Term dummy = termComponent.addTerm(voc.getId(), "Dummy");
		Assert.assertFalse(categorizationComponent.isCategory(dummy.getId()));
	}

	@Test
	public void testIsCategorizationType() {
		Assert.assertTrue(categorizationComponent.isCategorizationType(catTypeTerm.getId()));
		Term dummy = termComponent.addTerm(voc.getId(), "Dummy");
		Assert.assertFalse(categorizationComponent.isCategorizationType(dummy.getId()));

	}

	@Test
	public void testAddCategoryToCategorizationScheme() {
		Term result = categorizationComponent.addCategoryToCategorizationScheme(scheme.getId(), category.getId());
		Assert.assertNotNull(result);
		Assert.assertEquals(scheme.getId(), result.getId());
		Assert.assertEquals(scheme.getObjectType().getId(), result.getObjectType().getId());
		Collection<Term> schemes = categorizationComponent.getSchemesForCategory(category.getId());
		Assert.assertEquals(1, schemes.size());
		Assert.assertEquals(result.getId(), schemes.iterator().next().getId());
	}

	@Test
	public void testRemoveCategory() {
		categorizationComponent.removeCategory(category.getId());

		try {
			termComponent.getTerm(category.getId());
			Assert.fail();
		} catch (EntityNotFoundException e) {
			// This is correct.
		}
	}

	@Test
	public void testRemoveCategorizationType() {
		categorizationComponent.removeCategorizationType(catTypeTerm.getId().toString());

		try {
			termComponent.getTerm(catTypeTerm.getId());
			Assert.fail();
		} catch (EntityNotFoundException e) {
			// This is correct.
		}
	}

	@Test
	public void testRemoveCategoryFromCategorizationScheme() {
		Term result = categorizationComponent.addCategoryToCategorizationScheme(scheme.getId(), category.getId());
		Collection<Term> schemes = categorizationComponent.getSchemesForCategory(category.getId().toString());
		Assert.assertEquals(1, schemes.size());
		Assert.assertEquals(result.getId(), schemes.iterator().next().getId());

		result = categorizationComponent.removeCategoryFromCategorizationScheme(result.getId(), category.getId());

		schemes = categorizationComponent.getSchemesForCategory(category.getId());
		Assert.assertEquals(0, schemes.size());
	}

	@Test
	public void testRemoveCategorizedConceptFromCategorizationScheme() {
		Term result = categorizationComponent.addCategoryToCategorizationScheme(scheme.getId(), category.getId());

		Collection<Term> schemes = categorizationComponent.getSchemesForConcept(concept.getId());
		Assert.assertEquals(1, schemes.size());
		Assert.assertEquals(result.getId(), schemes.iterator().next().getId());

		result = categorizationComponent.removeCategorizedConceptFromCategorizationScheme(result.getId(),
				concept.getId());
		schemes = categorizationComponent.getSchemesForConcept(concept.getId());
		Assert.assertEquals(0, schemes.size());
	}

	@Test
	public void testPromoteObjectTypeToCategory() {
		Term category2 = termComponent.addTerm(voc.getId(), "Category2");
		categorizationComponent.promoteObjectTypeToCategory(category2.getId(), catTypeTerm.getId());

		try {

			categorizationComponent.getCategory(category2.getId());
			Assert.fail();

		} catch (EntityNotFoundException e) {

			Assert.assertEquals(DGCErrorCodes.CATEGORY_NOT_FOUND, e.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryForConcept() {
		Term category2 = termComponent.addTerm(voc.getId(), "Category2");
		categorizationComponent.promoteObjectTypeToCategoryForConcept(category2.getId(), concept.getId(),
				catTypeTerm.getId());

		try {

			categorizationComponent.getCategory(category2.getObjectType().getId());
			Assert.fail();

		} catch (EntityNotFoundException e) {

			Assert.assertEquals(DGCErrorCodes.CATEGORY_NOT_FOUND, e.getErrorCode());
		}
	}

	@Test
	public void testDemoteCategoryToObjectType() {
		Term concept2 = termComponent.addTerm(voc.getId(), "Concept2");
		Term catTypeTerm2 = categorizationComponent.addCategorizationType(voc.getId(), "CatType2", concept2.getId()
				.toString());
		Term cat2 = categorizationComponent.addCategory(voc.getId(), "Category2", catTypeTerm2.getId().toString());
		cat2 = categorizationComponent.demoteCategoryToObjectType(cat2.getId());
		Assert.assertFalse(categorizationComponent.isCategory(cat2.getObjectType().getId()));
	}

	@Test
	public void testPromoteObjectTypeToCategorizationType() {
		Term catTypeTerm2 = termComponent.addTerm(voc.getId(), "CategorizationType2");

		catTypeTerm2 = categorizationComponent.promoteObjectTypeToCategorizationType(catTypeTerm2.getId(),
				concept.getId());
		Assert.assertTrue(categorizationComponent.isCategorizationType(catTypeTerm2.getId()));
	}

	@Test
	public void testDemoteCategorizationTypeToObjectType() {
		catTypeTerm = categorizationComponent.demoteCategorizationTypeToObjectType(catTypeTerm.getId());
		Assert.assertFalse(categorizationComponent.isCategorizationType(catTypeTerm.getId()));
	}

	@Test
	public void testGetCategories() {
		Term category2 = categorizationComponent.addCategory(voc.getId(), "Category2", catTypeTerm.getId());
		Collection<Category> categories = categorizationComponent.getCategories(catTypeTerm.getId());
		assertContainsCategories(categories, category, category2);
	}

	@Test
	public void testGetCategorizationTypes() {
		Term catTypeTerm2 = categorizationComponent.addCategorizationType(voc.getId(), "CatType2", concept.getId());

		Collection<CategorizationType> types = categorizationComponent.getCategorizationTypes(concept.getId());
		assertContainsCategories(types, catTypeTerm, catTypeTerm2);
	}

	@Test
	public void testGetScheme() {
		categorizationComponent.addCategoryToCategorizationScheme(scheme.getId(), category.getId());

		Collection<Term> schemes = categorizationComponent.getSchemesForConcept(concept.getId());
		Assert.assertEquals(1, schemes.size());
		Assert.assertEquals(scheme.getId(), schemes.iterator().next().getId());

		schemes = categorizationComponent.getSchemesForCategory(category.getId());
		Assert.assertEquals(1, schemes.size());
		Assert.assertEquals(scheme.getId(), schemes.iterator().next().getId());
	}

	private void assertContainsCategories(Collection<? extends ObjectType> categories, Term... terms) {
		for (ObjectType category : categories) {
			boolean contains = false;
			for (Term term : terms) {
				if (category.getId().equals(term.getObjectType().getId())) {
					contains = true;
					break;
				}
			}

			Assert.assertTrue(contains);
		}
	}
}
