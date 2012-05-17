package com.collibra.dgc.model.representation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestBinaryFactTypeForm {

	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private CommunityFactory communityFactory;

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
		Term t2 = representationFactory.makeTerm(voc, "T2");
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(voc, t1, "has", "of", t2);
		BinaryFactTypeForm bftfSame = representationFactory.makeBinaryFactTypeForm(voc, t1, "has", "of", t2);
		BinaryFactTypeForm bftf2 = representationFactory.makeBinaryFactTypeForm(voc, t1, "has2", "of2", t2);
		assertFalse(bftf.equals(bftf2));
		assertTrue(bftf.equals(bftfSame));
		assertFalse(bftfSame.equals(bftf2));
	}

	@Test
	public void testCreateBinaryFactTypeForm() {
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Term t1 = representationFactory.makeTerm(voc, "T1");
		Term t2 = representationFactory.makeTerm(voc, "T2");
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(voc, t1, "has", "of", t2);
		assertEquals(bftf.getHeadTerm(), t1);
		assertEquals(bftf.getTailTerm(), t2);
		assertNotNull(bftf.getBinaryFactType());
	}

	@Test
	public void testBinaryFactTypeFormVerbalise() {
		final String verbalise = "T1 has / of T2";
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Term t1 = representationFactory.makeTerm(voc, "T1");
		Term t2 = representationFactory.makeTerm(voc, "T2");
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(voc, t1, "has", "of", t2);

		assertEquals(bftf.verbalise(), verbalise);
	}

}
