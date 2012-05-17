package com.collibra.dgc.service.vocabulary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.service.AbstractBootstrappedServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestVocabularyManager extends AbstractBootstrappedServiceTest {

	/*
	 * @Before public void verifyInitialDatabaseState() throws Exception { // logic to verify the initial state before a
	 * transaction is started BootstrapUtils utils = new BootstrapUtils(configuration); if(utils.detectDatabase()) {
	 * //MySQL, Batch utils.executeBatchDropSatements(); utils.executeBatchCreateStatements(); //
	 * executeBatchBootstrapDataStatements() }else { // Oracle, Single statements utils.executeDropSatements();
	 * utils.executeCreateStatements(); //executeBootstrapDataStatements(); } resetTransaction(); }
	 * 
	 * @After public void verifyFinalDatabaseState() throws Exception { // logic to verify the final state after
	 * transaction has rolled back BootstrapUtils utils = new BootstrapUtils(configuration); if(utils.detectDatabase())
	 * { //MySQL, Batch //executeBatchDropSatements(); }else { // Oracle, Single statements //executeDropSatements(); }
	 * }
	 */

	@Test
	public void testVocabularyTypes() {
		Community c = communityFactory.makeCommunity("C", "C");
		Vocabulary glossary = representationFactory.makeVocabulary(c, "glossary", "glossary");
		assertEquals(null, glossary.getType());
		communityService.save(c);
		resetTransaction();
		glossary = representationService.findVocabularyByResourceId(glossary.getId());
		assertEquals(meaningService.findMetaGlossaryVocabularyType(), glossary.getType());
		assertEquals(meaningService.findMetaBusinessVocabularyType(), meaningService.findMetaGlossaryVocabularyType()
				.getGeneralConcept());
		assertEquals(meaningService.findMetaVocabularyType(), meaningService.findMetaBusinessVocabularyType()
				.getGeneralConcept());

		resetTransaction();

		glossary = representationService.findVocabularyByResourceId(glossary.getId());
		assertEquals(meaningService.findMetaGlossaryVocabularyType(), glossary.getType());

		Vocabulary businessVocabulary = representationFactory.makeVocabularyOfType(c, "BV", "business Voc",
				meaningService.findMetaBusinessVocabularyType());
		assertEquals(meaningService.findMetaBusinessVocabularyType(), businessVocabulary.getType());
		representationService.saveVocabulary(businessVocabulary);

		resetTransaction();

		businessVocabulary = representationService.findVocabularyByResourceId(businessVocabulary.getId());
		assertEquals(meaningService.findMetaBusinessVocabularyType(), businessVocabulary.getType());
	}

	@Test
	public void testAddAttributeToTermInVocabulary() throws Exception {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);

		communityService.save(sp);
		resetTransaction();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);
		Set<Term> terms = vocabulary.getTerms();
		Iterator<Term> i = terms.iterator();
		Term t1 = i.next();
		Term t2 = i.next();

		assertNotNull(t1);
		assertNotNull(t2);
		assertTrue(t1 != t2);
		assertTrue(!t1.getSignifier().equals(t2.getSignifier()));

		assertNotNull(t1.getMeaning());
		assertNotNull(t1.getMeaning().getId());

		assertNotNull(t2.getMeaning());
		assertNotNull(t2.getMeaning().getId());

		Attribute attribute = representationFactory.makeStringAttribute(t1, t2, "the value for this attribute");
		assertNotNull(attribute);

		Vocabulary updatedVocabulary = representationService.saveVocabulary(vocabulary);
		assertNotNull(updatedVocabulary);

		System.out.println("done");
	}

	@Test
	public void testDeleteVocabularyAllVersions() {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocabulary.create.term");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);

		communityService.save(sp);
		resetTransaction();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Term taxi = representationFactory.makeTerm(vocabulary, "Taxi");
		representationService.saveVocabulary(vocabulary);

		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		taxi = representationService.findTermByResourceId(taxi.getId());
		representationService.remove(taxi);

		resetTransaction();

		sp = communityService.findCommunity(sp.getId());
		communityService.remove(sp);

		resetTransaction();

		Vocabulary ret = vocabularyDao.findById(vocabulary.getId());
		assertNull(ret);
	}

	/**
	 * Tests when there are vocabularies, that they are sorted (does not assume how many there are)
	 */
	@Test
	public void testFindAll() {
		List<Vocabulary> allVocabularies = representationService.findVocabularies();
		Vocabulary prev = null;
		// test whether vocabularies are sorted
		for (Vocabulary voc : allVocabularies) {
			if (prev == null) {
				prev = voc;
				continue;
			}
			System.out.println(voc.getName());
			int compare = prev.getName().toLowerCase().compareTo(voc.getName().toLowerCase());
			assertTrue(compare <= 0);
			prev = voc;
		}
	}

	@Test
	public void testFindAllNonMeta() {
		final Community sp = communityFactory.makeCommunity("test", "test");

		communityService.save(sp);
		resetTransaction();
		List<Vocabulary> allVocabularies = representationService.findVocabularies(true);
		// test whether vocabularies are sorted
		for (Vocabulary voc : allVocabularies) {
			System.out.println(voc.getName());
			assertFalse(voc.isMeta());
		}
	}
}
