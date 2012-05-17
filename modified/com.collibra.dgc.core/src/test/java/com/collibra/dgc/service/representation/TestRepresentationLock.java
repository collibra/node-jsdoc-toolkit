package com.collibra.dgc.service.representation;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.DGCException;
import com.collibra.dgc.core.model.impl.ResourceImpl;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.representation.impl.RepresentationImpl;
import com.collibra.dgc.core.model.rules.SimpleRuleStatement;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * Tests for {@link Representation} locking.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRepresentationLock extends AbstractServiceTest {
	private Term term;
	private BinaryFactTypeForm bftf;
	private CharacteristicForm cf;

	@Before
	public void setUp() throws Exception {
		prepareLockedRepresentations();
	}

	private void prepareLockedRepresentations() {
		// Create locked representations
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "TestRepresentationLock URI",
				"TestRepresentationLock");
		term = representationFactory.makeTerm(vocabulary, "Locked Term");
		representationFactory
				.makeStringAttribute(attributeService.findMetaDefinition(), term, "Locked Term Definition");

		Term head = representationFactory.makeTerm(vocabulary, "Head");
		Term tail = representationFactory.makeTerm(vocabulary, "Tail");
		bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, head, "has", "of", tail);
		representationFactory
				.makeStringAttribute(attributeService.findMetaDefinition(), bftf, "Locked BFTF Definition");
		cf = representationFactory.makeCharacteristicForm(vocabulary, head, "role");
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), cf, "Locked CF Definition");

		communityService.save(sp);
		resetTransaction();

		// Load and lock representations.
		reload();

		((RepresentationImpl) term).setLock(true);
		representationService.saveTerm(term);
		((RepresentationImpl) bftf).setLock(true);
		representationService.saveBinaryFactTypeForm(bftf);
		((RepresentationImpl) cf).setLock(true);
		representationService.saveCharacteristicForm(cf);

		resetTransaction();

		reload();
	}

	private void reload() {
		term = representationService.findTermByResourceId(term.getId());
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		cf = representationService.findCharacteristicFormByResourceId(cf.getId());
	}

	@Test
	public void testTermModifications() {
		// Changing the signifier of locked term not possible.
		try {
			representationService.changeSignifier(term, "Changed Signifier");
			Assert.fail();
		} catch (DGCException ex) {
			// Success
			rollback();
		}
		resetTransaction();
		reload();

		Term originalTerm = representationFactory.makeTerm(term.getVocabulary(), "Original Term");
		representationService.saveTerm(originalTerm);
		resetTransaction();
		reload();

		// Making the locked term as synonym other term is not possible.
		originalTerm = representationService.findTermByResourceId(originalTerm.getId());
		createSynonymNotPossible(originalTerm, term);

		// Removing attribute from locked term not possible.
		removeAttributeNotPossible(term);

		// Changing concept type for locked Term not possible.
		originalTerm = representationService.findTermByResourceId(originalTerm.getId());
		changeConceptTypeNotPossible(term, originalTerm);

		// Changing general concept for locked BFTF not possible
		originalTerm = representationService.findTermByResourceId(originalTerm.getId());
		changeGeneralConceptNotPossible(term, originalTerm);

		// Cannot add rule to locked term.
		createBftfNotPossibleWhenTermLocked(term);

		// Cannot create CF for locked term.
		createCfNotPossibleOnLockedTerm(term);
	}

	@Test
	public void testBftfModifications() {
		// Changing the signifier of locked BFTF not possible.
		try {
			representationService.changeBinaryFactTypeForm(bftf, bftf.getHeadTerm(), "new role", bftf.getCoRole(),
					bftf.getTailTerm());
			Assert.fail();
		} catch (DGCException ex) {
			// Success
			rollback();
		}
		resetTransaction();
		reload();

		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		Term h2 = representationFactory.makeTerm(bftf.getVocabulary(), "H2");
		representationService.save(h2);
		Term t2 = representationFactory.makeTerm(bftf.getVocabulary(), "T2");
		representationService.save(t2);
		BinaryFactTypeForm originalBftf = representationFactory.makeBinaryFactTypeForm(bftf.getVocabulary(), h2,
				"original has", "original of", t2);
		representationService.saveBinaryFactTypeForm(originalBftf);
		resetTransaction();
		reload();

		// Making the locked term as synonym other BFTF is not possible.
		originalBftf = representationService.findBinaryFactTypeFormByResourceId(originalBftf.getId());
		createSynonymNotPossible(originalBftf, bftf);

		// Removing attribute from locked BFTF not possible.
		removeAttributeNotPossible(bftf);

		// Changing concept type for locked BFTF not possible.
		h2 = representationService.findTermByResourceId(h2.getId());
		changeConceptTypeNotPossible(bftf, h2);

		// Changing general concept for locked BFTF not possible
		originalBftf = representationService.findBinaryFactTypeFormByResourceId(originalBftf.getId());
		changeGeneralConceptNotPossible(bftf, originalBftf);

		// Creating rule on locked BFTF not possible
		createRuleNotPossibleWhenBftfLocked(bftf);
	}

	@Test
	public void testCfModifications() {
		// Changing the signifier of locked CF not possible.
		try {
			representationService.changeCharacteristicForm(cf, cf.getTerm(), "changed role");
			Assert.fail();
		} catch (DGCException ex) {
			// Success
			rollback();
		}
		resetTransaction();
		reload();

		Term t = representationFactory.makeTerm(cf.getVocabulary(), "T");
		representationService.save(t);
		CharacteristicForm originalCf = representationFactory.makeCharacteristicForm(cf.getVocabulary(), t,
				"original role");
		representationService.saveCharacteristicForm(originalCf);
		resetTransaction();
		reload();

		// Making the locked term as synonym other CF is not possible.
		originalCf = representationService.findCharacteristicFormByResourceId(originalCf.getId());
		createSynonymNotPossible(originalCf, cf);

		// Removing attribute from locked CF not possible.
		removeAttributeNotPossible(cf);

		// Changing concept type for locked CF not possible.
		t = representationService.findTermByResourceId(t.getId());
		changeConceptTypeNotPossible(term, t);

		// Changing general concept for locked CF not possible
		originalCf = representationService.findCharacteristicFormByResourceId(originalCf.getId());
		changeGeneralConceptNotPossible(cf, originalCf);
	}

	@Test
	public void testRepresentationLockingIfStatusIsAccepeted() {
		Term test = representationFactory.makeTerm(term.getVocabulary(), "Test");
		representationService.saveTerm(test);
		resetTransaction();

		test = representationService.findTermByResourceId(test.getId());
		Assert.assertFalse(test.isLocked());

		// Change the status to accepted.
		Term acceptedStatusTerm = representationService.findTermBySignifier(
				representationService.findStatusesVocabulary(), "Accepted");
		representationService.changeStatus(test, acceptedStatusTerm);
		resetTransaction();

		// The term is locked.
		test = representationService.findTermByResourceId(test.getId());
		Assert.assertTrue(test.isLocked());

		// Changing the status to anything other than 'Accepted' will unlock the representation.
		Term candidateStatusTerm = representationService.findTermBySignifier(
				representationService.findStatusesVocabulary(), "Candidate");
		representationService.changeStatus(test, candidateStatusTerm);
		resetTransaction();

		test = representationService.findTermByResourceId(test.getId());
		Assert.assertFalse(test.isLocked());
	}

	@Test
	public void testCreateRuleNotPossibleOnLockedTerm() {
		Term newTerm = representationFactory.makeTerm(term.getVocabulary(), "New Term");
		Term tail = representationFactory.makeTerm(newTerm.getVocabulary(), "Tail Term");
		BinaryFactTypeForm newBftf = representationFactory.makeBinaryFactTypeForm(newTerm.getVocabulary(), newTerm,
				"role", "corole", tail);
		representationService.saveVocabulary(newTerm.getVocabulary());
		resetTransaction();

		newTerm = representationService.findTermByResourceId(newTerm.getId());
		((ResourceImpl) newTerm).setLock(true);
		representationService.saveTerm(newTerm);
		resetTransaction();

		newBftf = representationService.findBinaryFactTypeFormByResourceId(newBftf.getId());
		SimpleStatement st = ruleFactory.makeSimpleStatement(newBftf.getVocabulary());
		try {
			st.addReadingDirection(newBftf.getLeftPlaceHolder());
			Assert.fail();
		} catch (DGCException ex) {
			// Success
			rollback();
		}

		resetTransaction();
		reload();
	}

	private void createSynonymNotPossible(Representation original, Representation synonym) {
		try {
			representationService.addSynonym(original, synonym);
			Assert.fail();
		} catch (DGCException ex) {
			// Success
			rollback();
		}

		resetTransaction();
		reload();

	}

	private void removeAttributeNotPossible(Representation representation) {
		try {
			attributeService.remove(representation.getAttributes().iterator().next());
			Assert.fail();
		} catch (DGCException ex) {
			// Success
			rollback();
		}

		resetTransaction();
		reload();
	}

	private void changeConceptTypeNotPossible(Representation representation, Term typeTerm) {
		try {
			representationService.setConceptTypeRepresentation(representation, typeTerm);
			Assert.fail();
		} catch (DGCException ex) {
			// Success
			rollback();
		}

		resetTransaction();
		reload();
	}

	private void changeGeneralConceptNotPossible(Representation representation, Representation generalRepresentation) {
		try {
			representationService.setGeneralConceptRepresentation(representation, generalRepresentation);
			Assert.fail();
		} catch (DGCException ex) {
			// Success
			rollback();
		}

		resetTransaction();
		reload();
	}

	private void createBftfNotPossibleWhenTermLocked(Term term) {
		Term tail = representationFactory.makeTerm(term.getVocabulary(), "Tail" + UUID.randomUUID());
		try {
			representationFactory.makeBinaryFactTypeForm(term.getVocabulary(), term, "role", "corole", tail);
			Assert.fail();
		} catch (DGCException ex) {
			// Success
			rollback();
		}

		resetTransaction();
		reload();
	}

	private void createRuleNotPossibleWhenBftfLocked(BinaryFactTypeForm bftf) {
		SimpleRuleStatement rs = ruleFactory.makeMandatoryRuleStatement(bftf.getVocabulary());
		SimpleStatement st = ruleFactory.makeSimpleStatement(bftf.getVocabulary());
		rs.addSimpleStatement(st);
		try {
			st.addReadingDirection(bftf.getLeftPlaceHolder());
			Assert.fail();
		} catch (DGCException ex) {
			// Success
			rollback();
		}

		resetTransaction();
		reload();
	}

	private void createCfNotPossibleOnLockedTerm(Term term) {
		try {
			representationFactory.makeCharacteristicForm(term.getVocabulary(), term, "role");
			Assert.fail();
		} catch (DGCException ex) {
			// Success
			rollback();
		}

		resetTransaction();
		reload();
	}
}
