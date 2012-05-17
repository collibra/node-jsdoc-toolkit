package com.collibra.dgc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestSuggesterService extends AbstractServiceTest {
	private Vocabulary voc = null;

	@Before
	public void setUp() throws Exception {
		voc = createSampleVocabulary();
	}

	@Test
	public void testSuggestSynonymousFormsForCharacteristicForm() {

		Term proposition = representationFactory.makeTerm(voc, "Proposition");

		CharacteristicForm cf1 = representationFactory.makeCharacteristicForm(voc, proposition, "is false");
		CharacteristicForm cf2 = representationFactory.makeCharacteristicForm(voc, proposition, "is true");
		CharacteristicForm cf3 = representationFactory.makeCharacteristicForm(voc, proposition, "is possibly true");

		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());

		assertTrue(voc.getCharacteristicForms().contains(cf1));
		assertTrue(voc.getCharacteristicForms().contains(cf2));
		assertTrue(voc.getCharacteristicForms().contains(cf3));

		resetTransaction();

		List<CharacteristicForm> suggestedTerms = suggesterService.suggestSynonymousFormsForCharacteristicForm(cf1,
				"proposition");
		assertTrue(suggestedTerms.size() > 0);

		assertTrue(suggestedTerms.contains(cf2));
		assertTrue(suggestedTerms.contains(cf3));
		assertFalse(suggestedTerms.contains(cf1));
	}

	@Test
	public void testSuggestSynonymousFormsForBinaryFactTypeForm() {

		Term term1 = representationFactory.makeTerm(voc, "term1");
		Term term2 = representationFactory.makeTerm(voc, "term2");
		Term term3 = representationFactory.makeTerm(voc, "term3");

		BinaryFactTypeForm bftf1 = representationFactory.makeBinaryFactTypeForm(voc, term1, "role1", "corole1", term2);
		BinaryFactTypeForm bftf2 = representationFactory.makeBinaryFactTypeForm(voc, term1, "has", "of", term3);
		BinaryFactTypeForm bftf3 = representationFactory.makeBinaryFactTypeForm(voc, term2, "has", "of", term3);

		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());

		resetTransaction();

		List<BinaryFactTypeForm> suggestedTerms = suggesterService.suggestSynonymousFormsForBinaryFactTypeForm(bftf1,
				"term");
		assertTrue(suggestedTerms.size() > 0);

		assertTrue(suggestedTerms.contains(bftf2));
		assertTrue(suggestedTerms.contains(bftf3));
		assertFalse(suggestedTerms.contains(bftf1));
	}

	@Test
	public void testSuggestSynonymsForTerm() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term driver = representationFactory.makeTerm(voc, "Driver");
		Term synPerson1 = representationFactory.makeTerm(voc, "Person Synonym");
		Term synPerson2 = representationFactory.makeTerm(voc, "Person Synonym2");
		Term synPerson3 = representationFactory.makeTerm(voc, "Person Synonym3");

		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());

		assertTrue(voc.getTerms().contains(person));
		assertTrue(voc.getTerms().contains(driver));
		assertTrue(voc.getTerms().contains(synPerson1));
		assertTrue(voc.getTerms().contains(synPerson2));
		assertTrue(voc.getTerms().contains(synPerson3));

		resetTransaction();

		List<Term> suggestedTerms = suggesterService.suggestSynonymsForTerm(person, "Person");
		assertTrue(suggestedTerms.size() > 0);
		assertTrue(suggestedTerms.contains(synPerson1));
		assertTrue(suggestedTerms.contains(synPerson2));
		assertTrue(suggestedTerms.contains(synPerson3));
		assertFalse(suggestedTerms.contains(person));

	}

