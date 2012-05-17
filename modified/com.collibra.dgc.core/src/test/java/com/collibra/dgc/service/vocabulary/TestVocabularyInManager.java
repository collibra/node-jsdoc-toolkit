package com.collibra.dgc.service.vocabulary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.Concept;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Attribute;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.service.AbstractBootstrappedServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestVocabularyInManager extends AbstractBootstrappedServiceTest {

	@Test
	public void testIncorporateDisincorporateVocabulary() {

		Community community = communityFactory.makeCommunity("Test Community", "http://community.com");
		Vocabulary incorporatingVocabulary = representationFactory.makeVocabulary(community, "http://voc1", "voc1");
		Vocabulary incorporatedVocabulary = representationFactory.makeVocabulary(community, "http://voc2", "voc2");
		communityService.save(community);
		resetTransaction();

		incorporatingVocabulary = representationService.findVocabularyByResourceId(incorporatingVocabulary.getId());
		incorporatedVocabulary = representationService.findVocabularyByResourceId(incorporatedVocabulary.getId());

		representationService.incorporate(incorporatingVocabulary, incorporatedVocabulary);
		resetTransaction();

		incorporatingVocabulary = representationService.findVocabularyByResourceId(incorporatingVocabulary.getId());
		incorporatedVocabulary = representationService.findVocabularyByResourceId(incorporatedVocabulary.getId());

		assertTrue(incorporatingVocabulary.getIncorporatedVocabularies().contains(incorporatedVocabulary));

		representationService.disincorporateVocabulary(incorporatingVocabulary, incorporatedVocabulary);
		resetTransaction();

		incorporatingVocabulary = representationService.findVocabularyByResourceId(incorporatingVocabulary.getId());
		incorporatedVocabulary = representationService.findVocabularyByResourceId(incorporatedVocabulary.getId());

		assertFalse(incorporatingVocabulary.getIncorporatedVocabularies().contains(incorporatedVocabulary));
	}

	@Test
	public void testFindSBVRVocabulariesResourceIDs() {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns",
				"vocaulary.create.new.vocabulary");

		communityService.save(sp);
		resetTransaction();
		List<String> metaIds = representationService.findMetaVocabularyIDs();

		assertEquals(10, metaIds.size());

		for (String metaId : metaIds) {
			System.out.println("'" + metaId + "',");
		}
	}

	@Test
	public void testCreateNewVocabulary() throws Exception {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns",
				"vocaulary.create.new.vocabulary");

		communityService.save(sp);
		resetTransaction();
		Vocabulary createdVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertNotNull(createdVocabulary);
		assertNotNull(createdVocabulary.getId());
	}

	@Test
	public void testIncorporateVocabulary() throws Exception {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		Vocabulary vocabulary1 = representationFactory.makeVocabulary(sp, "com.namespace.ns1",
				"vocaulary.create.new.vocabulary1");
		Vocabulary vocabulary2 = representationFactory.makeVocabulary(sp, "com.namespace.ns2",
				"vocaulary.create.new.vocabulary2");
		vocabulary1.addIncorporatedVocabulary(vocabulary2);

		communityService.save(sp);
		resetTransaction();
		vocabulary1 = representationService.findVocabularyByResourceId(vocabulary1.getId());
		vocabulary2 = representationService.findVocabularyByResourceId(vocabulary2.getId());

		for (Vocabulary voc : vocabulary1.getAllIncorporatedVocabularies())
			System.out.println(voc.verbalise());

		assertEquals(7, vocabulary1.getAllIncorporatedVocabularies().size());
		assertEquals(6, vocabulary2.getAllIncorporatedVocabularies().size());
		assertEquals(1, vocabulary1.getAllNonSbvrIncorporatedVocabularies().size());
		assertEquals(0, vocabulary2.getAllNonSbvrIncorporatedVocabularies().size());
	}

	@Test
	public void testCreateNewVocabularyAlreadyExists() {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		String vocabularyA = "vocabularyA";
		String uri = "namespaceA";

		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, uri, vocabularyA);
		communityService.save(sp);
		resetTransaction();
		sp = communityService.findCommunity(sp.getId());

		try {
			vocabulary = representationFactory.makeVocabulary(sp, uri, vocabularyA);
			communityService.save(sp);
			// representationService.createVocabulary(vocabulary);
			fail("should be out of synch");
		} catch (IllegalArgumentException e) {
			// we expect this
		}

		vocabulary = representationFactory.makeVocabulary(sp, uri, "New Name");

		try {
			communityService.save(sp);
			// representationService.createVocabulary(vocabulary);
			fail("should be out of synch");
		} catch (IllegalArgumentException e) {
			// we expect this
		}

		// Since unsaved transient objects exist, just rollback otherwise the transaction commit will fail.
		rollback();
	}

	@Test
	public void testCreateTermInExsistingVocabularyWithLaterVersion() throws Exception {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		communityService.save(sp);
		resetTransaction();
		sp = communityService.findCommunity(sp.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);

		vocabulary.setName("renamed Vocabulary");

		communityService.save(sp);
		Vocabulary savedVocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(savedVocabulary);
		assertNotNull(savedVocabulary.getId());
		assertEquals("renamed Vocabulary", savedVocabulary.getName());
	}

	@Test
	public void testIncorporateVocabularyVersioning() throws Exception {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		Vocabulary vocabulary1 = representationFactory.makeVocabulary(sp, "com.namespace.ns1", "vocabulary 1");
		Vocabulary vocabulary2 = representationFactory.makeVocabulary(sp, "com.namespace.ns2", "vocabulary 2");

		communityService.save(sp);
		resetTransaction();

		vocabulary1 = representationService.findVocabularyByResourceId(vocabulary1.getId());
		vocabulary2 = representationService.findVocabularyByResourceId(vocabulary2.getId());

		vocabulary1.addIncorporatedVocabulary(vocabulary2);
		vocabulary1 = representationService.saveVocabulary(vocabulary1);
		resetTransaction();

		vocabulary1 = representationService.findVocabularyByResourceId(vocabulary1.getId());
		vocabulary2 = representationService.findVocabularyByResourceId(vocabulary2.getId());
		assertEquals(2, vocabulary1.getIncorporatedVocabularies().size());
		// assertEquals(vocabulary2.getVersionedId(),
		// vocabulary1.getIncorporatedVocabularies().iterator().next().getVersionedId());

		Term term1 = representationFactory.makeTerm(vocabulary1, "Term1Voc1");
		vocabulary1 = representationService.saveVocabulary(vocabulary1);
		resetTransaction();

		vocabulary1 = representationService.findVocabularyByResourceId(vocabulary1.getId());
		vocabulary2 = representationService.findVocabularyByResourceId(vocabulary2.getId());
		assertEquals(2, vocabulary1.getIncorporatedVocabularies().size());
		// assertEquals(vocabulary2.getVersionedId(),
		// vocabulary1.getIncorporatedVocabularies().iterator().next().getVersionedId());

		Term term2 = representationFactory.makeTerm(vocabulary2, "Term2Voc2");
		vocabulary2 = representationService.saveVocabulary(vocabulary2);
		resetTransaction();

		vocabulary1 = representationService.findVocabularyByResourceId(vocabulary1.getId());
		vocabulary2 = representationService.findVocabularyByResourceId(vocabulary2.getId());
		assertEquals(2, vocabulary1.getIncorporatedVocabularies().size());
		// assertEquals(vocabulary2.getVersionedId(),
		// vocabulary1.getIncorporatedVocabularies().iterator().next().getVersionedId());

		// check versioning with sbvr vocabularies
		Vocabulary sbvr = representationService.findSbvrVocabulary();
		Term equivalence = representationFactory.makeTerm(sbvr, "Equivalence");
		sbvr = representationService.saveVocabulary(sbvr);
		resetTransaction();

		sbvr = representationService.findVocabularyByResourceId(sbvr.getId());
		vocabulary1 = representationService.findVocabularyByResourceId(vocabulary1.getId());
		vocabulary2 = representationService.findVocabularyByResourceId(vocabulary2.getId());
		assertNotNull(representationService.findPreferredTermInAllIncorporatedVocabularies(equivalence.getObjectType(),
				vocabulary1));
		assertNotNull(representationService.findPreferredTermInAllIncorporatedVocabularies(equivalence.getObjectType(),
				vocabulary2));

		Vocabulary sbvrCollibra = representationService.findSbvrCollibraExtensionsVocabulary();
		Term lineage = representationFactory.makeTerm(sbvrCollibra, "Lineage");
		lineage = representationService.saveTerm(lineage);
		resetTransaction();

		sbvrCollibra = representationService.findVocabularyByResourceId(sbvrCollibra.getId());
		vocabulary1 = representationService.findVocabularyByResourceId(vocabulary1.getId());
		vocabulary2 = representationService.findVocabularyByResourceId(vocabulary2.getId());
		term1 = representationService.findTermByResourceId(term1.getId());
		assertNotNull(representationService.findPreferredTermInAllIncorporatedVocabularies(lineage.getObjectType(),
				vocabulary1));
		assertNotNull(representationService.findPreferredTermInAllIncorporatedVocabularies(lineage.getObjectType(),
				vocabulary2));

		ObjectType type = ((Concept) term1.getObjectType()).getType();
		Term conceptTypeRepresentation = representationService.findPreferredTermInAllIncorporatedVocabularies(type,
				term1.getVocabulary());
		assertNotNull(conceptTypeRepresentation);

	}

	@Test
	public void testDeleteExsistingVocabulary() throws Exception {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.namespace.ns", "vocaulary.create.term");
		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		representationService.remove(vocabulary);
		resetTransaction();

		Vocabulary foundVoc = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertNull(foundVoc);

		// Assert that all vocabulary content is obsolete.
		assertVocabularyObsolete(vocabulary);
	}

	@Test
	public void testVocabulariesToIncorporate() throws Exception {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");

		Vocabulary aVoc = representationFactory.makeVocabulary(sp, "http://localhost/autobahn/bin/view/A/", "A");
		Vocabulary bVoc = representationFactory.makeVocabulary(sp, "http://localhost/autobahn/bin/view/B/", "B");
		Vocabulary cVoc = representationFactory.makeVocabulary(sp, "http://localhost/autobahn/bin/view/C/", "C");
		Vocabulary dVoc = representationFactory.makeVocabulary(sp, "http://localhost/autobahn/bin/view/D/", "D");
		Vocabulary eVoc = representationFactory.makeVocabulary(sp, "http://localhost/autobahn/bin/view/E/", "E");
		Vocabulary fVoc = representationFactory.makeVocabulary(sp, "http://localhost/autobahn/bin/view/F/", "F");
		Vocabulary gVoc = representationFactory.makeVocabulary(sp, "http://localhost/autobahn/bin/view/G/", "G");
		Vocabulary hVoc = representationFactory.makeVocabulary(sp, "http://localhost/autobahn/bin/view/H/", "H");
		Vocabulary iVoc = representationFactory.makeVocabulary(sp, "http://localhost/autobahn/bin/view/I/", "I");

		bVoc.addIncorporatedVocabulary(aVoc);
		cVoc.addIncorporatedVocabulary(bVoc);
		dVoc.addIncorporatedVocabulary(aVoc);
		eVoc.addIncorporatedVocabulary(dVoc);
		fVoc.addIncorporatedVocabulary(cVoc);
		gVoc.addIncorporatedVocabulary(eVoc);
		aVoc.addIncorporatedVocabulary(hVoc);

		communityService.save(sp);
		resetTransaction();
		aVoc = representationService.findVocabularyByResourceId(aVoc.getId());
		List<Vocabulary> suggestedVocs = representationService.findVocabulariesToInCorporate(aVoc);
		assertNotNull(suggestedVocs);
		assertEquals(1, suggestedVocs.size());
		assertTrue(suggestedVocs.contains(iVoc));
	}

	@Test
	public void testRename() throws Exception {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "testRename");
		representationFactory.makeTerm(vocabulary, "term");
		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Vocabulary result = representationService.changeName(vocabulary, "changed testRename");
		assertEquals("changed testRename", result.getName());
		resetTransaction();

		vocabulary = representationService.findVocabularyByName("changed testRename");
		assertNotNull(vocabulary);
		assertEquals("changed testRename", vocabulary.getName());
	}

	@Test
	public void testChangeUri() throws Exception {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "testChangeUri");
		representationFactory.makeTerm(vocabulary, "term");
		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Vocabulary result = representationService.changeUri(vocabulary, "com.collibra.ns.new");
		assertEquals("com.collibra.ns.new", result.getUri());
		resetTransaction();

		vocabulary = representationService.findVocabularyByUri("com.collibra.ns.new");
		assertNotNull(vocabulary);
		assertEquals("com.collibra.ns.new", vocabulary.getUri());
	}

	@Test
	public void testChangeCommunity() {
		Community community1 = communityFactory.makeCommunity("com1", "com.1");
		Vocabulary vocabulary = representationFactory.makeVocabulary(community1, "com.collibra.ns",
				"testChangeCommunity");
		communityService.save(community1);
		assertTrue(community1.getVocabularies().contains(vocabulary));
		resetTransaction();

		Community community2 = communityFactory.makeCommunity("com2", "com.2");
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		communityService.save(community2);
		assertFalse(community2.getVocabularies().contains(vocabulary));
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		community1 = communityService.findCommunity(community1.getId());
		community2 = communityService.findCommunity(community2.getId());
		vocabulary = representationService.changeCommunity(vocabulary, community2);
		assertNotNull(vocabulary.getCommunity());
		assertEquals(community2, vocabulary.getCommunity());
		assertTrue(community2.getVocabularies().contains(vocabulary));
		assertFalse(community1.getVocabularies().contains(vocabulary));
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		community1 = communityService.findCommunity(community1.getId());
		community2 = communityService.findCommunity(community2.getId());
		assertNotNull(vocabulary.getCommunity());
		assertEquals(community2, vocabulary.getCommunity());
		assertTrue(community2.getVocabularies().contains(vocabulary));
		assertFalse(community1.getVocabularies().contains(vocabulary));
	}

	/**
	 * #8186. Delete a vocabulary that was incorporated
	 */
	@Test
	public void testDisincorporationWithVocabularyRemove() {
		Community sp = communityFactory.makeCommunity("SP", "SPRUI");
		Vocabulary voc1 = representationFactory.makeVocabulary(sp, "com.collibra.ns1", "voc1");
		Vocabulary voc2 = representationFactory.makeVocabulary(sp, "com.collibra.ns2", "voc2");
		Vocabulary voc3 = representationFactory.makeVocabulary(sp, "com.collibra.ns3", "voc3");
		Vocabulary incVocabulary = representationFactory.makeVocabulary(sp, "incorporated vocabulary ns",
				"incorporated vocabulary");
		voc1.addIncorporatedVocabulary(incVocabulary);
		voc2.addIncorporatedVocabulary(incVocabulary);
		voc3.addIncorporatedVocabulary(incVocabulary);
		communityService.save(sp);
		resetTransaction();

		voc1 = representationService.findVocabularyByResourceId(voc1.getId());
		voc2 = representationService.findVocabularyByResourceId(voc2.getId());
		voc3 = representationService.findVocabularyByResourceId(voc3.getId());
		incVocabulary = representationService.findVocabularyByResourceId(incVocabulary.getId());

		assertTrue(voc1.getIncorporatedVocabularies().contains(incVocabulary));
		assertTrue(voc2.getIncorporatedVocabularies().contains(incVocabulary));
		assertTrue(voc3.getIncorporatedVocabularies().contains(incVocabulary));

		representationService.remove(incVocabulary);
		resetTransaction();

		voc1 = representationService.findVocabularyByResourceId(voc1.getId());
		incorporateVocabularyNotFound(voc1, incVocabulary.getId());
		voc2 = representationService.findVocabularyByResourceId(voc2.getId());
		incorporateVocabularyNotFound(voc2, incVocabulary.getId());
		voc3 = representationService.findVocabularyByResourceId(voc3.getId());
		incorporateVocabularyNotFound(voc3, incVocabulary.getId());
	}

	private void incorporateVocabularyNotFound(Vocabulary vocabulary, String incVocResourceId) {
		for (Vocabulary incVoc : vocabulary.getIncorporatedVocabularies()) {
			Assert.assertNotSame(incVocResourceId, incVoc.getId());
		}
	}

	private void assertVocabularyObsolete(Vocabulary vocabulary) {
		for (Term term : vocabulary.getTerms()) {
			term = representationService.findTermByResourceId(term.getId());
			assertNull(term);
			assertAttributesRelevance(term);
		}

		for (BinaryFactTypeForm bftf : vocabulary.getBinaryFactTypeForms()) {
			bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
			assertNull(bftf);
			assertAttributesRelevance(bftf);
		}

		for (CharacteristicForm cf : vocabulary.getCharacteristicForms()) {
			cf = representationService.findCharacteristicFormByResourceId(cf.getId());
			assertNull(cf);
			assertAttributesRelevance(cf);
		}
	}

	private void assertAttributesRelevance(Representation representation) {
		for (Attribute att : representation.getAttributes()) {
			att = attributeService.getAttribute(att.getId());
			assertNull(att);
		}
	}
}
