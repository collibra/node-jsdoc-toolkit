package com.collibra.dgc.service.term;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * @author wouter
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestTermsInManager extends AbstractServiceTest {

	@Test
	public void testCreateTermInNewVocabulary() throws Exception {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns2", "vocaulary.create.term");

		ObjectType objectType = meaningFactory.makeObjectType();
		Term term1 = representationFactory.makeTerm(vocabulary, "Car", objectType);

		Term term2 = representationFactory.makeTerm(vocabulary, "Person");
		term2.setSignifier("Person_Renamed");

		communityService.save(sp);
		resetTransaction();
		Vocabulary createdVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertNotNull(createdVocabulary);
		assertNotNull(createdVocabulary.getId());
	}

	@Test
	public void testCreateTermTwice() throws Exception {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term human = representationFactory.makeTerm(vocabulary, "Human");
		Term test = representationFactory.makeTerm(vocabulary, "Test");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", human);

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Set<Term> terms = vocabulary.getTerms();

		assertEquals(4, terms.size());
		assertTrue(!vocabularyContainsTerm(vocabulary, "Truck"));

		Term term = representationFactory.makeTerm(vocabulary, "Truck");

		Term createdTerm = representationService.saveTerm(term);
		assertNotNull(createdTerm);
		assertNotNull(createdTerm.getId());
		assertEquals("Truck", createdTerm.getSignifier());
		assertTrue(vocabularyContainsTerm(vocabulary, "Truck"));
		createdTerm = representationService.saveTerm(createdTerm);
		assertNotNull(createdTerm);
		assertNotNull(createdTerm.getId());
		assertEquals("Truck", createdTerm.getSignifier());
		assertTrue(vocabularyContainsTerm(vocabulary, "Truck"));
		resetTransaction();
	}

	@Test
	public void testCreateTermTwiceWithoutVocabularyNewVersion() throws Exception {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term human = representationFactory.makeTerm(vocabulary, "Human");
		Term test = representationFactory.makeTerm(vocabulary, "Test");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", human);

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Set<Term> terms = vocabulary.getTerms();

		assertEquals(4, terms.size());
		assertTrue(!vocabularyContainsTerm(vocabulary, "Truck"));

		Term term = representationFactory.makeTerm(vocabulary, "Truck");

		Term createdTerm = representationService.saveTerm(term);
		resetTransaction();

		createdTerm = representationService.findTermByResourceId(createdTerm.getId());

		assertNotNull(createdTerm);
		assertNotNull(createdTerm.getId());
		assertEquals("Truck", createdTerm.getSignifier());
		assertTrue(vocabularyContainsTerm(vocabulary, "Truck"));

		createdTerm = representationService.saveTerm(createdTerm);

		assertNotNull(createdTerm);
		assertNotNull(createdTerm.getId());
		assertEquals("Truck", createdTerm.getSignifier());
		assertTrue(vocabularyContainsTerm(vocabulary, "Truck"));
		resetTransaction();
	}

	@Test
	public void testCreateTermInExsistingVocabulary() throws Exception {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term human = representationFactory.makeTerm(vocabulary, "Human");
		Term test = representationFactory.makeTerm(vocabulary, "Test");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", human);

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Set<Term> terms = vocabulary.getTerms();

		assertEquals(4, terms.size());
		assertNotNull(representationService.findTermBySignifier(vocabulary, "Test"));
		assertNotNull(representationService.findTermBySignifier(vocabulary, "Human"));
		assertNotNull(representationService.findTermBySignifier(vocabulary, "Human"));
		assertNotNull(representationService.findTermBySignifier(vocabulary, "Person"));
		assertTrue(!vocabularyContainsTerm(vocabulary, "Truck"));

		Term term = representationFactory.makeTerm(vocabulary, "Truck");

		Term createdTerm = representationService.saveTerm(term);
		assertNotNull(createdTerm);
		assertNotNull(createdTerm.getId());
		assertEquals("Truck", createdTerm.getSignifier());
		assertTrue(vocabularyContainsTerm(vocabulary, "Truck"));
	}

	@Test
	public void testDeleteTermFromExsistingVocabularySaveVocabulary() throws Exception {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term human = representationFactory.makeTerm(vocabulary, "Human");
		Term auto = representationFactory.makeTerm(vocabulary, "Auto");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", human);

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Set<Term> terms = vocabulary.getTerms();

		assertEquals(4, terms.size());
		assertTrue(vocabularyContainsTerm(vocabulary, "Person"));
		assertTrue(vocabularyContainsTerm(vocabulary, "Car"));
		assertTrue(vocabularyContainsTerm(vocabulary, "Auto"));
		assertTrue(vocabularyContainsTerm(vocabulary, "Human"));

		Term termAuto = representationService.findTermBySignifier(vocabulary, "Auto");

		representationService.remove(termAuto);
		resetTransaction();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertTrue(!vocabularyContainsTerm(vocabulary, "Auto"));
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

	@Test
	public void testVocabularyTerms() throws Exception {
		Vocabulary voc1 = representationFactory.makeVocabulary(sp, "com.collibra.ns2", "Vocabulary.terms");
		Term term1 = representationFactory.makeTerm(voc1, "Term1Voc1");
		Term term2 = representationFactory.makeTerm(voc1, "Term2Voc1");

		communityService.save(sp);
		resetTransaction();

		voc1 = representationService.findVocabularyByResourceId(voc1.getId());

		term1 = representationService.findTermBySignifier(voc1, term1.getSignifier());
		term2 = representationService.findTermBySignifier(voc1, term2.getSignifier());

		// validate
		Vocabulary valVoc1 = representationService.findVocabularyByResourceId(voc1.getId());
		Term valTerm1 = representationService.findTermBySignifier(voc1, term1.getSignifier());
		assertTrue(valVoc1 != null);
		assertTrue(valTerm1 != null);
		assertEquals(voc1, valTerm1.getVocabulary());
		assertEquals(2, valVoc1.getTerms().size());
		assertTrue(valVoc1.getTerms().contains(term1));
		assertTrue(valVoc1.getTerms().contains(term2));

		resetTransaction();

		// test term remove
		voc1 = representationService.findVocabularyByResourceId(voc1.getId());
		term2 = representationService.findTermBySignifier(voc1, term2.getSignifier());
		representationService.remove(term2);

		resetTransaction();

		// validate
		valVoc1 = representationService.findVocabularyByResourceId(voc1.getId());
		term1 = representationService.findTermByResourceId(term1.getId());
		term2 = representationService.findTermByResourceId(term2.getId());
		assertTrue(valVoc1 != null);
		assertEquals(1, valVoc1.getTerms().size());
		assertTrue(valVoc1.getTerms().contains(term1));
		assertTrue(!valVoc1.getTerms().contains(term2));
		assertEquals(valVoc1, term1.getVocabulary());
	}

	@Test
	public void testCreateDuplicateTerm() {
		Vocabulary voc = representationFactory.makeVocabulary(sp, "My Test URI", "My Test");
		representationFactory.makeTerm(voc, "My Term");
		representationFactory.makeTerm(voc, "My Second Term");
		communityService.save(sp);
		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());

		try {
			Term duplicateTerm = representationFactory.makeTerm(voc, "My Term");
			representationService.saveTerm(duplicateTerm);
			fail();
		} catch (IllegalArgumentException e) {
			// we expect this
			rollback();
		}
	}

	@Test
	public void testAddAndRemoveOfSynonymsForRepresentations() {

		// Currently only synonyms for terms can be created and thus tested

		// Test up a vocabulary with a term
		Vocabulary vocoabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns2", "Vocabulary.synonyms");
		Term term = representationFactory.makeTerm(vocoabulary, "TermOne");
		Term mySynonym = representationFactory.makeTerm(vocoabulary, "My Synonym");
		communityService.save(sp);
		resetTransaction();

		// assert and add a synonym to the term
		Vocabulary loadedVocabulary = representationService.findVocabularyByName("Vocabulary.synonyms");
		term = representationService.findTermByResourceId(term.getId());
		mySynonym = representationService.findTermByResourceId(mySynonym.getId());
		assertNotNull(loadedVocabulary);
		assertEquals(2, loadedVocabulary.getTerms().size());

		representationService.addSynonym(term, mySynonym);
		resetTransaction();

		term = representationService.findTermByResourceId(term.getId());
		mySynonym = representationService.findTermByResourceId(mySynonym.getId());

		assertEquals(mySynonym.getObjectType(), term.getObjectType());
		mySynonym = representationService.saveTerm(mySynonym);
		loadedVocabulary = mySynonym.getVocabulary();

		// verify vocabulary versions
		assertEquals(2, term.getObjectType().getTerms().size());

		resetTransaction();

		// verify the synonyms after persisting
		loadedVocabulary = representationService.findVocabularyByResourceId(loadedVocabulary.getId());
		mySynonym = representationService.findTermBySignifier(loadedVocabulary, "My Synonym");
		term = representationService.findTermByResourceId(term.getId());
		assertEquals(mySynonym.getObjectType(), term.getObjectType());
		assertEquals(term.getObjectType().getId(), mySynonym.getObjectType().getId());

		representationService.removeSynonym(mySynonym);
		resetTransaction();

		loadedVocabulary = representationService.findVocabularyByResourceId(loadedVocabulary.getId());
		mySynonym = representationService.findTermBySignifier(loadedVocabulary, "My Synonym");
		term = representationService.findTermBySignifier(loadedVocabulary, "TermOne");
		assertTrue(!term.getObjectType().equals(mySynonym.getObjectType()));
		assertEquals(1, term.getObjectType().getTerms().size());
		assertEquals(1, mySynonym.getObjectType().getTerms().size());
	}

	// @Ignore
	// public void testMoveTerm() {
	// Vocabulary voc = representationFactory.makeVocabulary(sp, "My Test URI", "My Test");
	// Term term = representationFactory.makeTerm(voc, "My Term");
	// communityService.save(sp);
	// resetTransaction();
	//
	// Community anotherSp = communityFactory.makeCommunity("Another SP Comm", "Another SP Comm URI");
	// Vocabulary anotherVoc = representationFactory.makeVocabulary(anotherSp, "Another Voc URI", "Another Voc");
	// communityService.save(anotherSp);
	// resetTransaction();
	//
	// anotherVoc = representationService.findVocabularyByResourceId(anotherVoc.getId());
	// assertNotNull(anotherVoc);
	//
	// term = representationService.findTermByResourceId(term.getId());
	// assertNotNull(term);
	//
	// Term newterm = representationService.moveTerm(term, anotherVoc);
	// resetTransaction();
	//
	// term = representationService.findTermByResourceId(term.getId());
	// Assert.assertNull(term);
	//
	// anotherVoc = representationService.findVocabularyByResourceId(anotherVoc.getId());
	// newterm = representationService.findTermByResourceId(newterm.getId());
	// assertNotNull(newterm);
	// assertEquals(anotherVoc, newterm.getVocabulary());
	// }

	// @Ignore
	// public void testMoveTermFailsAsTermWithSimilarSignifierExists() {
	// Vocabulary voc = representationFactory.makeVocabulary(sp, "My Test URI", "My Test");
	// Term term = representationFactory.makeTerm(voc, "My Term");
	// communityService.save(sp);
	// resetTransaction();
	//
	// Community anotherSp = communityFactory.makeCommunity("Another SP Comm", "Another SP Comm URI");
	// Vocabulary anotherVoc = representationFactory.makeVocabulary(anotherSp, "Another Voc URI", "Another Voc");
	// representationFactory.makeTerm(anotherVoc, "My Term");
	// communityService.save(anotherSp);
	// resetTransaction();
	//
	// anotherVoc = representationService.findVocabularyByResourceId(anotherVoc.getId());
	// assertNotNull(anotherVoc);
	//
	// term = representationService.findTermByResourceId(term.getId());
	// assertNotNull(term);
	//
	// try {
	// representationService.moveTerm(term, anotherVoc);
	// fail();
	// } catch (ConstraintViolationException ex) {
	// // SUccess
	// }
	//
	// }
	//
	// @Ignore
	// public void testMoveTermWithAttributes() {
	// Vocabulary voc = representationFactory.makeVocabulary(sp, "My Test URI", "My Test");
	// Term term = representationFactory.makeTerm(voc, "My Term");
	// communityService.save(sp);
	// resetTransaction();
	//
	// term = representationService.findTermByResourceId(term.getId());
	// representationFactory.makeStringAttribute(attributeService.findMetaDescription(), term, "Definition");
	// representationFactory.makeStringAttribute(attributeService.findMetaDescription(), term, "Description");
	// representationService.saveTerm(term);
	// resetTransaction();
	//
	// Community anotherSp = communityFactory.makeCommunity("Another SP Comm", "Another SP Comm URI");
	// Vocabulary anotherVoc = representationFactory.makeVocabulary(anotherSp, "Another Voc URI", "Another Voc");
	// communityService.save(anotherSp);
	// resetTransaction();
	//
	// anotherVoc = representationService.findVocabularyByResourceId(anotherVoc.getId());
	// assertNotNull(anotherVoc);
	//
	// term = representationService.findTermByResourceId(term.getId());
	// assertNotNull(term);
	// assertEquals(2, term.getAttributes().size());
	//
	// Term newterm = representationService.moveTerm(term, anotherVoc);
	// resetTransaction();
	//
	// term = representationService.findTermByResourceId(term.getId());
	// Assert.assertNull(term);
	//
	// anotherVoc = representationService.findVocabularyByResourceId(anotherVoc.getId());
	// newterm = representationService.findTermByResourceId(newterm.getId());
	// assertNotNull(newterm);
	// assertEquals(anotherVoc, newterm.getVocabulary());
	// assertEquals(3, newterm.getAttributes().size());
	// }
	//
	// @Ignore
	// public void testMoveTerms() {
	// List<Term> terms = new LinkedList<Term>();
	//
	// Vocabulary voc = representationFactory.makeVocabulary(sp, "Test URI 1", "Test 1");
	// for (int i = 1; i < 3; i++) {
	// Term term = representationFactory.makeTerm(voc, "Term " + i);
	// terms.add(term);
	// }
	//
	// voc = representationFactory.makeVocabulary(sp, "Test URI 2", "Test 2");
	// for (int i = 3; i < 5; i++) {
	// Term term = representationFactory.makeTerm(voc, "Term " + i);
	// terms.add(term);
	// }
	//
	// communityService.save(sp);
	// resetTransaction();
	//
	// Community anotherSp = communityFactory.makeCommunity("Another SP Comm", "Another SP Comm URI");
	// Vocabulary anotherVoc = representationFactory.makeVocabulary(anotherSp, "Another Voc URI", "Another Voc");
	// communityService.save(anotherSp);
	// resetTransaction();
	//
	// anotherVoc = representationService.findVocabularyByResourceId(anotherVoc.getId());
	// assertNotNull(anotherVoc);
	//
	// List<Term> reloadedTerms = new LinkedList<Term>();
	// for (Term term : terms) {
	// term = representationService.findTermByResourceId(term.getId());
	// assertNotNull(term);
	//
	// reloadedTerms.add(term);
	// }
	//
	// Collection<Term> newterms = representationService.moveTerms(reloadedTerms, anotherVoc);
	// resetTransaction();
	//
	// // Assert for newly created terms.
	// anotherVoc = representationService.findVocabularyByResourceId(anotherVoc.getId());
	// for (Term term : newterms) {
	// term = representationService.findTermByResourceId(term.getId());
	// Assert.assertNotNull(term);
	// assertEquals(anotherVoc, term.getVocabulary());
	// }
	//
	// // Old terms are gone
	// for (Term term : terms) {
	// term = representationService.findTermByResourceId(term.getId());
	// Assert.assertNull(term);
	// }
	// }
	//
	// @Ignore
	// public void testMoveTermsSkipsTheOnesAlreadyInDestination() {
	// List<Term> terms = new LinkedList<Term>();
	//
	// Vocabulary voc = representationFactory.makeVocabulary(sp, "Test URI 1", "Test 1");
	// for (int i = 1; i < 3; i++) {
	// Term term = representationFactory.makeTerm(voc, "Term " + i);
	// terms.add(term);
	// }
	//
	// communityService.save(sp);
	// resetTransaction();
	//
	// Community anotherSp = communityFactory.makeCommunity("Another SP Comm", "Another SP Comm URI");
	// Vocabulary anotherVoc = representationFactory.makeVocabulary(anotherSp, "Another Voc URI", "Another Voc");
	// List<Term> alreadyExistingOnes = new LinkedList<Term>();
	// for (int i = 3; i < 6; i++) {
	// Term term = representationFactory.makeTerm(anotherVoc, "Term " + i);
	// alreadyExistingOnes.add(term);
	// }
	//
	// communityService.save(anotherSp);
	// resetTransaction();
	//
	// anotherVoc = representationService.findVocabularyByResourceId(anotherVoc.getId());
	// assertNotNull(anotherVoc);
	//
	// List<Term> reloadedTerms = new LinkedList<Term>();
	// for (Term term : terms) {
	// term = representationService.findTermByResourceId(term.getId());
	// assertNotNull(term);
	//
	// reloadedTerms.add(term);
	// }
	//
	// for (Term term : alreadyExistingOnes) {
	// term = representationService.findTermByResourceId(term.getId());
	// assertNotNull(term);
	//
	// reloadedTerms.add(term);
	// }
	//
	// Collection<Term> newterms = representationService.moveTerms(reloadedTerms, anotherVoc);
	// resetTransaction();
	//
	// // Already existing terms are not moved.
	// assertEquals(alreadyExistingOnes.size(), reloadedTerms.size() - terms.size());
	// assertEquals(terms.size(), newterms.size());
	//
	// // Assert for newly created terms.
	// anotherVoc = representationService.findVocabularyByResourceId(anotherVoc.getId());
	// for (Term term : newterms) {
	// term = representationService.findTermByResourceId(term.getId());
	// Assert.assertNotNull(term);
	// assertEquals(anotherVoc, term.getVocabulary());
	// }
	//
	// // Old terms are gone
	// for (Term term : terms) {
	// term = representationService.findTermByResourceId(term.getId());
	// Assert.assertNull(term);
	// }
	//
	// // Already existing terms in destination vocabulary are not moved.
	// for (Term term : alreadyExistingOnes) {
	// term = representationService.findTermByResourceId(term.getId());
	// Assert.assertNotNull(term);
	// }
	// }
}