//	public void testSuggestConceptTypeTermsNameString() {
//		Term country = representationFactory.makeTerm(voc, "Country");
//		Term individualConcept = representationFactory.makeTerm(voc, "Individual Concept");
//
//		Name name = representationFactory.makeName(voc, "Swiss", country);
//
//		representationService.saveVocabulary(voc);
//		resetTransaction();
//
//		voc = representationService.findVocabularyByResourceId(voc.getResourceId());
//
//		assertTrue(voc.getNames().contains(name));
//
//		resetTransaction();
//		List<Term> suggestedTerms = suggesterService.suggestconcept.suggestConceptTypeTerms(name, "Individual Concept");
//
//		assertTrue(suggestedTerms.contains(individualConcept));
//		assertTrue(suggestedTerms.size() > 1);
//
//		for (Term suggestTerm : suggestedTerms) {
//			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaObjectType()));
//			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaBinaryFactType()));
//			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaCharacteristic()));
//		}
//	}

	@Test
	public void testSuggestConceptTypeTermsCharacteristicFormString() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term characteristic = representationFactory.makeTerm(voc, "Characteristic");
		characteristic.getObjectType().setGeneralConcept(meaningService.findMetaCharacteristic());

		CharacteristicForm cf = representationFactory.makeCharacteristicForm(voc, person, "is married");

		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());

		assertTrue(voc.getCharacteristicForms().contains(cf));

		resetTransaction();
		cf = representationService.findCharacteristicFormByResourceId(cf.getId());
		characteristic = representationService.findTermByResourceId(characteristic.getId());
		List<Term> suggestedTerms = suggesterService.suggestConceptTypeTerms(cf, "Characteristic");

		assertFalse(suggestedTerms.contains(cf));
		assertTrue(suggestedTerms.contains(characteristic));
		assertTrue(suggestedTerms.size() > 0);

		for (Term suggestTerm : suggestedTerms) {
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaObjectType()));
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaBinaryFactType()));
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaIndividualConcept()));
		}
	}

	@Test
	public void testSuggestConceptTypeTermsBinaryFactTypeFormString() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term driver = representationFactory.makeTerm(voc, "Driver");
		Term Car = representationFactory.makeTerm(voc, "Car");
		Term bft = representationFactory.makeTerm(voc, "Binary Fact Type");
		bft.getObjectType().setGeneralConcept(meaningService.findMetaBinaryFactType());

		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(voc, person, "drives", "driven by", Car);

		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());

		assertTrue(voc.getBinaryFactTypeForms().contains(bftf));

		resetTransaction();
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		List<Term> suggestedTerms = suggesterService.suggestConceptTypeTerms(bftf, "Binary Fact Type");

		assertFalse(suggestedTerms.contains(driver));
		assertFalse(suggestedTerms.contains(bftf));
		assertTrue(suggestedTerms.contains(bft));
		assertTrue(suggestedTerms.size() > 0);

		for (Term suggestTerm : suggestedTerms) {
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaObjectType()));
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaCharacteristic()));
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaIndividualConcept()));
		}
	}

	//
	// public void testSuggestConceptTypeTermsRepresentationString() {
	// fail("Not yet implemented");
	// }
	@Test
	public void testSuggestConceptTypeTermsTermString() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term driver = representationFactory.makeTerm(voc, "Driver");
		Term Concept = representationFactory.makeTerm(voc, "Concept");
		Term Car = representationFactory.makeTerm(voc, "Car");
		Term objectType = representationFactory.makeTerm(voc, "Object Type");
		
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(voc, person, "drives", "driven by", Car);

		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());
		assertTrue(voc.getTerms().contains(person));
		assertTrue(voc.getTerms().contains(driver));
		assertTrue(voc.getTerms().contains(objectType));
		assertTrue(voc.getBinaryFactTypeForms().contains(bftf));

		resetTransaction();
		person = representationService.findTermByResourceId(person.getId());
		List<Term> suggestedTerms = suggesterService.suggestConceptTypeTerms(person, "Concept");

		assertTrue(suggestedTerms.size() > 0);

		assertFalse(suggestedTerms.contains(driver));
		assertFalse(suggestedTerms.contains(bftf));
		assertFalse(suggestedTerms.contains(Concept));

		for (Term suggestTerm : suggestedTerms) {
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaBinaryFactType()));
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaCharacteristic()));
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaIndividualConcept()));
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaObjectType()));
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaCategory()));
			assertFalse(suggestTerm.getObjectType().equals(meaningService.getMetaStatusType()));
			assertFalse(suggestTerm.getObjectType().equals(meaningService.findMetaThing()));
		}

	}

	@Test
	public void testSuggestSpecializedTerms() {

		Term person = representationFactory.makeTerm(voc, "Person");
		Term driver = representationFactory.makeTerm(voc, "Driver");
		Term objectType1 = representationFactory.makeTerm(voc, "Object Type1");
		Term objectType = representationFactory.makeTerm(voc, "Object Type");
		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());
		assertEquals(4, voc.getTerms().size());
		assertTrue(voc.getTerms().contains(person));
		assertTrue(voc.getTerms().contains(driver));
		assertTrue(voc.getTerms().contains(objectType));
		assertTrue(voc.getTerms().contains(objectType1));

		resetTransaction();
		Vocabulary sbvrVoc = representationService.findSbvrVocabulary();
		List<Term> suggestedTerms = suggesterService.suggestSpecializedTerms(person, "object");
		assertFalse(suggestedTerms.contains(driver));
		assertTrue(suggestedTerms.contains(objectType));
		assertFalse(sbvrVoc.getTerms().contains(objectType));

		assertTrue(suggestedTerms.contains(objectType1));
		assertFalse(sbvrVoc.getTerms().contains(objectType1));
		for (Vocabulary incVoc : sbvrVoc.getIncorporatedVocabularies()) {
			assertFalse(incVoc.getTerms().contains(objectType));
			assertFalse(incVoc.getTerms().contains(objectType1));
		}

	}

	@Test
	public void testSuggestSpecializedBinaryFactTypeForms() {

		Term term1 = representationFactory.makeTerm(voc, "term1");
		Term term2 = representationFactory.makeTerm(voc, "term2");
		Term concept = representationFactory.makeTerm(voc, "concept");
		Term instance = representationFactory.makeTerm(voc, "instance");
		Term definition = representationFactory.makeTerm(voc, "definition");

		BinaryFactTypeForm bftf1 = representationFactory.makeBinaryFactTypeForm(voc, term1, "role1", "corole1", term2);
		BinaryFactTypeForm bftf2 = representationFactory.makeBinaryFactTypeForm(voc, concept, "has", "of", instance);
		BinaryFactTypeForm bftf3 = representationFactory.makeBinaryFactTypeForm(voc, concept, "has", "of", definition);

		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());

		assertTrue(voc.getBinaryFactTypeForms().contains(bftf1));
		assertTrue(voc.getBinaryFactTypeForms().contains(bftf2));
		assertTrue(voc.getBinaryFactTypeForms().contains(bftf3));

		resetTransaction();
		Vocabulary sbvrVoc = representationService.findSbvrVocabulary();
		List<BinaryFactTypeForm> suggestedBFTFs = suggesterService.suggestSpecializedBinaryFactTypeForms(bftf1,
				"concept");
		assertTrue(suggestedBFTFs.contains(bftf2));
		assertTrue(suggestedBFTFs.contains(bftf3));
		assertFalse(sbvrVoc.getBinaryFactTypeForms().contains(bftf2));
		assertFalse(sbvrVoc.getBinaryFactTypeForms().contains(bftf3));
		for (Vocabulary incVoc : sbvrVoc.getIncorporatedVocabularies()) {
			assertFalse(incVoc.getBinaryFactTypeForms().contains(bftf2));
			assertFalse(incVoc.getBinaryFactTypeForms().contains(bftf3));
		}

	}

	@Test
	public void testSuggestSpecializedCharacteristicForms() {

		Term proposition = representationFactory.makeTerm(voc, "Proposition");

		CharacteristicForm cf1 = representationFactory.makeCharacteristicForm(voc, proposition, "is false");
		CharacteristicForm cf2 = representationFactory.makeCharacteristicForm(voc, proposition, "is true");
		CharacteristicForm cf3 = representationFactory.makeCharacteristicForm(voc, proposition, "is possibly true");

		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());

		assertTrue(voc.getCharacteristicForms().contains(cf1));
		assertTrue(voc.getCharacteristicForms().contains(cf2));
		assertTrue(voc.getCharacteristicForms().contains(cf3));

		resetTransaction();
		Vocabulary sbvrVoc = representationService.findSbvrVocabulary();
		List<CharacteristicForm> suggestedCFs = suggesterService.suggestSpecializedCharacteristicForms(cf1,
				"proposition");
		assertTrue(suggestedCFs.contains(cf2));
		assertTrue(suggestedCFs.contains(cf3));
		assertFalse(sbvrVoc.getCharacteristicForms().contains(cf2));
		assertFalse(sbvrVoc.getCharacteristicForms().contains(cf3));
		for (Vocabulary incVoc : sbvrVoc.getIncorporatedVocabularies()) {
			assertFalse(incVoc.getCharacteristicForms().contains(cf2));
			assertFalse(incVoc.getCharacteristicForms().contains(cf3));
		}

	}

	@Test
	public void testSuggestGeneralTerms() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term driver = representationFactory.makeTerm(voc, "Driver");
		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());
		assertEquals(2, voc.getTerms().size());
		assertTrue(voc.getTerms().contains(person));
		assertTrue(voc.getTerms().contains(driver));

		resetTransaction();
		person = representationService.findTermByResourceId(person.getId());
		Vocabulary meaningVoc = representationService.findSbvrMeaningAndRepresentationVocabulary();
		Set<Vocabulary> includeVocs = new HashSet<Vocabulary>();
		List<Term> suggestedTerms = suggesterService.suggestGeneralTerms(person, "Object T");
		assertTrue(suggestedTerms.size() > 0);
		assertFalse(suggestedTerms.contains(driver));
		assertTrue(suggestedTerms.get(0).getVocabulary().equals(meaningVoc));

	}

	@Test
	public void testSuggestGeneralBinaryFactTypeForms() {
		Term term1 = representationFactory.makeTerm(voc, "term1");
		Term term2 = representationFactory.makeTerm(voc, "term2");
		Term concept = representationFactory.makeTerm(voc, "Concept");
		Term instance = representationFactory.makeTerm(voc, "Instance");
		Term definition = representationFactory.makeTerm(voc, "Definition");

		BinaryFactTypeForm bftf1 = representationFactory.makeBinaryFactTypeForm(voc, term1, "role1", "corole1", term2);
		BinaryFactTypeForm bftf2 = representationFactory.makeBinaryFactTypeForm(voc, concept, "has", "of", instance);
		BinaryFactTypeForm bftf3 = representationFactory.makeBinaryFactTypeForm(voc, concept, "has", "of", definition);

		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());

		assertTrue(voc.getBinaryFactTypeForms().contains(bftf1));
		assertTrue(voc.getBinaryFactTypeForms().contains(bftf2));
		assertTrue(voc.getBinaryFactTypeForms().contains(bftf3));

		resetTransaction();

		List<BinaryFactTypeForm> suggestedBFTFs = suggesterService.suggestGeneralBinaryFactTypeForms(bftf1, "concept");
		assertTrue(suggestedBFTFs.contains(bftf2));
		assertTrue(suggestedBFTFs.contains(bftf3));

	}

	@Test
	public void testSuggestGeneralCharacteristicForms() {
		Term proposition = representationFactory.makeTerm(voc, "proposition");

		CharacteristicForm cf1 = representationFactory.makeCharacteristicForm(voc, proposition, "is false");
		CharacteristicForm cf2 = representationFactory.makeCharacteristicForm(voc, proposition, "is true");
		CharacteristicForm cf3 = representationFactory.makeCharacteristicForm(voc, proposition, "is possibly true");

		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());

		assertTrue(voc.getCharacteristicForms().contains(cf1));
		assertTrue(voc.getCharacteristicForms().contains(cf2));
		assertTrue(voc.getCharacteristicForms().contains(cf3));

		resetTransaction();
		List<CharacteristicForm> suggestedCFs = suggesterService.suggestGeneralCharacteristicForms(cf1, "proposition");
		assertTrue(suggestedCFs.contains(cf2));
		assertTrue(suggestedCFs.contains(cf3));

	}

	@Test
	public void testSuggestBinaryFactTypeSpecializations() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term car = representationFactory.makeTerm(voc, "Car");
		BinaryFactTypeForm personDrivesCar = representationFactory.makeBinaryFactTypeForm(voc, person, "drives",
				"driven by", car);
		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());
		Term taxiDriver = representationFactory.makeTerm(voc, "Taxi Driver");
		Term taxi = representationFactory.makeTerm(voc, "Taxi");
		BinaryFactTypeForm taxiDriverDrivesTaxi = representationFactory.makeBinaryFactTypeForm(voc, taxiDriver,
				"drives", "driven by", taxi);
		car = representationService.findTermByResourceId(car.getId());
		person = representationService.findTermByResourceId(person.getId());
		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());
		taxiDriver.getObjectType().setGeneralConcept(person.getObjectType());
		taxi.getObjectType().setGeneralConcept(car.getObjectType());

		voc = representationService.saveVocabulary(voc);

		resetTransaction();

		// test the normal case
		List<BinaryFactTypeForm> suggestions = suggesterService
				.suggestSpecializableBinaryFactTypeForms(personDrivesCar);
		assertEquals(1, suggestions.size());
		assertEquals(taxiDriverDrivesTaxi, suggestions.get(0));

		// add something unrelated and make sure it doesn't show up
		voc = representationService.findVocabularyByResourceId(voc.getId());
		Term cowboy = representationFactory.makeTerm(voc, "Cowboy");
		Term horse = representationFactory.makeTerm(voc, "Horse");
		BinaryFactTypeForm cowboyCatchesHorse = representationFactory.makeBinaryFactTypeForm(voc, cowboy, "catches",
				"caught by", horse);

		voc = representationService.saveVocabulary(voc);

		// we still only expect the same results for person drives car
		resetTransaction();
		suggestions = suggesterService.suggestSpecializableBinaryFactTypeForms(personDrivesCar);
		taxiDriverDrivesTaxi = representationService.findBinaryFactTypeFormByResourceId(taxiDriverDrivesTaxi
				.getId());
		assertEquals(1, suggestions.size());
		assertEquals(taxiDriverDrivesTaxi, suggestions.get(0));

		// and we expect nothing for the cowboy catching horses
		suggestions = suggesterService.suggestSpecializableBinaryFactTypeForms(cowboyCatchesHorse);
		assertEquals(0, suggestions.size());

		// and now we specialize person drives car with taxiDriverDrivesTaxi
		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());
		taxiDriverDrivesTaxi.getBinaryFactType().setGeneralConcept(personDrivesCar.getBinaryFactType());
		meaningService.saveAndCascade(personDrivesCar.getBinaryFactType());

		resetTransaction();
		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());
		suggestions = suggesterService.suggestSpecializableBinaryFactTypeForms(personDrivesCar);
		assertEquals(0, suggestions.size());

	}

	/**
	 * Specialize a bft that shares one concept with its general bft
	 */
	@Test
	public void testSuggestSpecializableBinaryFactTypeFormsWithSharedConcept() {
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

		taxiDriver.getObjectType().setGeneralConcept(person.getObjectType());
		meaningService.saveAndCascade(taxiDriver.getObjectType());
		voc = representationService.saveVocabulary(voc);

		resetTransaction();

		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());
		taxiDriverDrivesCar = representationService.findBinaryFactTypeFormByResourceId(taxiDriverDrivesCar
				.getId());
		List<BinaryFactTypeForm> suggestions = suggesterService
				.suggestSpecializableBinaryFactTypeForms(personDrivesCar);
		assertEquals(1, suggestions.size());
		assertEquals(taxiDriverDrivesCar, suggestions.get(0));
	}

	/**
	 * Generalize a bft that shares one concept with its general bft
	 */
	@Test
	public void testSuggestGeneralizalbleBinaryFactTypeFormsWithSharedConcept() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term car = representationFactory.makeTerm(voc, "Car");
		BinaryFactTypeForm personDrivesCar = representationFactory.makeBinaryFactTypeForm(voc, person, "drives",
				"driven by", car);
		representationService.saveVocabulary(voc);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());
		Term taxiDriver = representationFactory.makeTerm(voc, "Taxi Driver");
		representationService.save(taxiDriver);
		car = representationService.findTermByResourceId(car.getId());
		person = representationService.findTermByResourceId(person.getId());
		BinaryFactTypeForm taxiDriverDrivesCar = representationFactory.makeBinaryFactTypeForm(voc, taxiDriver,
				"drives", "driven by", car);

		taxiDriver.getObjectType().setGeneralConcept(person.getObjectType());
		meaningService.saveAndCascade(taxiDriver.getObjectType());
		voc = representationService.saveVocabulary(voc);

		resetTransaction();

		personDrivesCar = representationService.findBinaryFactTypeFormByResourceId(personDrivesCar.getId());
		taxiDriverDrivesCar = representationService.findBinaryFactTypeFormByResourceId(taxiDriverDrivesCar
				.getId());
		List<BinaryFactTypeForm> suggestions = suggesterService
				.suggestGeneralizableBinaryFactTypeForms(taxiDriverDrivesCar);
		assertEquals(1, suggestions.size());
		assertEquals(personDrivesCar, suggestions.get(0));
	}

	/**
	 * Specialize a bft that shares one concept with its general bft
	 */
	@Test
	public void testsuggestFactTypes() {
		Term person = representationFactory.makeTerm(voc, "Person");
		Term car = representationFactory.makeTerm(voc, "Car");
		Term otTerm = representationService.findPreferredTermInAllIncorporatedVocabularies(
				meaningService.findMetaObjectType(), voc);

		BinaryFactTypeForm personDrivesCar = representationFactory.makeBinaryFactTypeForm(voc, otTerm, "drives",
				"driven by", otTerm);
		representationService.saveVocabulary(voc);
		resetTransaction();
		person = representationService.findTermByResourceId(person.getId());
		Collection<BinaryFactTypeForm> bforms = suggesterService.suggestFactTypes(otTerm, null, voc);
		assertEquals(1, bforms.size());
	}
}
