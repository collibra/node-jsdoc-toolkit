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
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCharacteristicForm {

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
		CharacteristicForm cf = representationFactory.makeCharacteristicForm(voc, t1, "is married");
		CharacteristicForm cfSame = representationFactory.makeCharacteristicForm(voc, t1, "is married");
		CharacteristicForm cf2 = representationFactory.makeCharacteristicForm(voc, t1, "is unque");
		assertFalse(cf.equals(cf2));
		assertTrue(cf.equals(cfSame));
		assertFalse(cfSame.equals(cf2));
	}

	@Test
	public void testCreateCharacteristicForm() {

		final String termSignifier = "Person";
		final String role = "is married";

		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Term t1 = representationFactory.makeTerm(voc, termSignifier);
		CharacteristicForm cf1 = representationFactory.makeCharacteristicForm(voc, t1, role);

		assertEquals(cf1.getVocabulary(), voc);
		assertTrue(voc.getCharacteristicForms().contains(cf1));
		assertTrue(cf1.getTerm().equals(t1));
		assertNotNull(cf1.getCharacteristic());

	}

	@Test
	public void testCharacteristicFormVerbalise() {

		final String termSignifier = "Person";
		final String role = "is married";

		final String verbalise = termSignifier + " " + role;

		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Term t1 = representationFactory.makeTerm(voc, termSignifier);
		CharacteristicForm cf1 = representationFactory.makeCharacteristicForm(voc, t1, role);

		assertEquals(cf1.verbalise(), verbalise);

	}

	@Test
	public void testGetTerm() {

		final String termSignifier = "Person";
		final String role = "is married";

		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Term t1 = representationFactory.makeTerm(voc, termSignifier);
		CharacteristicForm cf1 = representationFactory.makeCharacteristicForm(voc, t1, role);

		assertEquals(cf1.getTerm(), t1);

	}

	@Test
	public void testGetRole() {

		final String termSignifier = "Person";
		final String role = "is married";

		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Term t1 = representationFactory.makeTerm(voc, termSignifier);
		CharacteristicForm cf1 = representationFactory.makeCharacteristicForm(voc, t1, role);

		assertEquals(cf1.getRole(), role);

	}
}
