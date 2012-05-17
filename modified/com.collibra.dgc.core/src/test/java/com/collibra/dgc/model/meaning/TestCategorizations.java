package com.collibra.dgc.model.meaning;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.collibra.dgc.core.component.VerbaliserComponent;
import com.collibra.dgc.core.model.categorizations.CategorizationFactory;
import com.collibra.dgc.core.model.categorizations.CategorizationType;
import com.collibra.dgc.core.model.categorizations.Category;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.CommunityService;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.service.AbstractBootstrappedServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCategorizations extends AbstractBootstrappedServiceTest {

	@Autowired
	private CommunityFactory communityFactory;
	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private CategorizationFactory categorizationFactory;
	@Autowired
	private VerbaliserComponent verbaliserComponent;
	@Autowired
	private RepresentationService representationService;
	@Autowired
	private CommunityService communityService;

	private Vocabulary euRentVocabulary = null;

	@Before
	public void onSetup() {
		euRentVocabulary = prepareEuRentVocabulary();
	}

	@After
	public void onTearDown() {
		euRentVocabulary = null;
	}

	public Vocabulary prepareEuRentVocabulary() {
		Community euRentCommunity = communityFactory.makeCommunity("EU-Rent", "eurent");
		communityService.save(euRentCommunity);
		Community englishEuRent = communityFactory.makeCommunity(euRentCommunity, "English EU-Rent", "englisheurent");
		communityService.save(englishEuRent);
		Vocabulary euRentVocabulary = representationFactory.makeVocabulary(englishEuRent, "EU-Rent Vocabulary",
				"eurentvoc");
		representationService.saveVocabulary(euRentVocabulary);
		return euRentVocabulary;
	}

	// test flat categorization scheme
	@Test
	public void testCarModelsExample() {
		// The car model concept
		Term carModel = representationFactory.makeTerm(euRentVocabulary, "Car Model");

		// The car body style categorization type
		CategorizationType carBodyStyleType = categorizationFactory.createCategorizationType(euRentVocabulary,
				"Car Body Style", carModel.getObjectType());
		Concept schemeModelsByBodyStyle = categorizationFactory.createCategorizationScheme(carModel.getObjectType(),
				euRentVocabulary, "Models by Body Style");
		Category sedan = categorizationFactory.createCategory("Sedan", schemeModelsByBodyStyle, carBodyStyleType,
				euRentVocabulary);
		Category coupe = categorizationFactory.createCategory("Coupe", schemeModelsByBodyStyle, carBodyStyleType,
				euRentVocabulary);
		Category convertible = categorizationFactory.createCategory("Convertible", schemeModelsByBodyStyle,
				carBodyStyleType, euRentVocabulary);

		// check the dedicated objecttype
		Assert.assertEquals(carModel.getObjectType(), carBodyStyleType.getIsForConcept());

		// check the categorization scheme
		Assert.assertEquals(carBodyStyleType, convertible.getType());
		Assert.assertEquals(carBodyStyleType, sedan.getType());
		Assert.assertEquals(carBodyStyleType, coupe.getType());

		// check the categories in the scheme
		Assert.assertTrue(schemeModelsByBodyStyle.getCategories().contains(convertible));
		Assert.assertTrue(schemeModelsByBodyStyle.getCategories().contains(coupe));
		Assert.assertTrue(schemeModelsByBodyStyle.getCategories().contains(sedan));

		// check the type of the categories
		Assert.assertEquals(carBodyStyleType, sedan.getType());
		Assert.assertEquals(carBodyStyleType, coupe.getType());
		Assert.assertEquals(carBodyStyleType, convertible.getType());

		System.out.println(verbaliserComponent.verbalise(schemeModelsByBodyStyle, euRentVocabulary));
	}

	// TODO: Implement as segmentation once supported after completing Categorization V2 implementation.
	// test hierarchical categorization schemes
	@Test
	public void testCarMovementsExample() {
		// The car movement concept
		Term carMovement = representationFactory.makeTerm(euRentVocabulary, "Car Movement");

		// The directional movement type
		CategorizationType directionalMovementType = categorizationFactory.createCategorizationType(euRentVocabulary,
				"Directional Movement Type", carMovement.getObjectType());
		Concept movementsByDirection = categorizationFactory.createCategorizationScheme(carMovement.getObjectType(),
				euRentVocabulary, "Movements by Direction");
		Category roundTripCarMovement = categorizationFactory.createCategory("Round-trip Car Movement",
				movementsByDirection, directionalMovementType, euRentVocabulary);
		Category oneWayCarMovement = categorizationFactory.createCategory("One-way Car Movement", movementsByDirection,
				directionalMovementType, euRentVocabulary);

		CategorizationType geographicalMovementType = categorizationFactory.createCategorizationType(euRentVocabulary,
				"Geographical Movement Type", oneWayCarMovement);
		Concept movementsByGeography = categorizationFactory.createCategorizationScheme(oneWayCarMovement,
				euRentVocabulary, "Movements by Geography");
		Category localCarMovement = categorizationFactory.createCategory("Local Car Movement", movementsByGeography,
				geographicalMovementType, euRentVocabulary);
		Category inCountryCarMovement = categorizationFactory.createCategory("In-country Car Movement",
				movementsByGeography, geographicalMovementType, euRentVocabulary);
		Category internationalCarMovement = categorizationFactory.createCategory("International Car Movement",
				movementsByGeography, geographicalMovementType, euRentVocabulary);

		// TODO write actual tests instead of print
		System.out.println(verbaliserComponent.verbalise(movementsByDirection, euRentVocabulary));
		System.out.println(verbaliserComponent.verbalise(movementsByGeography, euRentVocabulary));
	}

	@Test
	public void testHSbcProductTypeExample() {

		/**
		 * First part is creating the Categorization Scheme
		 */

		// The objecttype the categorization schemes are for
		Term product = representationFactory.makeTerm(euRentVocabulary, "Product");

		// the product type categorizations
		CategorizationType productType = categorizationFactory.createCategorizationType(euRentVocabulary,
				"Product Type", product.getObjectType());
		Concept productsByType = categorizationFactory.createCategorizationScheme(product.getObjectType(),
				euRentVocabulary, "Products By Type");
		Category depositProduct = categorizationFactory.createCategory("Deposit Product", productsByType, productType,
				euRentVocabulary);
		Category financeServiceProduct = categorizationFactory.createCategory("Finance Service (Loan) Product",
				productsByType, productType, euRentVocabulary);

		// Finance service limit type categorizations
		CategorizationType financeServiceLimitType = categorizationFactory.createCategorizationType(euRentVocabulary,
				"Finance Service Limit Type", financeServiceProduct);
		Concept productsByServiceLimitType = categorizationFactory.createCategorizationScheme(financeServiceProduct,
				euRentVocabulary, "Finance Service Products by Service Limit Type");
		Category revolvingLimitProduct = categorizationFactory.createCategory("Revolving Limit Product",
				productsByServiceLimitType, financeServiceLimitType, euRentVocabulary);
		Category nonRevolvingLimitProduct = categorizationFactory.createCategory("Non-Revolving Limit Product",
				productsByServiceLimitType, financeServiceLimitType, euRentVocabulary);

		// Finance service Type categorizations
		CategorizationType financeServiceType = categorizationFactory.createCategorizationType(euRentVocabulary,
				"Finance Service Type", financeServiceProduct);
		Concept financeServicesByType = categorizationFactory.createCategorizationScheme(financeServiceProduct,
				euRentVocabulary, "Finance Services By Type");
		Category leasing = categorizationFactory.createCategory("Leasing", financeServicesByType, financeServiceType,
				euRentVocabulary);
		Category factoring = categorizationFactory.createCategory("Factoring", financeServicesByType,
				financeServiceType, euRentVocabulary);
		Category mortgage = categorizationFactory.createCategory("Mortgage", financeServicesByType, financeServiceType,
				euRentVocabulary);
		Category creditFacility = categorizationFactory.createCategory("Credit Facility", financeServicesByType,
				financeServiceType, euRentVocabulary);

		// Secured Landing Indicator categorizations
		CategorizationType securedLandingIndicator = categorizationFactory.createCategorizationType(euRentVocabulary,
				"Secured Landing Indicator", financeServiceProduct);
		Concept productsByFinanceSecuredLandingIndicator = categorizationFactory.createCategorizationScheme(
				financeServiceProduct, euRentVocabulary, "Finance Service Products by Secured Landing Indicator");
		Category securedLendingProduct = categorizationFactory.createCategory("Secured Landing Product",
				productsByFinanceSecuredLandingIndicator, securedLandingIndicator, euRentVocabulary);

		// Financing Service by purpose categorizations
		CategorizationType financeServicePurpose = categorizationFactory.createCategorizationType(euRentVocabulary,
				"Finance Service Purpose", financeServiceProduct);
		Concept financeServicesByPurpose = categorizationFactory.createCategorizationScheme(financeServiceProduct,
				euRentVocabulary, "Finance Services by Purpose");
		Category assetAcquisitionFinancing = categorizationFactory.createCategory("Asset Acquisition Financing",
				financeServicesByPurpose, financeServicePurpose, euRentVocabulary);
		Category constructionFinancing = categorizationFactory.createCategory("Construction Financing",
				financeServicesByPurpose, financeServicePurpose, euRentVocabulary);

		// Asset Acquisition Financing Type categorizations
		CategorizationType assetAcquisitionFinancingType = categorizationFactory.createCategorizationType(
				euRentVocabulary, "Asset Acquisition Financing Type", assetAcquisitionFinancing);
		Concept assetAcquisitionFinancingByType = categorizationFactory.createCategorizationScheme(
				assetAcquisitionFinancing, euRentVocabulary, "Asset Acquisition Financing By Type");
		Category vehiclePurchasingFinancing = categorizationFactory.createCategory("Vehicle Purchase Financing",
				assetAcquisitionFinancingByType, assetAcquisitionFinancingType, euRentVocabulary);
		Category realPropertyPurchaseFinancing = categorizationFactory.createCategory(
				"Real Property Purchase Financing", assetAcquisitionFinancingByType, assetAcquisitionFinancingType,
				euRentVocabulary);

		System.out.println(verbaliserComponent.verbalise(productsByType, euRentVocabulary));
		System.out.println(verbaliserComponent.verbalise(productsByServiceLimitType, euRentVocabulary));
		System.out.println(verbaliserComponent.verbalise(financeServicesByType, euRentVocabulary));
		System.out.println(verbaliserComponent.verbalise(productsByFinanceSecuredLandingIndicator, euRentVocabulary));
		System.out.println(verbaliserComponent.verbalise(financeServicesByPurpose, euRentVocabulary));
		System.out.println(verbaliserComponent.verbalise(assetAcquisitionFinancingByType, euRentVocabulary));
	}

	@Test
	public void testClassifyingConceptTobeScheme() {
		// The car model concept
		Term carModel = representationFactory.makeTerm(euRentVocabulary, "Car Model");

		// The car body style categorization type
		CategorizationType carBodyStyleType = categorizationFactory.createCategorizationType(euRentVocabulary,
				"Car Body Style", carModel.getObjectType());
		categorizationFactory.createCategory("Sedan", carModel.getObjectType(), carBodyStyleType, euRentVocabulary);
		categorizationFactory.createCategory("Coupe", carModel.getObjectType(), carBodyStyleType, euRentVocabulary);
		categorizationFactory.createCategory("Convertible", carModel.getObjectType(), carBodyStyleType,
				euRentVocabulary);

		// check the dedicated object type
		Assert.assertEquals(carModel.getObjectType(), carBodyStyleType.getIsForConcept());
		System.out.println(verbaliserComponent.verbalise((Concept) carModel.getObjectType(), euRentVocabulary));

		// Assert that the concept being classified itself is the classification scheme
		Assert.assertEquals(true, carModel.getObjectType().isCategorizationScheme());
		Assert.assertEquals(carModel.getObjectType(), carModel.getObjectType().getConceptsCategorized().iterator()
				.next());
	}
}
