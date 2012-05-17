package com.collibra.dgc.service.representation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.dao.impl.RepresentationDaoHibernate;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.FactTypeRole;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.service.impl.ConstraintChecker;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * 
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestSynonyms extends AbstractServiceTest {
	private Vocabulary vocabulary;

	@Test
	public void testWithAttributes() throws Exception {
		vocabulary = representationFactory.makeVocabulary(sp, "My Test", "Synonyms Test");
		Term t1 = representationFactory.makeTerm(vocabulary, "Term1");
		Term t2 = representationFactory.makeTerm(vocabulary, "Term2");

		communityService.save(sp);
		resetTransaction();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		// Find the term
		t1 = representationService.findTermBySignifier(vocabulary, "Term1");
		assertNotNull(t1);

		// Add attribute
		representationFactory.makeStringAttribute(attributeService.findMetaDescription(), t1, "test");
		representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), t1, "Definition");
		representationService.saveTerm(t1);
		resetTransaction();

		// Add as synonym
		t1 = representationService.findTermByResourceId(t1.getId());
		t2 = representationService.findTermByResourceId(t2.getId());
		representationService.addSynonym(t1, t2);
		resetTransaction();

		// Assert on synonyms
		t1 = representationService.findTermByResourceId(t1.getId());
		t2 = representationService.findTermByResourceId(t2.getId());
		List<Term> synonyms = representationService.findSynonyms(t1);
		assertEquals(1, synonyms.size());
		assertEquals(t2.getId(), synonyms.get(0).getId());

		// Remove synonym.
		representationService.removeSynonym(t2);
		resetTransaction();

		// Assert on synonyms
		t1 = representationService.findTermByResourceId(t1.getId());
		synonyms = representationService.findSynonyms(t1);
		assertEquals(0, synonyms.size());
	}

	// FIXME
	// @Test
	// public void testChangeMeaning() {
	// SemanticCommunity sc = communityFactory.makeSemanticCommunity("Test", "TestURI");
	// Community spc = communityFactory.makeCommunity(sc, "Test Sub", "TestSubURI");
	// Vocabulary voc = representationFactory.makeVocabulary(spc, "Test", "uri");
	// Term t0 = representationFactory.makeTerm(voc, "t0");
	// Term t1 = representationFactory.makeTerm(voc, "t1");
	// Term t2 = representationFactory.makeTerm(voc, "t2");
	//
	// communityService.commit(sc);
	// resetTransaction();
	//
	// voc = representationService.findVocabularyByResourceId(voc.getResourceId());
	// t0 = representationService.findTermByResourceId(t0.getResourceId());
	// t1 = representationService.findTermByResourceId(t1.getResourceId());
	// t2 = representationService.findTermByResourceId(t2.getResourceId());
	//
	// // prepare a taxonomy t0 < t1 & t2
	// ObjectType ot0 = t0.getObjectType();
	// ObjectType ot1 = t1.getObjectType();
	// ObjectType ot2 = t2.getObjectType();
	// ot1.setGeneralConcept(ot0);
	// ot2.setGeneralConcept(ot0);
	// meaningService.createObjectType(ot0);
	// meaningService.createObjectType(ot1);
	// meaningService.createObjectType(ot2);
	//
	// Vocabulary persistedVoc = representationService.createVocabulary(voc);
	// t0 = persistedVoc.getTerm(t0.getSignifier());
	// t1 = persistedVoc.getTerm(t1.getSignifier());
	// t2 = persistedVoc.getTerm(t2.getSignifier());
	//
	// ot0 = t0.getObjectType();
	// ot1 = t1.getObjectType();
	// ot2 = t2.getObjectType();
	//
	// Term t1Syn = t1.changeObjectType(t2.getObjectType());
	// t1Syn = representationService.createTerm(t1Syn);
	// ObjectType ot1_2 = meaningService.createObjectType(t1.getObjectType());
	// assertTrue(ot1.equals(ot1_2));
	// assertFalse(ot1_2.getTerms().contains(t1));
	//
	// resetTransaction();
	//
	// // now after reloading from db
	// ObjectType ot0reloaded = meaningService.findObjectTypeByResourceId(ot0.getResourceId());
	// ObjectType ot1reloaded = meaningService.findObjectTypeByResourceId(ot1.getResourceId());
	// ObjectType ot1_2reloaded = meaningService.findObjectTypeByResourceId(ot1_2.getResourceId());
	// ObjectType ot2reloaded = meaningService.findObjectTypeByResourceId(ot2.getResourceId());
	// assertFalse(ot1_2reloaded.getTerms().contains(t1));
	// assertTrue(ot1reloaded.getTerms().contains(t1));
	//
	// // test the hierarchy (should not say anymore that t1 is a subclass of t0)
	// List<Concept> specializedConcepts = meaningService.findAllSpecializedConcepts(ot0reloaded);
	// assertEquals(2, specializedConcepts.size());
	// assertTrue(specializedConcepts.contains(ot1_2reloaded));
	// assertTrue(specializedConcepts.contains(ot2reloaded));
	// resetTransaction();
	// }

	@Test
	public void testCreateSynonymViaService() {
		Vocabulary voc = createSampleVocabulary();
		Term auto = representationFactory.makeTerm(voc, "auto");
		Term car = representationFactory.makeTerm(voc, "car");
		voc = representationService.saveVocabulary(voc);
		auto = voc.getTerm("auto");
		car = voc.getTerm("car");
		assertFalse(auto.equals(car));
		assertFalse(auto.getObjectType().equals(car.getObjectType()));

		car = representationService.createSynonym(auto, car);
		auto = representationService.findTermByResourceId(auto.getId());
		car = representationService.findTermByResourceId(car.getId());
		assertEquals(auto.getObjectType(), car.getObjectType());
		List<Term> autoSynonyms = representationService.findSynonyms(auto);
		List<Term> carSynonyms = representationService.findSynonyms(car);

		// synonyms should only refer to each other and should not list themselves
		assertEquals(1, autoSynonyms.size());
		assertEquals(1, carSynonyms.size());
		assertTrue(autoSynonyms.contains(car));
		assertFalse(autoSynonyms.contains(auto));
		assertTrue(carSynonyms.contains(auto));
		assertFalse(carSynonyms.contains(car));
	}

	@Test
	public void testRemoveSynonymViaService() {
		// first create the synonyms
		// we mostly assume this is correct since we test the same in #testCreateSynonymViaService()
		Vocabulary voc = createSampleVocabulary();
		Term auto = representationFactory.makeTerm(voc, "auto");
		Term car = representationFactory.makeTerm(voc, "car");
		voc = representationService.saveVocabulary(voc);
		resetTransaction();

		auto = representationService.findTermByResourceId(auto.getId());
		car = representationService.findTermByResourceId(car.getId());

		car = representationService.createSynonym(auto, car);
		assertEquals(auto.getObjectType(), car.getObjectType());
		resetTransaction();

		auto = representationService.findTermByResourceId(auto.getId());
		car = representationService.findTermByResourceId(car.getId());

		// now we will break the synonimity
		car = (Term) representationService.removeSynonym(car);
		resetTransaction();

		auto = representationService.findTermByResourceId(auto.getId());
		car = representationService.findTermByResourceId(car.getId());

		// test via object type
		assertFalse(auto.getObjectType().equals(car.getObjectType()));

		// test via lookup via service
		List<Term> autoSynonyms = representationService.findSynonyms(auto);
		List<Term> carSynonyms = representationService.findSynonyms(car);
		assertEquals(0, autoSynonyms.size());
		assertEquals(0, carSynonyms.size());
	}

	@Test
	public void testRemoveSynonymViaServiceWithNewMethod() {
		// first create the synonyms
		// we mostly assume this is correct since we test the same in #testCreateSynonymViaService()
		Vocabulary voc = createSampleVocabulary();
		Term auto = representationFactory.makeTerm(voc, "auto");
		Term car = representationFactory.makeTerm(voc, "car");
		voc = representationService.saveVocabulary(voc);
		resetTransaction();

		auto = representationService.findTermByResourceId(auto.getId());
		car = representationService.findTermByResourceId(car.getId());

		auto = representationService.createSynonym(car, auto);
		assertEquals(auto.getObjectType(), car.getObjectType());
		assertTrue(car.getIsPreferred());
		assertFalse(auto.getIsPreferred());
		resetTransaction();

		auto = representationService.findTermByResourceId(auto.getId());
		car = representationService.findTermByResourceId(car.getId());

		// now we will break the synonimity
		car = (Term) representationService.removeSynonym(car);
		resetTransaction();

		auto = representationService.findTermByResourceId(auto.getId());
		car = representationService.findTermByResourceId(car.getId());

		// Test that now the auto is the preferred Term cause car was removed as a synonym
		assertTrue(auto.getIsPreferred());

		// test via object type
		assertFalse(auto.getObjectType().equals(car.getObjectType()));

		// test via lookup via service
		List<Term> autoSynonyms = representationService.findSynonyms(auto);
		List<Term> carSynonyms = representationService.findSynonyms(car);
		assertEquals(0, autoSynonyms.size());
		assertEquals(0, carSynonyms.size());
	}

	/**
	 * create term T1 create term T2 set T2 as synonym of T1 remove synonymity => all is ok
	 * 
	 * create term T3 set T2 as synonym of T3 => now T1, T2 and T3 are all synonyms
	 */
	@Test
	public void testHSBCIssue() {
		Vocabulary voc = createSampleVocabulary();
		Term T1 = representationFactory.makeTerm(voc, "T1");
		Term T2 = representationFactory.makeTerm(voc, "T2");
		voc = representationService.saveVocabulary(voc);
		// communityService.commit(sc);
		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());

		representationService.createSynonym(T1, T2);

		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		assertEquals(1, representationService.findSynonyms(T1).size());
		assertEquals(1, representationService.findSynonyms(T2).size());

		representationService.removeSynonym(T2);

		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		voc = representationService.findVocabularyByResourceId(voc.getId());
		assertEquals(0, representationService.findSynonyms(T1).size());
		assertEquals(0, representationService.findSynonyms(T2).size());

		Term T3 = representationFactory.makeTerm(voc, "T3");
		voc = representationService.saveVocabulary(voc);

		resetTransaction();

		// T2 is synonym of T3
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());
		representationService.createSynonym(T3, T2);

		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());
		assertEquals(0, representationService.findSynonyms(T1).size());
		assertEquals(1, representationService.findSynonyms(T2).size());
		assertEquals(1, representationService.findSynonyms(T3).size());

		representationService.removeSynonym(T2);
		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());
		assertEquals(0, representationService.findSynonyms(T1).size());
		assertEquals(0, representationService.findSynonyms(T2).size());
		assertEquals(0, representationService.findSynonyms(T3).size());

		// T3 is synonym of T2
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());
		representationService.createSynonym(T2, T3);

		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());
		assertEquals(0, representationService.findSynonyms(T1).size());
		assertEquals(1, representationService.findSynonyms(T2).size());
		assertEquals(1, representationService.findSynonyms(T3).size());

		representationService.removeSynonym(T2);
		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());
		assertEquals(0, representationService.findSynonyms(T1).size());
		assertEquals(0, representationService.findSynonyms(T2).size());
		assertEquals(0, representationService.findSynonyms(T3).size());

		// T1, T2, T3 are synonyms of each other
		representationService.createSynonym(T1, T2);
		representationService.createSynonym(T1, T3);
		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());
		assertEquals(2, representationService.findSynonyms(T1).size());
		assertEquals(2, representationService.findSynonyms(T2).size());
		assertEquals(2, representationService.findSynonyms(T3).size());

		// Remove all synonyms
		representationService.removeSynonym(T2);
		representationService.removeSynonym(T3);
		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());
		assertEquals(0, representationService.findSynonyms(T1).size());
		assertEquals(0, representationService.findSynonyms(T2).size());
		assertEquals(0, representationService.findSynonyms(T3).size());
	}

	/**
	 * create term T1 create term T2 set T2 as synonym of T1 remove synonymity => all is ok
	 * 
	 * create term T3 set T2 as synonym of T3 => now T1, T2 and T3 are all synonyms
	 */
	@Test
	public void testSynonymsAcrossSemanticCommunities() {
		// Set up semantic community hierarchy 1
		Community sc1 = communityFactory.makeCommunity("SC1", "SC1 URI");
		Community sp1 = communityFactory.makeCommunity(sc1, "SP1", "SP1 URI");
		Vocabulary voc1 = representationFactory.makeVocabulary(sp1, "VOC 1 URI", "VOC 1");
		Term T1 = representationFactory.makeTerm(voc1, "T1");
		communityService.save(sc1);

		// Setup semantic community hierarchy 2
		Community sc2 = communityFactory.makeCommunity("SC2", "SC2 URI");
		Community sp2 = communityFactory.makeCommunity(sc2, "SP2", "SP2 URI");
		Vocabulary voc2 = representationFactory.makeVocabulary(sp2, "VOC 2 URI", "VOC 2");
		Term T2 = representationFactory.makeTerm(voc2, "T2");
		communityService.save(sc2);

		resetTransaction();

		// Make T1 and T2 as synonyms
		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());

		assertNotNull(T1);
		assertNotNull(T2);

		representationService.createSynonym(T1, T2);
		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());

		assertEquals(1, representationService.findSynonyms(T1).size());
		assertEquals(1, representationService.findSynonyms(T2).size());

		// Remove T2 and T1 as synonyms
		representationService.removeSynonym(T2);
		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());

		assertEquals(0, representationService.findSynonyms(T1).size());
		assertEquals(0, representationService.findSynonyms(T2).size());

		// Create term T3 in voc2.
		voc2 = representationService.findVocabularyByResourceId(voc2.getId());
		assertNotNull(voc2);
		Term T3 = representationFactory.makeTerm(voc2, "T3");
		representationService.saveVocabulary(voc2);
		resetTransaction();

		// Make T1 and T3 as synonyms
		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());

		representationService.createSynonym(T1, T3);
		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());

		assertEquals(1, representationService.findSynonyms(T1).size());
		assertEquals(0, representationService.findSynonyms(T2).size());
		assertEquals(1, representationService.findSynonyms(T3).size());

		// T1 T2 and T3 synonyms
		representationService.createSynonym(T1, T2);
		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());

		assertEquals(2, representationService.findSynonyms(T1).size());
		assertEquals(2, representationService.findSynonyms(T2).size());
		assertEquals(2, representationService.findSynonyms(T3).size());

		// Remove T1 and T3 as synonyms
		representationService.removeSynonym(T3);
		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());

		assertEquals(1, representationService.findSynonyms(T1).size());
		assertEquals(1, representationService.findSynonyms(T2).size());
		assertEquals(0, representationService.findSynonyms(T3).size());

		// Remove T1 and T2 as synonyms
		representationService.removeSynonym(T2);
		resetTransaction();

		T1 = representationService.findTermByResourceId(T1.getId());
		T2 = representationService.findTermByResourceId(T2.getId());
		T3 = representationService.findTermByResourceId(T3.getId());

		assertEquals(0, representationService.findSynonyms(T1).size());
		assertEquals(0, representationService.findSynonyms(T2).size());
		assertEquals(0, representationService.findSynonyms(T3).size());
	}

	@Test
	public void testBug5847() {
		// Set up semantic community hierarchy 1
		Community sc1 = communityFactory.makeCommunity("SC1", "SC1 URI");
		Community sp1 = communityFactory.makeCommunity(sc1, "SP1", "SP1 URI");
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp1, "VOC 1 URI", "VOC 1");
		Term Original = representationFactory.makeTerm(vocabulary, "Original");
		Term Synonym = representationFactory.makeTerm(vocabulary, "Synonym");
		Term SuperType = representationFactory.makeTerm(vocabulary, "SuperType");
		communityService.save(sc1);
		resetTransaction();

		Original = representationService.findTermByResourceId(Original.getId());
		SuperType = representationService.findTermByResourceId(SuperType.getId());
		representationService.setGeneralConceptRepresentation(Original, SuperType);
		resetTransaction();

		Synonym = representationService.findTermByResourceId(Synonym.getId());
		SuperType = representationService.findTermByResourceId(SuperType.getId());
		representationService.setGeneralConceptRepresentation(Synonym, SuperType);
		resetTransaction();

		Original = representationService.findTermByResourceId(Original.getId());
		Synonym = representationService.findTermByResourceId(Synonym.getId());
		representationService.addSynonym(Original, Synonym);
		resetTransaction();

		Original = representationService.findTermByResourceId(Original.getId());
		Term newStatus = representationService.findTermBySignifier(representationService.findStatusesVocabulary(),
				"Accepted");
		representationService.changeStatus(Original, newStatus);
		resetTransaction();

		Synonym = representationService.findTermByResourceId(Synonym.getId());
		newStatus = representationService.findTermBySignifier(representationService.findStatusesVocabulary(),
				"Obsolete");
		representationService.changeStatus(Synonym, newStatus);
		resetTransaction();

		Original = representationService.findTermByResourceId(Original.getId());
		List<Term> synonyms = representationService.findSynonyms(Original);
		Assert.assertTrue(synonyms.size() > 0);
	}

	/**
	 * Steps:
	 * 
	 * 1. Create two terms ATerm and synA.
	 * <p>
	 * 2. Goto synA term and change its general concept to ATerm.
	 * <p>
	 * 3. Goto ATerm and add synA as the synonym.
	 * <p>
	 * 4. Now you will see that ATerm is the general concept of ATerm as shown in the attached screen shot.
	 * <p>
	 * The root cause of the problem is because of the method {@link RepresentationDaoHibernate}
	 * .findPreferredRepresentation(Concept concept, Vocabulary vocabulary) not using 'isLatest' in the second query.
	 * Hence the problem.
	 */
	@Test
	public void testBug8397() {
		// 1. Create two terms ATerm and synA.
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "VOC 1 URI", "VOC 1");
		Term ATerm = representationFactory.makeTerm(vocabulary, "ATerm");
		Term synA = representationFactory.makeTerm(vocabulary, "synA");
		communityService.save(sp);
		resetTransaction();

		// 2. Goto synA term and change its general concept to ATerm.
		ATerm = representationService.findTermByResourceId(ATerm.getId());
		synA = representationService.findTermByResourceId(synA.getId());

		synA.getObjectType().setGeneralConcept(ATerm.getObjectType());
		resetTransaction();

		// 3. Goto ATerm and add synA as the synonym.
		ATerm = representationService.findTermByResourceId(ATerm.getId());
		synA = representationService.findTermByResourceId(synA.getId());
		ObjectType savedOT = ATerm.getObjectType();
		representationService.createSynonym(synA, ATerm);
		resetTransaction();

		// 4. Solved the issue. The preferred term should be null.
		savedOT = meaningService.findObjectTypeByResourceId(savedOT.getId());
		Representation perferredRepresentation = representationService.findPreferredRepresentation(savedOT,
				ATerm.getVocabulary());
		Assert.assertNull(perferredRepresentation);
	}

	/**
	 * Create Terms A,B,C in the same vocabulary.
	 * <p>
	 * Goto page A and make it synonym of B. Now A is not a preferred term anymore.
	 * <p>
	 * Goto page B and make it synonym of C. Now B is not a preferred term anymore.
	 * <p>
	 * Now the problem is that A is not preferred term but without synonyms.
	 */
	@Test
	public void testBug8452() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "VOC 1 URI", "VOC 1");
		Term A = representationFactory.makeTerm(vocabulary, "A");
		Term B = representationFactory.makeTerm(vocabulary, "B");
		Term C = representationFactory.makeTerm(vocabulary, "C");
		communityService.save(sp);
		resetTransaction();

		A = representationService.findTermByResourceId(A.getId());
		B = representationService.findTermByResourceId(B.getId());
		representationService.createSynonym(B, A);
		resetTransaction();

		A = representationService.findTermByResourceId(A.getId());
		B = representationService.findTermByResourceId(B.getId());
		assertTrue(B.getIsPreferred());
		assertFalse(A.getIsPreferred());

		C = representationService.findTermByResourceId(C.getId());
		representationService.createSynonym(C, B);
		resetTransaction();

		A = representationService.findTermByResourceId(A.getId());
		B = representationService.findTermByResourceId(B.getId());
		C = representationService.findTermByResourceId(C.getId());
		assertTrue(C.getIsPreferred());
		assertFalse(B.getIsPreferred());
		assertTrue(A.getIsPreferred());
	}

	/**
	 * #9191. UML import is failed for my old community
	 * 
	 * The possible reason is that when using synonym operations the meanings lose their representations. And as a side
	 * effect the {@link ConstraintChecker} was trying to check the head and tail {@link FactTypeRole} of a
	 * {@link BinaryFactType} that has no {@link BinaryFactTypeForm}s associated. This was causing the problem. Now it
	 * should be fixed.
	 */
	@Test
	public void testBug9191() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "VOC 1 URI", "VOC 1");
		Term head = representationFactory.makeTerm(vocabulary, "HEAD");
		Term tail = representationFactory.makeTerm(vocabulary, "TAIL");
		BinaryFactTypeForm bftf = representationFactory
				.makeBinaryFactTypeForm(vocabulary, head, "role", "corole", tail);

		BinaryFactTypeForm bftfSyn = representationFactory.makeBinaryFactTypeForm(vocabulary, head, "role2", "corole2",
				tail);
		communityService.save(sp);
		resetTransaction();

		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		bftfSyn = representationService.findBinaryFactTypeFormByResourceId(bftfSyn.getId());
		representationService.addSynonym(bftf, bftfSyn);
		resetTransaction();

		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		bftfSyn = representationService.findBinaryFactTypeFormByResourceId(bftfSyn.getId());
		List<BinaryFactTypeForm> synonyms = representationService.findSynonymousForms(bftf);
		Assert.assertEquals(1, synonyms.size());
		Assert.assertEquals(bftfSyn, synonyms.get(0));

		representationService.removeSynonym(bftfSyn);
		resetTransaction();

		// After removing synonym the old meaning without representation is causing the problem because head and tail
		// fact type roles are not present. Hence was causing exception. A check is being made to avoid this.
		sp = communityService.findCommunity(sp.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		representationFactory.makeTerm(vocabulary, "X");
		communityService.save(sp);
		resetTransaction();

		// The new meaning after removing from the synonym should have the representation. This is also fixed.
		bftfSyn = representationService.findBinaryFactTypeFormByResourceId(bftfSyn.getId());
		Assert.assertEquals(1, bftfSyn.getBinaryFactType().getBinaryFactTypeForms().size());
	}
}
