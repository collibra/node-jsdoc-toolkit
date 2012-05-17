package com.collibra.dgc.model.representation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.TermImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestTerm {
	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private CommunityFactory communityFactory;
	@Autowired
	private MeaningFactory meaningFactory;

	private Community sc;
	private Community sp;

	@Before
	public void setup() {
		sc = communityFactory.makeCommunity("SC1", "SC1URI");
		sp = communityFactory.makeCommunity(sc, "SP", "SPRUI");
	}

	@Test
	public void testEquals() {

		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");

		Term t1 = representationFactory.makeTerm(voc, "T1");
		Term t1Same = new TermImpl(voc, "T1", meaningFactory.makeObjectType());
		Term t2 = representationFactory.makeTerm(voc, "T2");

		assertTrue(voc.getTerms().contains(t1));
		assertTrue(voc.getTerms().contains(t1Same));
		assertTrue(voc.getTerms().contains(t2));

		assertTrue(t1.equals(t1Same));
		assertFalse(t1.equals(t2));
		assertFalse(t2.equals(t1Same));

	}

	@Test
	public void testCreateTerm() {

		final String termSignifier = "Person";

		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Term t1 = representationFactory.makeTerm(voc, termSignifier);

		assertEquals(t1.getVocabulary(), voc);
		assertTrue(voc.getTerms().contains(t1));
		assertNotNull(t1.getObjectType());
	}

	@Test
	public void testTermVerbalise() {

		final String termSignifier = "Person";

		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Term t1 = representationFactory.makeTerm(voc, termSignifier);

		assertEquals(t1.verbalise(), t1.getSignifier());
	}

	@Test
	public void testSignifier() {
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");

		try {
			// Signifier larger than 255 characters.
			representationFactory
					.makeTerm(
							voc,
							"Once a man approached and spoke to him and the detective merely replied by shaking his head Thus the night passed At dawn the half-extinguished disc of the sun rose above a misty horizon but it was now possible to recognise objects two miles off PhileasFog");
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			// Success
			Assert.assertEquals(DGCErrorCodes.TERM_SIGNIFIER_TOO_LARGE, ex.getErrorCode());
		}
	}
}
