package com.collibra.dgc.service.facttypeform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestBinaryFactTypeFormsInManager extends AbstractServiceTest {

	@Test
	public void testCreateBinaryFactTypeFormInNewVocabulary() throws Exception {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");

		Term termPerson = representationFactory.makeTerm(vocabulary, "Person");
		assertNotNull(termPerson);
		Term termCar = representationFactory.makeTerm(vocabulary, "Car");
		assertNotNull(termCar);

		BinaryFactType binaryFactType = meaningFactory.makeBinaryFactType();
		representationFactory.makeBinaryFactTypeForm(vocabulary, termPerson, "drives", "driven by", termCar,
				binaryFactType);

		Term termHuman = representationFactory.makeTerm(vocabulary, "Human");
		Term termSpace = representationFactory.makeTerm(vocabulary, "Space");

		BinaryFactTypeForm binaryFactTypeForm2 = representationFactory.makeBinaryFactTypeForm(vocabulary, termHuman,
				"travels to", "visited by", termSpace);
		binaryFactTypeForm2.setRole("revisited by");

		communityService.save(sp);

		resetTransaction();

		Vocabulary createdVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertNotNull(createdVocabulary);
		assertNotNull(createdVocabulary.getId());
	}

	@Test
	public void testMetaBinaryFactTypeFormSetting() {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		representationService.saveVocabulary(vocabulary);

		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Term termPerson = representationFactory.makeTerm(vocabulary, "Person");
		assertNotNull(termPerson);
		Term termCar = representationFactory.makeTerm(vocabulary, "Car");
		assertNotNull(termCar);

		ObjectType OT = meaningService.findMetaObjectType();
		Term termOT = representationService.findPreferredTermInAllIncorporatedVocabularies(OT, vocabulary);

		BinaryFactTypeForm metabftf = representationFactory.makeBinaryFactTypeForm(vocabulary, termOT, "drives",
				"driven by", termOT);
		Term bftTerm = representationFactory.makeTerm(vocabulary, "Drive Relation");
		bftTerm.getObjectType().setGeneralConcept(meaningService.findMetaBinaryFactType());
		metabftf.getBinaryFactType().setType(bftTerm.getObjectType());

		communityService.save(vocabulary.getCommunity());
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		metabftf = representationService.findBinaryFactTypeFormByResourceId(metabftf.getId());
		termPerson = representationService.findTermByResourceId(termPerson.getId());
		termCar = representationService.findTermByResourceId(termCar.getId());

		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, termPerson, "drives",
				"driven by", termCar, metabftf.getBinaryFactType());

		representationService.saveBinaryFactTypeForm(bftf);
		resetTransaction();

		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		bftTerm = representationService.findTermByResourceId(bftTerm.getId());

		assertEquals(1, bftf.getBinaryFactType().getType().getRepresentations().size());
		assertEquals(bftTerm, bftf.getBinaryFactType().getType().getRepresentations().iterator().next());
	}

	@Test
	public void testCreateBinaryFactTypeFormBetweenSBVRTerms() throws Exception {
		ObjectType ot = meaningService.findMetaObjectType();
		Term oTerm = null;

		for (Term t : ot.getTerms()) {
			if (t.getIsPreferred()) {
				oTerm = t;
				break;
			}
		}

		assertNotNull(oTerm);

		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(oTerm.getVocabulary(), oTerm, "drives",
				"driven by", oTerm);
		// FIXME Just calling create on BFTF is not working.
		representationService.saveBinaryFactTypeForm(bftf);
		// representationService.createVocabulary(bftf.getVocabulary());

		resetTransaction();

		BinaryFactTypeForm bftfs = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertNotNull(bftfs);

	}

	@Test
	public void testCreateBinaryFactTypeFormWithTermsFromDifferentVocabularies() throws Exception {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Vocabulary vocabulary2 = representationFactory
				.makeVocabulary(sp, "com.namespace.ns2", "vocaulary.create.term2");

		Term termPerson = representationFactory.makeTerm(vocabulary, "Person");
		assertNotNull(termPerson);
		Term termCar = representationFactory.makeTerm(vocabulary2, "Car");
		assertNotNull(termCar);

		BinaryFactType binaryFactType = meaningFactory.makeBinaryFactType();
		representationFactory.makeBinaryFactTypeForm(vocabulary, termPerson, "drives", "driven by", termCar,
				binaryFactType);

		Term termHuman = representationFactory.makeTerm(vocabulary, "Human");
		Term termSpace = representationFactory.makeTerm(vocabulary, "Space");

		BinaryFactTypeForm binaryFactTypeForm2 = representationFactory.makeBinaryFactTypeForm(vocabulary, termHuman,
				"travels to", "visited by", termSpace);
		binaryFactTypeForm2.setRole("revisited by");

		communityService.save(sp);
		resetTransaction();

		Vocabulary createdVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertNotNull(createdVocabulary);
		assertNotNull(createdVocabulary.getId());
	}

	@Test
	public void testCreateBinaryFactTypeDuplicate() {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");

		communityService.save(sp);
		resetTransaction();

		Vocabulary createdVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Term termPerson = representationFactory.makeTerm(createdVocabulary, "Person");
		assertNotNull(termPerson);
		Term termCar = representationFactory.makeTerm(createdVocabulary, "Car");
		assertNotNull(termCar);

		BinaryFactType binaryFactType = meaningFactory.makeBinaryFactType();
		representationFactory.makeBinaryFactTypeForm(createdVocabulary, termPerson, "drives", "driven by", termCar,
				binaryFactType);

		vocabulary = representationService.saveVocabulary(createdVocabulary);

		resetTransaction();

		termPerson = representationService.findTermByResourceId(termPerson.getId());
		termCar = representationService.findTermByResourceId(termCar.getId());
		BinaryFactTypeForm binaryFactTypeFormDuplicate = representationFactory.makeBinaryFactTypeForm(vocabulary,
				termPerson, "drives", "driven by", termCar, binaryFactType);
		try {
			representationService.saveBinaryFactTypeForm(binaryFactTypeFormDuplicate);
			fail();
		} catch (IllegalArgumentException e) {
			// we expect this
		}
	}

	@Test
	public void testCreateBinaryFactTypeFormInEmptyPersistentVocabulary() throws Exception {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");

		communityService.save(sp);
		resetTransaction();

		Vocabulary createdVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Term termPerson = representationFactory.makeTerm(createdVocabulary, "Person");
		representationService.save(termPerson);
		assertNotNull(termPerson);
		Term termCar = representationFactory.makeTerm(createdVocabulary, "Car");
		representationService.save(termCar);
		assertNotNull(termCar);

		BinaryFactType binaryFactType = meaningFactory.makeBinaryFactType();
		BinaryFactTypeForm binaryFactTypeForm1 = representationFactory.makeBinaryFactTypeForm(createdVocabulary,
				termPerson, "drives", "driven by", termCar, binaryFactType);

		BinaryFactTypeForm createdBinaryFactTypeForm1 = representationService
				.saveBinaryFactTypeForm(binaryFactTypeForm1);
		assertNotNull(createdBinaryFactTypeForm1);
		assertNotNull(createdBinaryFactTypeForm1.getId());
	}

	@Test
	public void testCreateModifiedBinaryFactTypeFormInEmptyPersistentVocabulary() throws Exception {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");

		communityService.save(sp);
		resetTransaction();

		Vocabulary createVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Term termHuman = representationFactory.makeTerm(createVocabulary, "Human");
		representationService.save(termHuman);
		Term termSpace = representationFactory.makeTerm(createVocabulary, "Space");
		representationService.save(termSpace);

		BinaryFactTypeForm binaryFactTypeForm2 = representationFactory.makeBinaryFactTypeForm(createVocabulary,
				termHuman, "travels to", "visited by", termSpace);
		binaryFactTypeForm2.setRole("revisited by");
		BinaryFactTypeForm createdBinaryFactTypeForm2 = representationService
				.saveBinaryFactTypeForm(binaryFactTypeForm2);
		assertNotNull(createdBinaryFactTypeForm2);
		assertNotNull(createdBinaryFactTypeForm2.getId());
	}

	@Test
	public void testCreateBinaryFactTypeFormInExsistingVocabulary() throws Exception {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		representationFactory.makeTerm(vocabulary, "Person");
		representationFactory.makeTerm(vocabulary, "Car");
		representationFactory.makeTerm(vocabulary, "Human");

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Set<Term> terms = vocabulary.getTerms();
		assertEquals(3, terms.size());
		assertTrue(vocabularyContainsTerm(vocabulary, "Person"));
		assertTrue(vocabularyContainsTerm(vocabulary, "Car"));
		assertTrue(vocabularyContainsTerm(vocabulary, "Human"));

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
	}

	@Test
	public void testDeleteBinaryFactTypeFormFromExsistingVocabularySaveVocabulary() throws Exception {
		communityService.save(sp);
		resetTransaction();

		sp = communityService.findCommunity(sp.getId());

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Set<Term> terms = vocabulary.getTerms();
		Set<BinaryFactTypeForm> binaryFactTypeForms = vocabulary.getBinaryFactTypeForms();

		assertEquals(3, terms.size());
		assertEquals(2, binaryFactTypeForms.size());
		assertTrue(vocabularyContainsTerm(vocabulary, "Person"));
		assertTrue(vocabularyContainsTerm(vocabulary, "Car"));
		assertTrue(vocabularyContainsTerm(vocabulary, "Human"));

		Term termPerson = representationService.findTermBySignifier(vocabulary, "Person");
		assertNotNull(termPerson);
		Term termCar = representationService.findTermBySignifier(vocabulary, "Car");
		assertNotNull(termCar);

		BinaryFactTypeForm binaryFactTypeForm = representationService.findBinaryFactTypeFormByRepresentation(
				vocabulary, termPerson, "drives", "driven by", termCar);
		// BinaryFactTypeForm binaryFactTypeForm = representationService.findBinaryFactTypeFormByExpression(vocabulary,
		// "Person", "drives", "driven by", "Car");

		assertNotNull(binaryFactTypeForm);
		assertTrue(vocabularyContainsBinaryFactTypeForm(vocabulary, "Person", "drives", "driven by", "Car"));

		representationService.remove(binaryFactTypeForm);
		resetTransaction();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertTrue(vocabularyContainsTerm(vocabulary, "Person"));
		assertTrue(vocabularyContainsTerm(vocabulary, "Car"));
		assertTrue(!vocabularyContainsBinaryFactTypeForm(vocabulary, "Person", "drives", "driven by", "Car"));
	}

	/**
	 * Bug #6167. Hibernate Exception when changing the status of a BFTF
	 */
	@Test
	public void testChangeStatus() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");

		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives",
				"driven by", car);

		communityService.save(sp);
		resetTransaction();

		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertNotNull(bftf);

		Term acceptedStatusTerm = representationService.findTermBySignifier(
				representationService.findStatusesVocabulary(), "Accepted");
		assertNotNull(acceptedStatusTerm);
		representationService.changeStatus(bftf, acceptedStatusTerm);
		resetTransaction();

		// Assert for setting proper status.
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertEquals(bftf.getStatus().getMeaning(), objectTypeDao.getMetaAcceptedStatusType());
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

	private boolean vocabularyContainsBinaryFactTypeForm(Vocabulary vocabulary, String headTerm, String role,
			String coRole, String tailTerm) {

		Set<BinaryFactTypeForm> binaryFactTypeForms = vocabulary.getBinaryFactTypeForms();
		for (BinaryFactTypeForm bftf : binaryFactTypeForms) {
			if (bftf.getHeadTerm().getSignifier().equals(headTerm) && bftf.getRole().equals(role)
					&& bftf.getCoRole().equals(coRole) && bftf.getTailTerm().getSignifier().equals(tailTerm)) {
				return true;
			}
		}
		return false;
	}

}
