package com.collibra.dgc.model.community;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.community.impl.CommunityImpl;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Vocabulary;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestCommunity {

	@Autowired
	protected CommunityFactory communityFactory;
	@Autowired
	protected RepresentationFactory representationFactory;

	protected Community createExampleCommunity(String name) {
		return communityFactory.makeCommunity(name, name);
	}

	protected Community createExampleCommunity(Community parent, String name) {
		return communityFactory.makeCommunity(parent, name, name);
	}

	@Test
	public void testGetVocabularies() {
		Community sc1 = createExampleCommunity("SC1");
		Community spc1 = createExampleCommunity(sc1, "SPC1");
		assertEquals(0, spc1.getVocabularies().size());
		Vocabulary voc = representationFactory.makeVocabulary(spc1, "MyURI", "MyVoc");
		Vocabulary voc2 = representationFactory.makeVocabulary(spc1, "MyURI2", "MyVoc2");
		assertEquals(2, spc1.getVocabularies().size());
	}

	@Test
	public void testEquals() {
		Community sc1 = createExampleCommunity("SC1");
		Community sc2 = createExampleCommunity("SC2");
		Community sc1Same = createExampleCommunity("SC1");

		assertFalse(sc1.equals(sc2));
		assertEquals(sc1, sc1Same);
	}

	@Test
	public void testGetName() {
		Community sc1 = createExampleCommunity("SC1");
		assertEquals("SC1", sc1.getName());
	}

	@Test
	public void testGetParentCommunityAndSubCommunities() {
		Community sc1 = createExampleCommunity("SC1");
		Community sc2 = createExampleCommunity("SC2");
		Community sc3 = createExampleCommunity("SC3");
		assertTrue(sc2.getParentCommunity() == null);
		((CommunityImpl) sc2).setParentCommunity(sc1);
		sc1.addSubCommunity(sc2);
		assertEquals(sc1, sc2.getParentCommunity());
		sc1.addSubCommunity(sc3);
		assertEquals(sc1, sc3.getParentCommunity());

		assertEquals(2, sc1.getSubCommunities().size());
	}

	@Test
	public void testGetLanguage() {
		Community sc1 = createExampleCommunity("SC1");
		Community spc1 = createExampleCommunity(sc1, "SPC1");
		assertEquals("English", spc1.getLanguage());
		spc1.setLanguage("Nederlands");
		assertEquals("Nederlands", spc1.getLanguage());
	}

	@Test
	public void testVerbalise() {
		Community sc1 = createExampleCommunity("SC1");
		assertEquals("SC1", sc1.verbalise());
	}
}
