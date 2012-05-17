package com.collibra.dgc.service.categorization;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.CategorizationService;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * Tests for {@link CategorizationService}.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCategorizationService extends AbstractServiceTest {

	private Vocabulary euRentVocabulary = null;
	private Term carModel;
	private CategorizationType carBodyStyleType;
	private ObjectType schemeModelsByBodyStyle;
	private Category sedan;
	private Category coupe;
	private Category convertible;

	private Term vehicleModel;
	private CategorizationType vehicleSize;
	private Category largeVehicle;
	private Category smallVehicle;
	private Category smallCar;

	private CategorizationType abstractVehicleType;
	private Category abstractVehicle;
	private Category abstractCar;

	@Before
	public void onSetUp() throws Exception {
		euRentVocabulary = prepareEuRentVocabulary();
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
	}

	@After
	public void onTearDown() {
		euRentVocabulary = null;
	}

	private Vocabulary prepareEuRentVocabulary() {
		Community euRentCommunity = communityFactory.makeCommunity("EU-Rent", "eurent");
		Community englishEuRent = communityFactory.makeCommunity(euRentCommunity, "English EU-Rent", "englisheurent");
		euRentVocabulary = representationFactory.makeVocabulary(englishEuRent, "EU-Rent Vocabulary", "eurentvoc");
		communityService.save(euRentCommunity);
		final Vocabulary ret = representationService.saveVocabulary(euRentVocabulary);
		resetTransaction();
		return ret;
	}

	private void prepareCarBodyScheme() {
		carModel = representationFactory.makeTerm(euRentVocabulary, "Car Model");
		representationFactory.makeTerm(euRentVocabulary, "Regular Term");
		euRentVocabulary = representationService.saveVocabulary(euRentVocabulary);
		resetTransaction();

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		carModel = representationService.findTermByResourceId(carModel.getId());

		carBodyStyleType = categorizationFactory.createCategorizationType(euRentVocabulary, "Car Body Style",
				carModel.getObjectType());
		schemeModelsByBodyStyle = (ObjectType) categorizationFactory.createCategorizationScheme(
				carModel.getObjectType(), euRentVocabulary, "Models by Body Style");
		sedan = categorizationFactory.createCategory("Sedan", schemeModelsByBodyStyle, carBodyStyleType,
				euRentVocabulary);
		coupe = categorizationFactory.createCategory("Coupe", schemeModelsByBodyStyle, carBodyStyleType,
				euRentVocabulary);
		convertible = categorizationFactory.createCategory("Convertible", schemeModelsByBodyStyle, carBodyStyleType,
				euRentVocabulary);
	}

	private void extendCarBodyScheme() {

		carModel = representationService.findTermByResourceId(carModel.getId());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		vehicleModel = representationFactory.makeTerm(euRentVocabulary, "Vehicle Model");
		representationService.save(vehicleModel);

		representationService.setGeneralConceptRepresentation(carModel, vehicleModel);

		vehicleSize = categorizationFactory.createCategorizationType(euRentVocabulary, "Vehicle Size",
				vehicleModel.getObjectType());
		largeVehicle = categorizationFactory.createCategory("Long Vehicle", vehicleSize, euRentVocabulary);
		smallVehicle = categorizationFactory.createCategory("Small Vehicle", vehicleSize, euRentVocabulary);

		categorizationService.commit(vehicleSize);
		resetTransaction();
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		vehicleModel = representationService.findTermByResourceId(vehicleModel.getId());
	}

	private void extendCarBodySchemeInheritance() {
		abstractVehicleType = categorizationService.createCategorizationType("Abstract Vehicle Type", vehicleModel,
				euRentVocabulary);

		resetTransaction();

		abstractVehicleType = categorizationService.findCategorizationTypeByResourceId(abstractVehicleType.getId());

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		vehicleModel = representationService.findTermByResourceId(vehicleModel.getId());
		abstractVehicle = categorizationService.createCategoryForConcept("Abstract Vehicle",
				vehicleModel.getObjectType(), abstractVehicleType, euRentVocabulary);

		resetTransaction();

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		carModel = representationService.findTermByResourceId(carModel.getId());
		abstractCar = categorizationService.createCategoryForConcept("Abstract Car", carModel.getObjectType(),
				abstractVehicleType, euRentVocabulary);

		resetTransaction();

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
	}

	private ObjectType reloadScheme() {
		schemeModelsByBodyStyle = (ObjectType) categorizationService.findSchemeByResourceId(schemeModelsByBodyStyle
				.getId());
		return schemeModelsByBodyStyle;
	}

	@Test
	public void testAutosuggestMethods() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		carModel = representationService.findTermByResourceId(carModel.getId());

		extendCarBodyScheme();
		extendCarBodySchemeInheritance();

		carModel = representationService.findTermByResourceId(carModel.getId());
		Term modelByBodyStyle = representationService.findTermBySignifier(euRentVocabulary, "Models by Body Style");
		Term carBodyStyleTypeTerm = representationService.findTermBySignifier(euRentVocabulary, "Car Body Style");
		Term coupe = representationService.findTermBySignifier(euRentVocabulary, "Coupe");
		Term smallVehicle = representationService.findTermBySignifier(euRentVocabulary, "Small Vehicle");
		Term vehicleSize = representationService.findTermBySignifier(euRentVocabulary, "Vehicle Size");

		// test suggestCategories methods
		assertEquals(2, suggesterService.suggestCategories(carBodyStyleTypeTerm, "").size());
		assertEquals(1, suggesterService.suggestCategories(carBodyStyleTypeTerm, "re").size());
		assertEquals(1, suggesterService.suggestCategories(carBodyStyleTypeTerm, "RE").size());

		assertEquals(3, suggesterService.suggestCategories(vehicleSize, "").size());

		// test suggestConceptsForScheme methods
		assertEquals(1, suggesterService.suggestConceptsForScheme(modelByBodyStyle, "").size());

		// test suggestCategorizationTypes methods
		List<Term> suggestedTypes = suggesterService.suggestCategorizationTypes(carModel, "");
		assertEquals(2, suggestedTypes.size());
		suggestedTypes = suggesterService.suggestCategorizationTypes(carModel, "vehicle");
		assertEquals(vehicleSize.getId(), suggestedTypes.get(0).getId());
		assertEquals(1, suggestedTypes.size());
		suggestedTypes = suggesterService.suggestCategorizationTypes(carModel, "hicle");
		assertEquals(0, suggestedTypes.size());
		suggestedTypes = suggesterService.suggestCategorizationTypes(coupe, "");
		assertEquals(3, suggestedTypes.size());
		suggestedTypes = suggesterService.suggestCategorizationTypes(smallVehicle, "");
		assertEquals(2, suggestedTypes.size());

	}

	@Test
	public void testFindCategoriesTermsForConcept() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		carModel = representationService.findTermByResourceId(carModel.getId());

		extendCarBodyScheme();
		extendCarBodySchemeInheritance();

		resetTransaction();

		carModel = representationService.findTermByResourceId(carModel.getId());
		vehicleModel = representationService.findTermByResourceId(vehicleModel.getId());
		Term carBodyStyleTypeTerm = representationService.findTermBySignifier(euRentVocabulary, "Car Body Style");
		Term abstractVehicleTypeTerm = representationService.findTermBySignifier(euRentVocabulary,
				"Abstract Vehicle Type");

		assertEquals(4, categorizationService.findCategoriesTermsForConcept(carModel.getObjectType()).size());
		assertEquals(3, categorizationService.findCategoriesTermsForConcept(vehicleModel.getObjectType()).size());

		Map<Term, List<Term>> map = categorizationService
				.getCategorizationTypeTermToCategoriesTermMapForConcept(carModel.getObjectType());
		assertEquals(2, map.size());
		assertEquals(3, map.get(carBodyStyleTypeTerm).size());
		assertEquals(1, map.get(abstractVehicleTypeTerm).size());
	}

	@Test
	public void testCreateCategorizationScheme() {
		prepareCarBodyScheme();

		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());

		// Lookup in DB
		resetTransaction();

		Concept reloadedScheme = reloadScheme();
		Assert.assertNotNull(reloadedScheme);

		// Assert for categorization type
		CategorizationType reloadedType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType
				.getId());
		Assert.assertNotNull(reloadedType);
		Assert.assertEquals(objectTypeDao.getMetaCategorizationType(), reloadedType.getType());

		// Assert for categorization scheme.
		Assert.assertEquals(3, reloadedScheme.getCategories().size());
		System.out.println(verbaliserComponent.verbalise(reloadedScheme, euRentVocabulary));
		Assert.assertEquals(reloadedType, reloadedScheme.getCategories().iterator().next().getType());

		carModel = representationService.findTermByResourceId(carModel.getId());

		// Assert for categories.
		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		Assert.assertEquals(sedan.getType(), reloadedType);
		Assert.assertEquals(sedan.getGeneralConcept(), carModel.getObjectType());

		coupe = categorizationService.findCategoryByResourceId(coupe.getId());
		Assert.assertEquals(coupe.getType(), reloadedType);
		Assert.assertEquals(coupe.getGeneralConcept(), carModel.getObjectType());

		convertible = categorizationService.findCategoryByResourceId(convertible.getId());
		Assert.assertEquals(convertible.getType(), reloadedType);
		Assert.assertEquals(convertible.getGeneralConcept(), carModel.getObjectType());

		// add more depth to the scheme
		Term sedanT = representationService.findPreferredTerm(sedan);

		CategorizationType sedanSize = categorizationService.createCategorizationType("Sedan Size", sedanT,
				sedanT.getVocabulary());
		resetTransaction();
		sedanT = representationService.findTermByResourceId(sedanT.getId());
		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		sedanSize = categorizationService.findCategorizationTypeByResourceId(sedanSize.getId());
		Category largeSedan = categorizationService.createCategory("large sedan", sedanSize, sedanT.getVocabulary());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Category smallSedan = categorizationService.createCategory("small sedan", sedanSize, sedanT.getVocabulary());
		schemeModelsByBodyStyle = (ObjectType) categorizationService.findSchemeByResourceId(schemeModelsByBodyStyle
				.getId());
		// remove the sedan selection from the scheme
		categorizationService
				.removeCategoryFromCategorizationScheme(schemeModelsByBodyStyle.findPreferredTerm(), sedan);

		// Lookup in DB
		resetTransaction();
		schemeModelsByBodyStyle = (ObjectType) categorizationService.findSchemeByResourceId(schemeModelsByBodyStyle
				.getId());

		// check that everything is correctly removed
		Assert.assertTrue(!schemeModelsByBodyStyle.getCategories().contains(sedan));

		categorizationService
				.addCategoryToCategorizationScheme(schemeModelsByBodyStyle.findPreferredTerm(), smallSedan);

		// Lookup in DB
		resetTransaction();
		schemeModelsByBodyStyle = (ObjectType) categorizationService.findSchemeByResourceId(schemeModelsByBodyStyle
				.getId());

		// check that everything is correctly added
		Assert.assertTrue(schemeModelsByBodyStyle.getCategories().contains(sedan));
		Assert.assertTrue(schemeModelsByBodyStyle.getCategories().contains(smallSedan));
	}

	/**
	 * Removing {@link CategorizationType} should have the following effects:
	 * <p>
	 * - All terms with this categorization type as meaning are removed.
	 * <p>
	 * - All categories of this type will be changed to normal terms with taxonomy to general type.
	 */
	@Test
	public void testRemoveCategorizationType() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		// Assert for categorization type
		CategorizationType reloadedType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType
				.getId());
		Assert.assertNotNull(reloadedType);

		// Remove categorization type.
		categorizationService.removeCategorizationType(reloadedType.findPreferredTerm());
		resetTransaction();

		// Categorization type should be removed.
		reloadedType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		Assert.assertNull(reloadedType);

		// All categories also removed.
		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		Assert.assertNull(sedan);

		coupe = categorizationService.findCategoryByResourceId(coupe.getId());
		Assert.assertNull(coupe);

		convertible = categorizationService.findCategoryByResourceId(convertible.getId());
		Assert.assertNull(convertible);

		// The categorization scheme should not have any categories.
		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		Assert.assertNotNull(schemeModelsByBodyStyle);
		Assert.assertEquals(0, schemeModelsByBodyStyle.getCategories().size());

		// All category terms are normal terms with classifying concept as general concept.
		carModel = representationService.findTermByResourceId(carModel.getId());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Term term = euRentVocabulary.getTerm("Sedan");
		Assert.assertNotNull(term);
		Assert.assertNotSame(carBodyStyleType.getId(), term.getObjectType().getId());
		Assert.assertEquals(carModel.getObjectType(), term.getObjectType().getGeneralConcept());

		term = euRentVocabulary.getTerm("Coupe");
		Assert.assertNotNull(term);
		Assert.assertNotSame(carBodyStyleType.getId(), term.getObjectType().getId());
		Assert.assertEquals(carModel.getObjectType(), term.getObjectType().getGeneralConcept());

		term = euRentVocabulary.getTerm("Convertible");
		Assert.assertNotNull(term);
		Assert.assertNotSame(carBodyStyleType.getId(), term.getObjectType().getId());
		Assert.assertEquals(carModel.getObjectType(), term.getObjectType().getGeneralConcept());
	}

	/**
	 * Removing {@link Category} will have the following effects:
	 * <p>
	 * - All terms with category as meaning will be removed.
	 * <p>
	 * - The category will be removed from the categorization schemes composing them.
	 */
	@Test
	public void testRemoveCategory() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		// Assert for category.
		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		Assert.assertNotNull(sedan);

		// Remove the category.
		categorizationService.removeCategory(sedan.findPreferredTerm());
		resetTransaction();

		// Category is removed.
		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		Assert.assertNull(sedan);

		// The categorization schemes do not contain the removed category.
		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		Assert.assertNotNull(schemeModelsByBodyStyle);
		Assert.assertEquals(2, schemeModelsByBodyStyle.getCategories().size());
	}

	/**
	 * Removing the {@link Term} whose {@link Concept} being classified results in the following effects if the
	 * {@link Term} removed is the only {@link Representation} of the {@link Concept}:
	 * <p>
	 * - All categorization types of the classified concept are removed.
	 * <p>
	 * - All categorization schemes will lose the classifying concept and its categories.
	 */
	@Test
	public void testRemoveClassifiedConcept() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		// Assert for term whose concept being classified
		carModel = representationService.findTermByResourceId(carModel.getId());
		representationService.remove(carModel);
		resetTransaction();

		// Remove the term.
		carModel = representationService.findTermByResourceId(carModel.getId());
		Assert.assertNull(carModel);

		// All categories of the classified concept are removed.
		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		Assert.assertNull(sedan);

		// All categorization types of the classified concept are removed.
		CategorizationType reloadedType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType
				.getId());
		Assert.assertNull(reloadedType);

		// Categorization schemes lose all categories of the classified concept.
		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		Assert.assertNotNull(schemeModelsByBodyStyle);
		Assert.assertEquals(0, schemeModelsByBodyStyle.getCategories().size());
		Assert.assertEquals(0, schemeModelsByBodyStyle.getConceptsCategorized().size());
	}

	/**
	 * Removing the {@link Term} whose meaning is a {@link Category} will have the following effects if this is the only
	 * {@link Term} representing the {@link Category}:
	 * <p>
	 * - Category is removed.
	 * <p>
	 * - All schemes having the removed category will lose the category.
	 */
	@Test
	public void testRemoveCategoryTerm() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		// Assert for category and its only term.
		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		Assert.assertNotNull(sedan);

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Term term = euRentVocabulary.getPreferredTerm(sedan);
		Assert.assertNotNull(term);

		// Remove the term.
		representationService.remove(term);
		resetTransaction();

		// Category is removed.
		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		Assert.assertNull(sedan);

		// Categorization schemes lose the category
		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		Assert.assertNotNull(schemeModelsByBodyStyle);
		Assert.assertEquals(2, schemeModelsByBodyStyle.getCategories().size());

		// Categorization type will not have the category anymore.
		CategorizationType reloadedType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType
				.getId());
		Assert.assertNotNull(reloadedType);
		Assert.assertEquals(2, categorizationService.findCategories(reloadedType).size());
	}

	/**
	 * Removing {@link Term} whose meaning is the {@link CategorizationType} will have the following effects if the
	 * {@link Term} is the only {@link Representation} for the {@link CategorizationType}:
	 * <p>
	 * - All categories of this type are removed.
	 * <p>
	 * - All categorization schemes lose categories from of this type.
	 */
	@Test
	public void testRemoveCategorizationTypeTerm() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		CategorizationType reloadedType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType
				.getId());
		Assert.assertNotNull(reloadedType);

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Term term = euRentVocabulary.getPreferredTerm(reloadedType);
		Assert.assertNotNull(term);

		representationService.remove(term);
		resetTransaction();

		reloadedType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		Assert.assertNull(reloadedType);

		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		Assert.assertNull(sedan);

		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		Assert.assertNotNull(schemeModelsByBodyStyle);
		Assert.assertEquals(0, schemeModelsByBodyStyle.getCategories().size());
	}

	/**
	 * If a categorization scheme {@link Term} is removed then if categorization concept has no other term then it is
	 * also removed.
	 */
	@Test
	public void testRemoveCategorizationSchemeTerm() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		Assert.assertNotNull(schemeModelsByBodyStyle);

		Term schemeTerm = (Term) schemeModelsByBodyStyle.getRepresentations().iterator().next();
		Assert.assertNotNull(schemeTerm);

		// Remove categorization scheme term.
		representationService.remove(schemeTerm);

		resetTransaction();

		// The scheme and its term are removed.
		schemeTerm = representationService.findTermByResourceId(schemeTerm.getId());
		Assert.assertNull(schemeTerm);

		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		Assert.assertNull(schemeModelsByBodyStyle);

		// No schemes for the category
		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		Assert.assertNotNull(sedan);

		Assert.assertEquals(0, categorizationService.findSchemes(sedan).size());
	}

	/**
	 * Test about the result when a vocabulary or an entity above vocabulary in the hierarchy is removed that contains
	 * categorizations. All categorization relations for the {@link Term}s will be removed if there is no other
	 * {@link Term} for the meaning.
	 */
	@Test
	public void testRemoveFromVocabularyLevelOrAbove() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		// Remove vocabulary.
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Assert.assertNotNull(euRentVocabulary);

		representationService.remove(euRentVocabulary);
		resetTransaction();

		// Assert that all categorizations are removed.
		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		Assert.assertNull(schemeModelsByBodyStyle);

		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		Assert.assertNull(sedan);

		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		Assert.assertNull(carBodyStyleType);
	}

	@Test
	public void testPromoteTermToCategory() {
		prepareCarBodyScheme();

		// Create term 'Hatchback' to be later promoted to category.
		Term hatchback = representationFactory.makeTerm(euRentVocabulary, "Hatchback");
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		hatchback = representationService.findTermByResourceId(hatchback.getId());

		// Promote
		categorizationService.promoteObjectTypeToCategory(hatchback, schemeModelsByBodyStyle.findPreferredTerm(),
				carBodyStyleType);
		resetTransaction();

		// Assert for new category added
		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		hatchback = representationService.findTermByResourceId(hatchback.getId());
		Assert.assertEquals(carBodyStyleType.getIsForConcept(), hatchback.getObjectType().getGeneralConcept());
		Assert.assertEquals(carBodyStyleType, hatchback.getObjectType().getType());
		Assert.assertEquals(4, schemeModelsByBodyStyle.getCategories().size());
	}

	@Test
	public void testDemoteCategoryToTerm() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		sedan = categorizationService.findCategoryByResourceId(sedan.getId());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Term sedanTerm = euRentVocabulary.getPreferredTerm(sedan);
		Assert.assertNotNull(sedanTerm);

		// Demote category sedan to term.
		categorizationService.demoteCategoryToObjectType(sedan.findPreferredTerm());
		resetTransaction();

		sedanTerm = representationService.findTermBySignifier(euRentVocabulary, "Sedan");

		Assert.assertNotNull(sedanTerm);

		// Assert for term sedan not a category.
		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		Assert.assertNotSame(schemeModelsByBodyStyle, sedanTerm.getObjectType().getType());

		carModel = representationService.findTermByResourceId(carModel.getId());
		Assert.assertEquals(carModel.getObjectType(), sedanTerm.getObjectType().getGeneralConcept());

		schemeModelsByBodyStyle = meaningService.findObjectTypeByResourceId(schemeModelsByBodyStyle.getId());
		Assert.assertEquals(2, schemeModelsByBodyStyle.getCategories().size());
	}

	@Test
	public void testPromoteTermToCategorizationType() {
		prepareCarBodyScheme();
		// Create the model by year term later to be promoted to categorization type.
		Term modelByYearTypeTerm = representationFactory.makeTerm(euRentVocabulary, "Model by year Type");
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		modelByYearTypeTerm = representationService.findTermByResourceId(modelByYearTypeTerm.getId());
		carModel = representationService.findTermByResourceId(carModel.getId());

		// Promote
		CategorizationType modelByYearType = categorizationService.promoteObjectTypeToCategorizationType(
				modelByYearTypeTerm, carModel.getObjectType());
		resetTransaction();

		modelByYearType = categorizationService.findCategorizationTypeByResourceId(modelByYearType.getId());
		Assert.assertNotNull(modelByYearType);

		// Create categories.
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		categorizationFactory.createCategory("1900", modelByYearType, euRentVocabulary);
		categorizationFactory.createCategory("2000", modelByYearType, euRentVocabulary);
		representationService.saveVocabulary(euRentVocabulary);
		resetTransaction();

		// Assert for categorization type.
		modelByYearType = categorizationService.findCategorizationTypeByResourceId(modelByYearType.getId());
		Assert.assertNotNull(modelByYearType);
		Assert.assertEquals(2, categorizationService.findCategories(modelByYearType).size());
	}

	@Test
	public void testDemoteCategorizationTypeToTerm() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		// Demote car model by body style to term.
		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		ObjectType ot = categorizationService
				.demoteCategorizationTypeToObjectType(carBodyStyleType.findPreferredTerm());
		resetTransaction();

		Assert.assertNull(categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId()));

		schemeModelsByBodyStyle = (ObjectType) categorizationService.findSchemeByResourceId(schemeModelsByBodyStyle
				.getId());
		Assert.assertEquals(0, schemeModelsByBodyStyle.getCategories().size());
	}

	@Test
	public void testCreateCategorizationTypeWithoutTermReuse() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		// Create new categorization type model by year.
		carModel = representationService.findTermByResourceId(carModel.getId());
		CategorizationType newType = categorizationService.createCategorizationType("ModelByYearType", carModel,
				carModel.getVocabulary());
		resetTransaction();

		newType = categorizationService.findCategorizationTypeByResourceId(newType.getId());
		Assert.assertNotNull(newType);

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Term newTypeTerm = euRentVocabulary.getPreferredTerm(newType);
		Assert.assertNotNull(newTypeTerm);
		Assert.assertEquals("ModelByYearType", newTypeTerm.getSignifier());

		carModel = representationService.findTermByResourceId(carModel.getId());
		Assert.assertEquals(carModel.getObjectType(), newType.getIsForConcept());

		// Add categories to this new type and assert the same.
		assertCanCreateCategories(newType);
	}

	@Test
	public void testCreateCategorizationTypeWithTermReuse() {
		prepareCarBodyScheme();
		Term newTypeTerm = representationFactory.makeTerm(euRentVocabulary, "ModelByYearType");
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		// Create categorization type by reusing ModelByYearType.
		carModel = representationService.findTermByResourceId(carModel.getId());
		CategorizationType newType = categorizationService.createCategorizationType("ModelByYearType", carModel,
				carModel.getVocabulary());
		resetTransaction();

		// Now assert that the new type term is converted to categorization type.
		newType = categorizationService.findCategorizationTypeByResourceId(newType.getId());
		Assert.assertNotNull(newType);
		newTypeTerm = representationService.findTermByResourceId(newTypeTerm.getId());
		Assert.assertEquals(newType, newTypeTerm.getObjectType());

		// Add categories to this new type and assert the same.
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		assertCanCreateCategories(newType);
	}

	@Test
	public void testCreateCategorizationTypeWithTermReuseException() {
		prepareCarBodyScheme();
		Term vehiclePeriod = representationFactory.makeTerm(euRentVocabulary, "Vehicle Period");
		Term outDatedModel = representationFactory.makeTerm(euRentVocabulary, "Outdated Model");
		outDatedModel.getObjectType().setType(vehiclePeriod.getObjectType());
		categorizationFactory.createCategorizationType(euRentVocabulary, "ModelByYearType",
				vehiclePeriod.getObjectType());
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		carModel = representationService.findTermByResourceId(carModel.getId());
		try {
			// Reusing fails because the term is already a categorization type.
			categorizationService.createCategorizationType("ModelByYearType", carModel, carModel.getVocabulary());
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			rollback();
		}

		carModel = representationService.findTermByResourceId(carModel.getId());
		try {
			// Reusing fails because the term is of different type.
			categorizationService.createCategorizationType("Outdated Model", carModel, carModel.getVocabulary());
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			rollback();
		}
	}

	@Test
	public void testCreateCategoryWithoutTermReuse() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		// Create categories hatchback not in any scheme and limo in the existing scheme.
		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Category hatchback = categorizationService.createCategory("Hatchback", carBodyStyleType, euRentVocabulary);

		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		schemeModelsByBodyStyle = (ObjectType) categorizationService.findSchemeByResourceId(schemeModelsByBodyStyle
				.getId());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Category limo = categorizationService.createCategory("Limo", schemeModelsByBodyStyle.findPreferredTerm(),
				carBodyStyleType, euRentVocabulary);

		resetTransaction();

		// Assert for newly created categories.
		assertCategoriesReuse(hatchback, limo);
	}

	@Test
	public void testCreateCategoryWithTermReuse() {
		prepareCarBodyScheme();
		Term hatchbackTerm = representationFactory.makeTerm(euRentVocabulary, "Hatchback");
		ObjectType hatchOT = hatchbackTerm.getObjectType();
		Term limoTerm = representationFactory.makeTerm(euRentVocabulary, "Limo");
		ObjectType limoOT = limoTerm.getObjectType();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		// Create category hatchback
		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Category hatchback = categorizationService.createCategory("Hatchback", carBodyStyleType, euRentVocabulary);
		resetTransaction();

		// Create category limo part of scheme
		schemeModelsByBodyStyle = (ObjectType) categorizationService.findSchemeByResourceId(schemeModelsByBodyStyle
				.getId());
		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Category limo = categorizationService.createCategory("Limo", schemeModelsByBodyStyle.findPreferredTerm(),
				carBodyStyleType, euRentVocabulary);
		resetTransaction();

		// Assert for newly created categories.
		assertCategoriesReuse(hatchback, limo);

		// Assert for terms reuse.
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		hatchback = categorizationService.findCategoryByResourceId(hatchback.getId());
		limo = categorizationService.findCategoryByResourceId(limo.getId());
		hatchbackTerm = representationService.findTermByResourceId(hatchbackTerm.getId());
		limoTerm = representationService.findTermByResourceId(limoTerm.getId());

		Assert.assertEquals(hatchback, hatchbackTerm.getObjectType());
		Assert.assertEquals(limo, limoTerm.getObjectType());
	}

	@Test
	public void testCreateCategoryWithTermReuseException() {
		prepareCarBodyScheme();

		// Create a category that we will try to reuse.
		Term bigCar = representationFactory.makeTerm(euRentVocabulary, "Big Car");
		CategorizationType carBySize = categorizationFactory.createCategorizationType(euRentVocabulary, "Car by size",
				bigCar.getObjectType());
		categorizationFactory.createCategory("Limo", carBySize, euRentVocabulary);

		// Create a term with general concept that we will try to reuse for category.
		Term hatchback = representationFactory.makeTerm(euRentVocabulary, "Hatchback");
		hatchback.getObjectType().setGeneralConcept(carModel.getObjectType());

		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		// Create category hatchback
		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());

		// Try reusing hatchback which is already a category and fails.
		try {
			categorizationService.createCategory("Hatchback", carBodyStyleType, euRentVocabulary);
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			rollback();
		}

		resetTransaction();

		// Try reusing limo which has other general concept and fails.
		try {
			euRentVocabulary = representationService.findVocabularyByName(euRentVocabulary.getName());
			categorizationService.createCategory("Limo", carBodyStyleType, euRentVocabulary);
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			rollback();
		}
	}

	@Test
	public void testReuseCatgorizationTypeNotPossibleDueToNoTaxonomicalPrentRelation() {
		prepareCarBodyScheme();
		Term bigCar = representationFactory.makeTerm(euRentVocabulary, "Big Car");
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		bigCar = representationService.findTermByResourceId(bigCar.getId());

		// Since big car is not a taxonomical child of car model, the categorization type car body style type cannot be
		// reused to create categories for big car.
		try {
			categorizationService.createCategoryForConcept("New Category", bigCar.getObjectType(), carBodyStyleType,
					euRentVocabulary);
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			rollback();
		}
	}

	@Test
	public void testReuseCategorizationTypePossibleDueToTaxonomicalParentRelation() {
		prepareCarBodyScheme();
		Term bigCar = representationFactory.makeTerm(euRentVocabulary, "Big Car");

		// Ensure that big car is a specialized concept of car model to reuse the categorization types of car model.
		bigCar.getObjectType().setGeneralConcept(carModel.getObjectType());
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		bigCar = representationService.findTermByResourceId(bigCar.getId());

		// Since big car is not a taxonomical child of car model, it is possible to reuse the categorization type car
		// body style type to create categories for big car.
		Category newCategory = categorizationService.createCategoryForConcept("New Category", bigCar.getObjectType(),
				carBodyStyleType, euRentVocabulary);
		resetTransaction();

		bigCar = representationService.findTermByResourceId(bigCar.getId());
		newCategory = categorizationService.findCategoryByResourceId(newCategory.getId());
		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		Assert.assertNotNull(newCategory);
		Assert.assertEquals(bigCar.getObjectType(), newCategory.getGeneralConcept());
		Assert.assertEquals(carBodyStyleType, newCategory.getType());
	}

	/**
	 * #8866. Can't delete empty root node on Category Creation Section
	 */
	@Test
	public void testBug8866() {
		Term term = representationFactory.makeTerm(euRentVocabulary, "T");
		CategorizationType type1 = categorizationFactory.createCategorizationType(euRentVocabulary, "Type1",
				term.getObjectType());
		CategorizationType type2 = categorizationFactory.createCategorizationType(euRentVocabulary, "Type2",
				term.getObjectType());
		CategorizationType type3 = categorizationFactory.createCategorizationType(euRentVocabulary, "Type3",
				term.getObjectType());
		euRentVocabulary = representationService.saveVocabulary(euRentVocabulary);
		resetTransaction();

		type1 = categorizationService.findCategorizationTypeByResourceId(type1.getId());
		categorizationService.removeCategorizationType(type1.findPreferredTerm());
		getCurrentSession().flush();

		type2 = categorizationService.findCategorizationTypeByResourceId(type2.getId());
		categorizationService.removeCategorizationType(type2.findPreferredTerm());
		getCurrentSession().flush();

		type3 = categorizationService.findCategorizationTypeByResourceId(type3.getId());
		categorizationService.removeCategorizationType(type3.findPreferredTerm());
		getCurrentSession().flush();

		resetTransaction();
	}

	/**
	 * #8847. Category Creation > Backend allows to add duplicated name to the same node
	 */
	@Test
	public void testBug8847() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		carModel = representationService.findTermByResourceId(carModel.getId());

		try {
			categorizationService.createCategorizationType("Car Body Style", carModel, euRentVocabulary);
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			rollback();
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS, ex.getErrorCode());
		}
	}

	@Test
	public void testFindCategoryTerms() {
		Term term = representationFactory.makeTerm(euRentVocabulary, "T");
		CategorizationType type = categorizationFactory.createCategorizationType(euRentVocabulary, "Type1",
				term.getObjectType());
		categorizationFactory.createCategory("X Cat", type, euRentVocabulary);
		categorizationFactory.createCategory("A Cat", type, euRentVocabulary);
		categorizationFactory.createCategory("d Cat", type, euRentVocabulary);
		categorizationFactory.createCategory("a Cat", type, euRentVocabulary);
		categorizationFactory.createCategory("B Cat", type, euRentVocabulary);
		euRentVocabulary = representationService.saveVocabulary(euRentVocabulary);
		resetTransaction();

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		type = categorizationService.findCategorizationTypeByResourceId(type.getId());
		List<Term> terms = categorizationService.findCategoryTerms(type, euRentVocabulary);
		Assert.assertEquals(5, terms.size());

		System.out.println(terms);
		// Assert that category terms are sorted.
		assertSortedOrder(terms, true);

		terms = (List<Term>) categorizationService.findCategoryTerms(euRentVocabulary);
		Assert.assertEquals(5, terms.size());
		// Assert that category terms are sorted.
		assertSortedOrder(terms, true);
	}

	@Test
	public void testFindCategorizationTypeTermsForConcept() {
		Term term = representationFactory.makeTerm(euRentVocabulary, "T");
		categorizationFactory.createCategorizationType(euRentVocabulary, "B", term.getObjectType());
		categorizationFactory.createCategorizationType(euRentVocabulary, "X", term.getObjectType());
		categorizationFactory.createCategorizationType(euRentVocabulary, "b", term.getObjectType());
		categorizationFactory.createCategorizationType(euRentVocabulary, "A", term.getObjectType());
		euRentVocabulary = representationService.saveVocabulary(euRentVocabulary);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		List<Term> terms = categorizationService.findCategorizationTypeTermsForConcept(term.getObjectType());
		Assert.assertEquals(4, terms.size());
		// Assert that category terms are sorted.
		assertSortedOrder(terms, true);
	}

	@Test
	public void testCreateCategorizationTypeOnSelf() {
		Term term = representationFactory.makeTerm(euRentVocabulary, "T");
		representationService.saveTerm(term);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		try {
			categorizationService.createCategorizationType(term.getSignifier(), term, term.getVocabulary());
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_TYPE_TERM_ALREADY_EXISTS, ex.getErrorCode());
		}
	}

	@Test
	public void testCreateCategoryOnSelf() {
		Term term = representationFactory.makeTerm(euRentVocabulary, "T");
		CategorizationType catType = categorizationFactory.createCategorizationType(euRentVocabulary, "B",
				term.getObjectType());
		euRentVocabulary = representationService.saveVocabulary(euRentVocabulary);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		catType = categorizationService.findCategorizationTypeByResourceId(catType.getId());
		try {
			categorizationService.createCategory(term.getSignifier(), catType, term.getVocabulary());
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			Assert.assertEquals(DGCErrorCodes.CATEGORY_TERM_CANNOT_BE_CATEGORY_SELF, ex.getErrorCode());
		}
	}

	@Test
	public void testCreateCategoryForConceptOnSelf() {
		Term term = representationFactory.makeTerm(euRentVocabulary, "T");
		CategorizationType catType = categorizationFactory.createCategorizationType(euRentVocabulary, "B",
				term.getObjectType());
		euRentVocabulary = representationService.saveVocabulary(euRentVocabulary);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		catType = categorizationService.findCategorizationTypeByResourceId(catType.getId());
		try {
			categorizationService.createCategoryForConcept(term.getSignifier(), term.getObjectType(), catType,
					term.getVocabulary());
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			Assert.assertEquals(DGCErrorCodes.CATEGORY_TERM_CANNOT_BE_CATEGORY_SELF, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategoryOnSelf() {
		Term term = representationFactory.makeTerm(euRentVocabulary, "T");
		CategorizationType catType = categorizationFactory.createCategorizationType(euRentVocabulary, "B",
				term.getObjectType());
		euRentVocabulary = representationService.saveVocabulary(euRentVocabulary);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		catType = categorizationService.findCategorizationTypeByResourceId(catType.getId());
		try {
			categorizationService.promoteObjectTypeToCategory(term, catType);
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			Assert.assertEquals(DGCErrorCodes.CATEGORY_TERM_CANNOT_BE_CATEGORY_SELF, ex.getErrorCode());
		}
	}

	@Test
	public void testpromoteObjectTypeToCategoryOnSelf() {
		Term term = representationFactory.makeTerm(euRentVocabulary, "T");
		CategorizationType catType = categorizationFactory.createCategorizationType(euRentVocabulary, "B",
				term.getObjectType());
		euRentVocabulary = representationService.saveVocabulary(euRentVocabulary);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		catType = categorizationService.findCategorizationTypeByResourceId(catType.getId());
		try {
			categorizationService.promoteObjectTypeToCategoryForConcept(term, term.getObjectType(), catType);
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			Assert.assertEquals(DGCErrorCodes.CATEGORY_TERM_CANNOT_BE_CATEGORY_SELF, ex.getErrorCode());
		}
	}

	@Test
	public void testPromoteObjectTypeToCategorizationTypeOnSelf() {
		Term term = representationFactory.makeTerm(euRentVocabulary, "T");
		representationService.saveTerm(term);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		try {
			categorizationService.promoteObjectTypeToCategorizationType(term, term.getObjectType());
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			Assert.assertEquals(DGCErrorCodes.CONCEPT_CANNOT_BE_CATEGORIZATION_TYPE_FOR_SELF, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveTerm() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		carModel = representationService.findTermByResourceId(carModel.getId());

		Collection<CategorizationType> types = categorizationService.findCategorizationTypes(carModel.getObjectType());
		Assert.assertTrue(types.size() > 0);

		Collection<Term> categories = categorizationService.findCategoryTerms(carModel.getVocabulary());
		Assert.assertTrue(categories.size() > 0);

		representationService.remove(carModel);
		resetTransaction();

		// No categorization types exist.
		for (CategorizationType type : types) {
			CategorizationType latest = categorizationService.findCategorizationTypeByResourceId(type.getId());
			Assert.assertNull(latest);
		}

		// All categories are gone.
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		categories = categorizationService.findCategoryTerms(euRentVocabulary);
		Assert.assertTrue(categories.size() == 0);
	}

	@Test
	public void testChangeConceptType() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Term term = representationFactory.makeTerm(euRentVocabulary, "Test Term");
		representationService.saveTerm(term);
		resetTransaction();

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		term = representationService.findTermByResourceId(term.getId());
		Term bodyStyleType = euRentVocabulary.getTerm("Car Body Style");
		Assert.assertNotNull(bodyStyleType.getObjectType());
		try {
			representationService.setConceptTypeRepresentation(bodyStyleType, term);
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_INVALID_OPERATION, ex.getErrorCode());
			resetTransaction();
		}

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		term = representationService.findTermByResourceId(term.getId());
		Term sedan = euRentVocabulary.getTerm("Sedan");
		try {
			representationService.setConceptTypeRepresentation(sedan, term);
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_INVALID_OPERATION, ex.getErrorCode());
			resetTransaction();
		}
	}

	@Test
	public void testChangeGeneralConcept() {
		prepareCarBodyScheme();
		categorizationService.commitScheme(schemeModelsByBodyStyle.findPreferredTerm());
		resetTransaction();

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Term term = representationFactory.makeTerm(euRentVocabulary, "Test Term");
		representationService.saveTerm(term);
		resetTransaction();

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		term = representationService.findTermByResourceId(term.getId());
		Term bodyStyleType = euRentVocabulary.getTerm("Car Body Style");
		Assert.assertNotNull(bodyStyleType.getObjectType());
		try {
			representationService.setGeneralConceptRepresentation(bodyStyleType, term);
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_INVALID_OPERATION, ex.getErrorCode());
			resetTransaction();
		}

		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		term = representationService.findTermByResourceId(term.getId());
		Term sedan = euRentVocabulary.getTerm("Sedan");
		try {
			representationService.setGeneralConceptRepresentation(sedan, term);
			Assert.fail();
		} catch (ConstraintViolationException ex) {
			// Success
			Assert.assertEquals(DGCErrorCodes.CATEGORIZATION_INVALID_OPERATION, ex.getErrorCode());
			resetTransaction();
		}
	}

	// @Test
	public void testBuildFSDMModel() {

	}

	// @Test
	public void testDestroyFSDMModel() {

	}

	private void assertSortedOrder(List<Term> terms, boolean asc) {
		for (int i = 1; i < terms.size(); i++) {
			if (asc) {
				Assert.assertTrue(terms.get(i).getSignifier().compareToIgnoreCase(terms.get(i - 1).getSignifier()) >= 0);
			} else {
				Assert.assertTrue(terms.get(i).getSignifier().compareToIgnoreCase(terms.get(i - 1).getSignifier()) <= 0);
			}
		}
	}

	private void assertCanCreateCategories(CategorizationType newType) {
		// Add categories to this new type and assert the same.
		Category cat2000 = categorizationService.createCategory("2000", newType, euRentVocabulary);
		euRentVocabulary = representationService.findVocabularyByResourceId(euRentVocabulary.getId());
		Category cat1990 = categorizationService.createCategory("1990", newType, euRentVocabulary);
		resetTransaction();

		cat2000 = categorizationService.findCategoryByResourceId(cat2000.getId());
		Assert.assertNotNull(cat2000);
		cat1990 = categorizationService.findCategoryByResourceId(cat1990.getId());
		Assert.assertNotNull(cat1990);

		newType = categorizationService.findCategorizationTypeByResourceId(newType.getId());
		Collection<Category> categories = categorizationService.findCategories(newType);
		Assert.assertEquals(2, categories.size());

		Assert.assertTrue(categories.contains(cat1990));
		Assert.assertTrue(categories.contains(cat2000));
	}

	private void assertCategoriesReuse(Category categoryWithoutScheme, Category categoryWithScheme) {
		schemeModelsByBodyStyle = (ObjectType) categorizationService.findSchemeByResourceId(schemeModelsByBodyStyle
				.getId());

		// Assert for new category hatchback which is not in the scheme yet.
		categoryWithoutScheme = categorizationService.findCategoryByResourceId(categoryWithoutScheme.getId());
		Assert.assertNotNull(categoryWithoutScheme);
		Assert.assertFalse(schemeModelsByBodyStyle.getCategories().contains(categoryWithoutScheme));

		carBodyStyleType = categorizationService.findCategorizationTypeByResourceId(carBodyStyleType.getId());
		Assert.assertTrue(categorizationService.findCategories(carBodyStyleType).contains(categoryWithoutScheme));

		// Assert for new category hatchback which is in the scheme
		categoryWithScheme = categorizationService.findCategoryByResourceId(categoryWithScheme.getId());
		Assert.assertNotNull(categoryWithScheme);
		Assert.assertTrue(schemeModelsByBodyStyle.getCategories().contains(categoryWithScheme));
	}
}
