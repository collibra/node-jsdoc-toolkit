package com.collibra.dgc.service.characteristic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCharacteristicFormsInManager extends AbstractServiceTest {

	@Test
	public void testCreateCharacteristicFormInEmptyVocabulary() throws Exception {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");

		communityService.save(sp);
		resetTransaction();

		Vocabulary createdVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(createdVocabulary);
		assertNotNull(createdVocabulary.getId());

		Characteristic characteristic = meaningFactory.makeCharacteristic();
		meaningService.saveAndCascade(characteristic);
		Term termPerson = representationFactory.makeTerm(createdVocabulary, "Person");
		representationService.save(termPerson);
		CharacteristicForm characteristicForm1 = representationFactory.makeCharacteristicForm(createdVocabulary,
				termPerson, "is married", characteristic);

		CharacteristicForm createdCharacteristicForm = representationService
				.saveCharacteristicForm(characteristicForm1);
		assertNotNull(createdCharacteristicForm);
		assertNotNull(createdCharacteristicForm.getId());
		Vocabulary latestVocabulary = createdCharacteristicForm.getVocabulary();

		Term termHuman = representationFactory.makeTerm(createdVocabulary, "Human");
		representationService.save(termHuman);
		CharacteristicForm characteristicForm2 = representationFactory.makeCharacteristicForm(latestVocabulary,
				termHuman, "is getting married");
		characteristicForm2.setRole("is getting married again");

		createdCharacteristicForm = representationService.saveCharacteristicForm(characteristicForm2);
		assertNotNull(createdCharacteristicForm);
		assertNotNull(createdCharacteristicForm.getId());
	}

	@Test
	public void testCreateCharacteristicFormInNewVocabulary() throws Exception {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");

		Characteristic characteristic = meaningFactory.makeCharacteristic();

		Term termPerson = representationFactory.makeTerm(vocabulary, "Person");

		CharacteristicForm characteristicForm1 = representationFactory.makeCharacteristicForm(vocabulary, termPerson,
				"is married", characteristic);

		Term termHuman = representationFactory.makeTerm(vocabulary, "Human");
		CharacteristicForm characteristicForm2 = representationFactory.makeCharacteristicForm(vocabulary, termHuman,
				"is getting married");
		characteristicForm2.setRole("is getting married again");

		communityService.save(sp);
		resetTransaction();

		Vocabulary createdVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(createdVocabulary);
		assertNotNull(createdVocabulary.getId());
	}

	@Test
	public void testCreateCharacteristicFormDuplicate() {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		representationFactory.makeTerm(vocabulary, "Car");
		representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeCharacteristicForm(vocabulary, person, "is married");

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		person = representationService.findTermByResourceId(person.getId());

		Characteristic characteristic = meaningFactory.makeCharacteristic();

		CharacteristicForm characteristicFormDuplicate = representationFactory.makeCharacteristicForm(vocabulary,
				person, "is married", characteristic);
		try {
			representationService.saveCharacteristicForm(characteristicFormDuplicate);
			fail();
		} catch (IllegalArgumentException e) {
			// we expect this
			rollback();
		}
	}

	@Test
	public void testCreateCharacteristicFormInExsistingVocabulary() throws Exception {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.cf");
		representationFactory.makeTerm(vocabulary, "Car");

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertTrue(vocabularyContainsTerm(vocabulary, "Car"));

		Term termAuto = representationService.findTermBySignifier(vocabulary, "Car");
		assertNotNull(termAuto);
		CharacteristicForm characteristicForm = representationFactory.makeCharacteristicForm(vocabulary, termAuto,
				"drives");

		CharacteristicForm createdCharacteristicForm = representationService.saveCharacteristicForm(characteristicForm);
		assertNotNull(createdCharacteristicForm);
		assertNotNull(createdCharacteristicForm.getTerm());
		assertNotNull(createdCharacteristicForm.getId());
		assertEquals("Car", createdCharacteristicForm.getTerm().getSignifier());

		assertEquals("Car drives", createdCharacteristicForm.verbalise());

		resetTransaction();
		characteristicForm = representationService.findCharacteristicFormByResourceId(characteristicForm
				.getId());

		// Creating again should not throw constraint violation exception.
		representationService.saveCharacteristicForm(characteristicForm);
	}

	@Test
	public void testDeleteCharacteristicFormFromExsistingVocabularySaveVocabulary() throws Exception {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		representationFactory.makeTerm(vocabulary, "Car");
		representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeCharacteristicForm(vocabulary, person, "is happy");

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Set<Term> terms = vocabulary.getTerms();
		Set<CharacteristicForm> characteristicForms = vocabulary.getCharacteristicForms();

		assertEquals(3, terms.size());
		assertEquals(1, characteristicForms.size());
		assertTrue(vocabularyContainsTerm(vocabulary, "Person"));
		assertTrue(vocabularyContainsTerm(vocabulary, "Car"));
		assertTrue(vocabularyContainsTerm(vocabulary, "Human"));

		Term termPerson = representationService.findTermBySignifier(vocabulary, "Person");
		assertNotNull(termPerson);

		CharacteristicForm charForm = representationService.findCharacteristicFormByRepresentation(vocabulary,
				termPerson, "is happy");
		assertNotNull(charForm);
		assertTrue(vocabularyContainsCharacteristicForm(vocabulary, termPerson, "is happy"));

		representationService.remove(charForm);
		resetTransaction();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertTrue(vocabularyContainsTerm(vocabulary, "Person"));

		Term updatedVocTermPerson = representationService.findTermBySignifier(vocabulary, "Person");

		assertTrue(!vocabularyContainsCharacteristicForm(vocabulary, updatedVocTermPerson, "is happy"));
	}

	private boolean vocabularyContainsTerm(Vocabulary vocabulary, String signifier) {
		Set<Term> terms = vocabulary.getTerms();
		for (Term t : terms) {
			if (t.getSignifier().equals(signifier)) {
				return true;
			}
		}
		return false;
	}

	private boolean vocabularyContainsCharacteristicForm(Vocabulary vocabulary, Term term, String role) {
		Set<CharacteristicForm> characteristicForms = vocabulary.getCharacteristicForms();
		for (CharacteristicForm cf : characteristicForms) {
			if (cf.getTerm().equals(term) && cf.getRole().equals(role)) {
				return true;
			}
		}
		return false;
	}

}
