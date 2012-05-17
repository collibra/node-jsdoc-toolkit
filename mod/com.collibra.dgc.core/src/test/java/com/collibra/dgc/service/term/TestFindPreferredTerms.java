package com.collibra.dgc.service.term;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestFindPreferredTerms extends AbstractServiceTest {

	@Test
	public void testFindAllPreferredTerms() {
		Community community = communityFactory.makeCommunity("My Community", "My Community URI");

		community = communityService.save(community);
		resetTransaction();

		community = communityService.findCommunity(community.getId());

		Vocabulary myVoc = representationFactory.makeVocabulary(community, "uri", "namev1");
		Term term1 = representationFactory.makeTerm(myVoc, "Term1");
		Term term2 = representationFactory.makeTerm(myVoc, "Term2");
		Term term3 = representationFactory.makeTerm(myVoc, "Term3");
		communityService.save(community);
		resetTransaction();

		myVoc = representationService.findVocabularyByResourceId(myVoc.getId());
		Term synonym1 = representationFactory.makeTerm(myVoc, "Synonym1");
		synonym1 = representationService.saveTerm(synonym1);
		final String preOTID = synonym1.getObjectType().getId();
		representationService.createSynonym(term1, synonym1);

		ObjectType synonymOT = meaningService.findObjectTypeByResourceId(preOTID);

		resetTransaction();

		synonymOT = meaningService.findObjectTypeByResourceId(preOTID);

		term1 = representationService.findTermByResourceId(term1.getId());
		term2 = representationService.findTermByResourceId(term2.getId());
		term3 = representationService.findTermByResourceId(term3.getId());
		myVoc = representationService.findVocabularyByResourceId(myVoc.getId());

		List<Term> preferredTerms = representationService.findAllPreferredTerms(myVoc);
		assertEquals(3, preferredTerms.size());
		assertFalse(preferredTerms.contains(synonym1));
		assertTrue(preferredTerms.contains(term1));
		assertTrue(preferredTerms.contains(term2));
		assertTrue(preferredTerms.contains(term3));
	}

	@Test
	public void testFindAllPreferredTermsWithIncorporatedVocabulari() {
		Community community = communityFactory.makeCommunity("My Community", "My Community URI");

		community = communityService.save(community);
		resetTransaction();

		community = communityService.findCommunity(community.getId());

		Vocabulary myVoc = representationFactory.makeVocabulary(community, "uri", "namev1");
		Term term1 = representationFactory.makeTerm(myVoc, "Term1");
		Term term2 = representationFactory.makeTerm(myVoc, "Term2");
		Term term3 = representationFactory.makeTerm(myVoc, "Term3");

		communityService.save(community);
		resetTransaction();
		myVoc = representationService.findVocabularyByResourceId(myVoc.getId());

		Term synonym1 = representationFactory.makeTerm(myVoc, "Synonym1");
		synonym1 = representationService.saveTerm(synonym1);
		representationService.createSynonym(term1, synonym1);
		resetTransaction();

		term1 = representationService.findTermByResourceId(term1.getId());
		term2 = representationService.findTermByResourceId(term2.getId());
		term3 = representationService.findTermByResourceId(term3.getId());
		myVoc = representationService.findVocabularyByResourceId(myVoc.getId());

		List<Term> preferredTerms = representationService.findAllPreferredTerms(myVoc);
		assertEquals(3, preferredTerms.size());
		assertFalse(preferredTerms.contains(synonym1));
		assertTrue(preferredTerms.contains(term1));
		assertTrue(preferredTerms.contains(term2));
		assertTrue(preferredTerms.contains(term3));
	}
}
