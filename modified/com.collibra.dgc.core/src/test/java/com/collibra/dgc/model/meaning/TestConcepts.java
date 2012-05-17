package com.collibra.dgc.model.meaning;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.community.CommunityFactory;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Vocabulary;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
@Ignore
public class TestConcepts {

	@Autowired
	private CommunityFactory communityFactory;
	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private MeaningFactory meaningFactory;

	protected Vocabulary euRentVocabulary = null;
	protected Community euRentCommunity = null;
	protected Community englishEuRent = null;

	@Before
	public void onSetup() {
		prepareEuRentVocabulary();
	}

	@After
	public void onTearDown() {
		euRentVocabulary = null;
		euRentCommunity = null;
		englishEuRent = null;
	}

	public void prepareEuRentVocabulary() {
		euRentCommunity = communityFactory.makeCommunity("EU-Rent", "eurent");
		englishEuRent = communityFactory.makeCommunity(euRentCommunity, "English EU-Rent", "englisheurent");
		euRentVocabulary = representationFactory.makeVocabulary(englishEuRent, "EU-Rent Vocabulary", "eurentvoc");
	}
}
