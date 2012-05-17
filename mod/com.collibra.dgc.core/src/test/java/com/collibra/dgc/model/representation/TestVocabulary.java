package com.collibra.dgc.model.representation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.facttype.BinaryFactType;
import com.collibra.dgc.core.model.meaning.facttype.Characteristic;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.CharacteristicForm;
import com.collibra.dgc.core.model.rules.RuleFactory;
import com.collibra.dgc.core.model.rules.RuleSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestVocabulary {
	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private CommunityFactory communityFactory;
	@Autowired
	private MeaningFactory meaningFactory;
	@Autowired
	private RuleFactory ruleFactory;

	private Community sc;
	private Community sp;

	@Before
	public void setup() {
		sc = communityFactory.makeCommunity("SC1", "SC1URI");
		sp = communityFactory.makeCommunity(sc, "SP", "SPRUI");
	}

	@Test
	public void testGetBinaryFactTypeForms() {
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Vocabulary voc2 = representationFactory.makeVocabulary(sp, "vuri2", "vname2");

		Term t1 = representationFactory.makeTerm(voc, "T1");
		Term t2 = representationFactory.makeTerm(voc, "T2");
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(voc, t1, "has", "of", t2);
		BinaryFactTypeForm bftfSame = representationFactory.makeBinaryFactTypeForm(voc, t1, "has", "of", t2);
		BinaryFactTypeForm bftf2 = representationFactory.makeBinaryFactTypeForm(voc, t1, "has2", "of2", t2);
		assertTrue(voc.getBinaryFactTypeForms().contains(bftf));
		assertTrue(voc.getBinaryFactTypeForms().contains(bftfSame));
		assertTrue(voc.getBinaryFactTypeForms().contains(bftf2));

		BinaryFactTypeForm bftfInOtherVoc = representationFactory.makeBinaryFactTypeForm(voc2, t1, "has", "of", t2);
		assertFalse(voc.getBinaryFactTypeForms().contains(bftfInOtherVoc));
		assertTrue(voc2.getBinaryFactTypeForms().contains(bftfInOtherVoc));

	}

	@Test
	public void testGetCharacteristicForms() {
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Vocabulary voc2 = representationFactory.makeVocabulary(sp, "vuri2", "vname2");

		Term t1 = representationFactory.makeTerm(voc, "T1");
		Term t2 = representationFactory.makeTerm(voc, "T2");
		CharacteristicForm cf = representationFactory.makeCharacteristicForm(voc, t1, "is married");
		CharacteristicForm cfSame = representationFactory.makeCharacteristicForm(voc, t1, "is married");
		CharacteristicForm cf2 = representationFactory.makeCharacteristicForm(voc, t2, "is married");
		assertTrue(voc.getCharacteristicForms().contains(cf));
		assertTrue(voc.getCharacteristicForms().contains(cfSame));
		assertTrue(voc.getCharacteristicForms().contains(cf2));

		CharacteristicForm cfInOtherVoc = representationFactory.makeCharacteristicForm(voc2, t1, "is married");
		assertFalse(voc.getCharacteristicForms().contains(cfInOtherVoc));
		assertTrue(voc2.getCharacteristicForms().contains(cfInOtherVoc));

	}

	@Test
	public void testGetTerms() {
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Vocabulary voc2 = representationFactory.makeVocabulary(sp, "vuri2", "vname2");

		Term t1 = representationFactory.makeTerm(voc, "T1");
		Term t2 = representationFactory.makeTerm(voc, "T2");
		assertTrue(voc.getTerms().contains(t1));
		assertTrue(voc.getTerms().contains(t2));

		Term t1InOtherVoc = representationFactory.makeTerm(voc2, "T1");
		assertFalse(voc.getTerms().contains(t1InOtherVoc));
		assertTrue(voc2.getTerms().contains(t1InOtherVoc));

	}

	@Test
	public void testGetIncorporatedVocabularies() {
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri1", "vname1");
		Vocabulary voc2 = representationFactory.makeVocabulary(sp, "vuri2", "vname2");
		Vocabulary incVoc1 = representationFactory.makeVocabulary(sp, "incVocuri1", "incVocname1");
		Vocabulary incVoc2 = representationFactory.makeVocabulary(sp, "incVocuri2", "incVocname2");

		voc.addIncorporatedVocabulary(incVoc1);
		voc.addIncorporatedVocabulary(incVoc2);
		assertTrue(voc.getIncorporatedVocabularies().contains(incVoc1));
		assertTrue(voc.getIncorporatedVocabularies().contains(incVoc2));

		assertFalse(voc2.getIncorporatedVocabularies().contains(incVoc1));
		assertFalse(voc2.getIncorporatedVocabularies().contains(incVoc2));

	}

	@Test
	public void testVerbalise() {
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri1", "vname1");
		assertEquals(voc.verbalise(), voc.getName());
	}

	@Test
	public void testGetPreferredBinaryFactTypeForm() {
		// FIXME the representation factory should provide an api to take boolean as parameter for is preferred.
		// need to be discussed.
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");

		Term t1 = representationFactory.makeTerm(voc, "T1");
		Term t2 = representationFactory.makeTerm(voc, "T2");
		BinaryFactType binaryFactType = meaningFactory.makeBinaryFactType();
		BinaryFactTypeForm bftf = representationFactory
				.makeBinaryFactTypeForm(voc, t1, "has", "of", t2, binaryFactType);
		BinaryFactTypeForm bftf2 = representationFactory.makeBinaryFactTypeForm(voc, t1, "has2", "of2", t2);
		assertTrue(voc.getBinaryFactTypeForms().contains(bftf));
		assertTrue(voc.getBinaryFactTypeForms().contains(bftf2));

		assertEquals(voc.getPreferredBinaryFactTypeForm(binaryFactType), bftf);

	}

	@Test
	public void testGetPreferredCharacteristicForm() {
		// FIXME the representation factory should provide an api to take boolean as parameter for is preferred.
		// need to be discussed.
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");

		Term t1 = representationFactory.makeTerm(voc, "T1");
		Characteristic characteristic = meaningFactory.makeCharacteristic();
		CharacteristicForm cf = representationFactory.makeCharacteristicForm(voc, t1, "is married", characteristic);
		CharacteristicForm cf2 = representationFactory.makeCharacteristicForm(voc, t1, "is runner");
		assertTrue(voc.getCharacteristicForms().contains(cf));
		assertTrue(voc.getCharacteristicForms().contains(cf2));

		assertEquals(voc.getPreferredCharacteristicForm(characteristic), cf);

	}

	@Test
	public void testGetPreferredTerm() {
		// FIXME the representation factory should provide an api to take boolean as parameter for is preferred.
		// need to be discussed.
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");

		ObjectType objType = meaningFactory.makeObjectType();
		Term t1 = representationFactory.makeTerm(voc, "T1", objType);
		Term t2 = representationFactory.makeTerm(voc, "T2");
		assertTrue(voc.getTerms().contains(t1));
		assertTrue(voc.getTerms().contains(t2));

		assertEquals(voc.getPreferredTerm(objType), t1);

	}

	@Test
	public void testGetCommunityName() {
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		assertEquals(voc.getCommunity(), sp);
	}

	@Test
	public void testGetRuleSets() {
		Vocabulary voc = representationFactory.makeVocabulary(sp, "vuri", "vname");
		Vocabulary voc2 = representationFactory.makeVocabulary(sp, "vuri2", "vname2");

		RuleSet rs1 = ruleFactory.makeRuleSet(voc, "General Rule Set");
		RuleSet rs1Same = ruleFactory.makeRuleSet(voc, "General Rule Set");
		RuleSet rs2 = ruleFactory.makeRuleSet(voc, "Test Rule Set");
		assertTrue(voc.getRuleSets().contains(rs1));
		assertTrue(voc.getRuleSets().contains(rs1Same));
		assertTrue(voc.getRuleSets().contains(rs2));

		RuleSet rs1InOtherVoc = ruleFactory.makeRuleSet(voc2, "General Rule Set");
		assertFalse(voc.getRuleSets().contains(rs1InOtherVoc));
		assertTrue(voc2.getRuleSets().contains(rs1InOtherVoc));
	}
}
