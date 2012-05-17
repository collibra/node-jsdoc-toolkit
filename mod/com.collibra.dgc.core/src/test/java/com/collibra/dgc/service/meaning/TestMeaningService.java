package com.collibra.dgc.service.meaning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.CircularTaxonomyException;
import com.collibra.dgc.core.exceptions.InconsistentTaxonomyException;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestMeaningService extends AbstractServiceTest {

	private Vocabulary voc = null;

	@Before
	public void setUp() throws Exception {
		voc = createSampleVocabulary();
	}

	@After
	public void onTearDown() {
		voc = null;
	}

	// see bug #4842
	// @Test
	// public void testFindSpecializedConcepts() {
	// ObjectType personOT = meaningFactory.makeObjectType(sc);
	// ObjectType employeeOT = meaningFactory.makeObjectType(sc);
	// ObjectType goldenEmployeeOT = meaningFactory.makeObjectType(sc);
	// employeeOT.setGeneralConcept(personOT);
	// goldenEmployeeOT.setGeneralConcept(employeeOT);
	// communityService.commit(sc);
	// resetTransaction();
	// personOT = meaningService.findObjectTypeByResourceId(personOT.getResourceId());
	// assertEquals(2, personOT.getAllSpecializedConcepts().size());
	// }

	@Test
	public void testMetaModel() {
		ObjectType ma = meaningService.findMetaBusinessAsset();
		assertNotNull(ma);
		assertEquals("Business Asset", ma.getTerms().iterator().next().getSignifier());
		assertEquals(meaningService.findMetaObjectType(), ma.getGeneralConcept());

		ObjectType bt = meaningService.findMetaBusinessTerm();
		assertNotNull(bt);
		assertEquals("Business Term", bt.getTerms().iterator().next().getSignifier());
		assertEquals(ma, bt.getGeneralConcept());

		ObjectType ta = meaningService.findMetaTechnicalAsset();
		assertNotNull(ta);
		assertEquals("Technical Asset", ta.getTerms().iterator().next().getSignifier());
		assertEquals(meaningService.findMetaObjectType(), ta.getGeneralConcept());

		ObjectType c = meaningService.findMetaCode();
		assertNotNull(c);
		assertEquals("Code Value", c.getTerms().iterator().next().getSignifier());
		assertEquals(ta, c.getGeneralConcept());

		ObjectType qa = meaningService.findMetaQualityAsset();
		assertNotNull(qa);
		assertEquals("Quality Asset", qa.getTerms().iterator().next().getSignifier());
		assertEquals(meaningService.findMetaObjectType(), qa.getGeneralConcept());
	}

	@Test
	public void testFindTaxonomicalParentConcepts() {
		ObjectType personOT = meaningFactory.makeObjectType();
		ObjectType employeeOT = meaningFactory.makeObjectType();
		ObjectType goldenEmployeeOT = meaningFactory.makeObjectType();
		employeeOT.setGeneralConcept(personOT);
		goldenEmployeeOT.setGeneralConcept(employeeOT);
		meaningService.saveAndCascade(personOT);
		meaningService.saveAndCascade(employeeOT);
		meaningService.saveAndCascade(goldenEmployeeOT);
		resetTransaction();
		List<Concept> generalConcepts = meaningService.findAllSpecializedConcepts(personOT);
		assertEquals(2, generalConcepts.size());
		assertTrue(generalConcepts.contains(employeeOT));
		assertTrue(generalConcepts.contains(goldenEmployeeOT));

	}

	/**
	 * Test generalise concept relation that is persisted via create of the general concept
	 */
	@Test
	public void testGeneraliseConceptAndPersistViaParent() {
		ObjectType personOT = meaningFactory.makeObjectType();
		personOT = meaningService.saveAndCascade(personOT);

		resetTransaction();

		personOT = meaningService.findObjectTypeByResourceId(personOT.getId());
		Concept personGeneralConcept = personOT.getGeneralConcept();
		ObjectType thingOT = meaningService.findMetaThing();
		assertEquals(thingOT, personGeneralConcept);
		ObjectType beingOT = meaningFactory.makeObjectType();
		personOT.setGeneralConcept(beingOT);
		// persist via create of general concept
		beingOT = meaningService.saveAndCascade(beingOT);

		resetTransaction();

		// check general concept relation after create
		ObjectType lookedupBeingOT = meaningService.findObjectTypeByResourceId(beingOT.getId());
		ObjectType lookedupPersonOT = meaningService.findObjectTypeByResourceId(personOT.getId());
		assertEquals(lookedupBeingOT, lookedupPersonOT.getGeneralConcept());

		// check specialized concept relation
		assertTrue(meaningService.findAllSpecializedConcepts(lookedupBeingOT).contains(lookedupPersonOT));
	}

	/**
	 * Test generalise concept relation that is persisted via create of the specialized concept
	 */
	@Test
	public void testGeneraliseConceptAndPersistViaChild() {
		ObjectType personOT = meaningFactory.makeObjectType();
		meaningService.saveAndCascade(personOT);

		resetTransaction();

		personOT = meaningService.findObjectTypeByResourceId(personOT.getId());

		Concept personGeneralConcept = personOT.getGeneralConcept();
		ObjectType thingOT = meaningService.findMetaThing();
		assertEquals(thingOT, personGeneralConcept);
		ObjectType beingOT = meaningFactory.makeObjectType();
		personOT.setGeneralConcept(beingOT);
		// persist via create of general concept
		beingOT = meaningService.saveAndCascade(beingOT);
		personOT = meaningService.saveAndCascade(personOT);
		resetTransaction();

		// check general concept relation after create
		ObjectType lookedupBeingOT = meaningService.findObjectTypeByResourceId(beingOT.getId());
		ObjectType lookedupPersonOT = meaningService.findObjectTypeByResourceId(personOT.getId());
		assertEquals(lookedupBeingOT, lookedupPersonOT.getGeneralConcept());

		// check specialized concept relation
		assertTrue(meaningService.findAllSpecializedConcepts(lookedupBeingOT).contains(lookedupPersonOT));
	}

	@Test
	public void testDirectCircularTaxonomy() {
		ObjectType personOT = meaningFactory.makeObjectType();
		personOT.setGeneralConcept(personOT);
		try {
			meaningService.saveAndCascade(personOT);
			fail();
		} catch (CircularTaxonomyException e) {
			// we expect this
			rollback();
		}
	}

	@Test
	public void testOneLevelCircularTaxonomy() {
		ObjectType personOT = meaningFactory.makeObjectType();
		ObjectType manOT = meaningFactory.makeObjectType();
		manOT.setGeneralConcept(personOT);
		personOT.setGeneralConcept(manOT);
		try {
			meaningService.saveAndCascade(personOT);
			fail();
		} catch (CircularTaxonomyException e) {
			// we expect this
			rollback();
		}
	}

	/**
	 * Generalizing a bft that goes through the normal case
	 */
	@Test
	public void testGeneralizeBinaryFactType() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term car = representationFactory.makeTerm(voc, "Car");
		BinaryFactTypeForm personDrivesCar = representationFactory.makeBinaryFactTypeForm(voc, person, "drives",
				"driven by", car);
		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());
		Term taxiDriver = representationFactory.makeTerm(voc, "Taxi Driver");
		representationService.save(taxiDriver);
		Term taxi = representationFactory.makeTerm(voc, "Taxi");
		representationService.save(taxi);
		BinaryFactTypeForm taxiDriverDrivesTaxi = representationFactory.makeBinaryFactTypeForm(voc, taxiDriver,
				"drives", "driven by", taxi);
		representationService.save(taxiDriverDrivesTaxi);
		resetTransaction();

		taxiDriver = representationService.findTermByResourceId(taxiDriver.getId());
		taxi = representationService.findTermByResourceId(taxi.getId());
		car = representationService.findTermByResourceId(car.getId());
		person = representationService.findTermByResourceId(person.getId());
		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());
		taxiDriverDrivesTaxi = representationService.findBinaryFactTypeFormByResourceId(taxiDriverDrivesTaxi
				.getId());

		taxiDriver.getObjectType().setGeneralConcept(person.getObjectType());
		representationService.save(taxiDriver);
		taxi.getObjectType().setGeneralConcept(car.getObjectType());
		representationService.save(taxi);
		taxiDriverDrivesTaxi.getBinaryFactType().setGeneralConcept(personDrivesCar.getBinaryFactType());
		representationService.save(taxiDriverDrivesTaxi);
		resetTransaction();

		// test some inheritance properties
		taxiDriverDrivesTaxi = representationService.findBinaryFactTypeFormByResourceId(taxiDriverDrivesTaxi
				.getId());
		BinaryFactType tddtBft = taxiDriverDrivesTaxi.getBinaryFactType();

		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());
		BinaryFactType pdcBft = personDrivesCar.getBinaryFactType();
		person = representationService.findTermByResourceId(person.getId());
		car = representationService.findTermByResourceId(car.getId());
		taxiDriver = representationService.findTermByResourceId(taxiDriver.getId());
		taxi = representationService.findTermByResourceId(taxi.getId());

		assertEquals(pdcBft, tddtBft.getGeneralConcept());
		assertEquals(person.getObjectType(), pdcBft.getHeadFactTypeRole().getObjectType());
		assertEquals(car.getObjectType(), pdcBft.getTailFactTypeRole().getObjectType());

		assertEquals(taxiDriver.getObjectType(), tddtBft.getHeadFactTypeRole().getObjectType());
		assertEquals(taxi.getObjectType(), tddtBft.getTailFactTypeRole().getObjectType());

		assertEquals(person.getObjectType(), taxiDriver.getObjectType().getGeneralConcept());
		assertEquals(car.getObjectType(), taxi.getObjectType().getGeneralConcept());

	}

	/**
	 * Generalizing a bft that shares one concept with its general bft
	 */
	@Test
	public void testGeneralizeBinaryFactTypeWithSharedConcept() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term car = representationFactory.makeTerm(voc, "Car");
		BinaryFactTypeForm personDrivesCar = representationFactory.makeBinaryFactTypeForm(voc, person, "drives",
				"driven by", car);
		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());
		person = representationService.findTermByResourceId(person.getId());
		car = representationService.findTermByResourceId(car.getId());

		Term taxiDriver = representationFactory.makeTerm(voc, "Taxi Driver");
		representationService.save(taxiDriver);
		BinaryFactTypeForm taxiDriverDrivesCar = representationFactory.makeBinaryFactTypeForm(voc, taxiDriver,
				"drives", "driven by", car);
		representationService.save(taxiDriverDrivesCar);

		resetTransaction();
		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());

		taxiDriver = representationService.findTermByResourceId(taxiDriver.getId());
		taxiDriver.getObjectType().setGeneralConcept(person.getObjectType());
		representationService.save(taxiDriver);

		taxiDriverDrivesCar = representationService.findBinaryFactTypeFormByResourceId(taxiDriverDrivesCar
				.getId());
		taxiDriverDrivesCar.getBinaryFactType().setGeneralConcept(personDrivesCar.getBinaryFactType());
		representationService.save(taxiDriverDrivesCar);

		resetTransaction();
		// test some inheritance properties
		taxiDriverDrivesCar = representationService.findBinaryFactTypeFormByResourceId(taxiDriverDrivesCar
				.getId());
		BinaryFactType tddcBft = taxiDriverDrivesCar.getBinaryFactType();

		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());
		BinaryFactType pdcBft = personDrivesCar.getBinaryFactType();
		person = representationService.findTermByResourceId(person.getId());
		car = representationService.findTermByResourceId(car.getId());
		taxiDriver = representationService.findTermByResourceId(taxiDriver.getId());

		assertEquals(pdcBft, tddcBft.getGeneralConcept());
		assertEquals(person.getObjectType(), pdcBft.getHeadFactTypeRole().getObjectType());
		assertEquals(car.getObjectType(), pdcBft.getTailFactTypeRole().getObjectType());

		assertEquals(taxiDriver.getObjectType(), tddcBft.getHeadFactTypeRole().getObjectType());
		assertEquals(car.getObjectType(), tddcBft.getTailFactTypeRole().getObjectType());

		assertEquals(person.getObjectType(), taxiDriver.getObjectType().getGeneralConcept());

	}

	/**
	 * Generalizing a bft that should be compatible but has head and tail reversed
	 */
	@Test
	public void testGeneralizeBinaryFactTypeReverse() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term car = representationFactory.makeTerm(voc, "Car");
		BinaryFactTypeForm personDrivesCar = representationFactory.makeBinaryFactTypeForm(voc, person, "drives",
				"driven by", car);
		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());
		Term taxiDriver = representationFactory.makeTerm(voc, "Taxi Driver");
		representationService.save(taxiDriver);
		Term taxi = representationFactory.makeTerm(voc, "Taxi");
		representationService.save(taxi);
		BinaryFactTypeForm taxiDrivenByTaxiDriver = representationFactory.makeBinaryFactTypeForm(voc, taxi,
				"driven by", "drives", taxiDriver);
		representationService.save(taxiDrivenByTaxiDriver);
		car = representationService.findTermByResourceId(car.getId());
		person = representationService.findTermByResourceId(person.getId());
		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());

		taxiDriver.getObjectType().setGeneralConcept(person.getObjectType());
		representationService.save(taxiDriver);
		taxi.getObjectType().setGeneralConcept(car.getObjectType());
		representationService.save(taxi);
		taxiDrivenByTaxiDriver.getBinaryFactType().setGeneralConcept(personDrivesCar.getBinaryFactType());
		representationService.save(taxiDrivenByTaxiDriver);
		resetTransaction();
		// test some inheritance properties
		taxiDrivenByTaxiDriver = representationService.findBinaryFactTypeFormByResourceId(taxiDrivenByTaxiDriver
				.getId());
		BinaryFactType tdbtdBft = taxiDrivenByTaxiDriver.getBinaryFactType();

		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());
		BinaryFactType pdcBft = personDrivesCar.getBinaryFactType();

		person = representationService.findTermByResourceId(person.getId());
		car = representationService.findTermByResourceId(car.getId());
		taxi = representationService.findTermByResourceId(taxi.getId());
		taxiDriver = representationService.findTermByResourceId(taxiDriver.getId());

		assertEquals(pdcBft, tdbtdBft.getGeneralConcept());
		assertEquals(person.getObjectType(), pdcBft.getHeadFactTypeRole().getObjectType());
		assertEquals(car.getObjectType(), pdcBft.getTailFactTypeRole().getObjectType());

		assertEquals(taxiDriver.getObjectType(), tdbtdBft.getTailFactTypeRole().getObjectType());
		assertEquals(taxi.getObjectType(), tdbtdBft.getHeadFactTypeRole().getObjectType());

		assertEquals(person.getObjectType(), taxiDriver.getObjectType().getGeneralConcept());
		assertEquals(car.getObjectType(), taxi.getObjectType().getGeneralConcept());
	}

	/**
	 * We'll try to create a bft generalization which would be inconsistent.
	 */
	@Test
	public void testInvalidGeneralizeBinaryFactType() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term car = representationFactory.makeTerm(voc, "Car");
		BinaryFactTypeForm personDrivesCar = representationFactory.makeBinaryFactTypeForm(voc, person, "drives",
				"driven by", car);
		voc = representationService.saveVocabulary(voc);
		resetTransaction();
		voc = representationService.findVocabularyByResourceId(voc.getId());
		Term taxiDriver = representationFactory.makeTerm(voc, "Taxi Driver");
		Term bike = representationFactory.makeTerm(voc, "Bike");
		BinaryFactTypeForm taxiDriverDrivesTaxi = representationFactory.makeBinaryFactTypeForm(voc, taxiDriver,
				"drives", "driven by", bike);
		car = representationService.findTermByResourceId(car.getId());
		person = representationService.findTermByResourceId(person.getId());
		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());
		taxiDriver.getObjectType().setGeneralConcept(person.getObjectType());
		taxiDriverDrivesTaxi.getBinaryFactType().setGeneralConcept(personDrivesCar.getBinaryFactType());
		try {
			representationService.saveVocabulary(voc);
			fail();
		} catch (InconsistentTaxonomyException e) {
			// we expect this
			rollback();
		}
	}

	/**
	 * #8843. Concept.getSpecializedConcepts() is empty while it shouldnt
	 */
	@Test
	public void testBug8843() {
		ObjectType parentOT = meaningFactory.makeObjectType();
		ObjectType child1OT = meaningFactory.makeObjectType();
		ObjectType child2OT = meaningFactory.makeObjectType();
		child1OT.setGeneralConcept(parentOT);
		child2OT.setGeneralConcept(parentOT);
		meaningService.saveAndCascade(parentOT);
		meaningService.saveAndCascade(child1OT);
		meaningService.saveAndCascade(child2OT);
		resetTransaction();

		parentOT = meaningService.findObjectTypeByResourceId(parentOT.getId());
		Assert.assertEquals(2, meaningService.findSpecializedConcepts(parentOT).size());

		child1OT = meaningService.findObjectTypeByResourceId(child1OT.getId());
		Assert.assertTrue(meaningService.findSpecializedConcepts(parentOT).contains(child1OT));

		child2OT = meaningService.findObjectTypeByResourceId(child1OT.getId());
		Assert.assertTrue(meaningService.findSpecializedConcepts(parentOT).contains(child2OT));
	}

}
