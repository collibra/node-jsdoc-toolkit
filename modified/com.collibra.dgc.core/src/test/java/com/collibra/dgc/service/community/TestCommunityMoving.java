package com.collibra.dgc.service.community;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
@Ignore
public class TestCommunityMoving extends AbstractServiceTest {

	/**
	 * The Ecoscore Semantic Community.
	 */
	private Community ecoScoreCom;

	/**
	 * The Dutch (NL) and French (FR) speaking ecoscore communies.
	 */
	private Community ecoScoreNLCom;
	private Community ecoScoreFRCom;

	/**
	 * The Dutch and French vocabulaires for ecoscore. Lists cars as terms with engine volume, fuel and ecoscore number
	 * as custom attributes.
	 */
	private Vocabulary ecoscorePWVoc;
	private Vocabulary ecoscoreVeVoc;

	/**
	 * The custom attribute types, with translation if necessary.
	 */
	private Term enginecc;
	private Term ccNL; // === enginecc
	private Term ccFR; // === enginecc

	private Term ecoscore;
	private Term fuel;
	private Term brandstof; // === fuel
	private Term combustible; // === fuel

	/**
	 * Two sample cars in dutch and french
	 */
	private Term citroenNL;
	private Term hondaNL;
	private Term citroenFR;
	private Term hondaFR;

	private void createEcoscoreCommunities() {
		ecoScoreCom = communityFactory.makeCommunity("Ecoscore Community", "http://www.ecoscore.be/");
		ecoScoreNLCom = communityFactory.makeCommunity(ecoScoreCom, "Ecoscore Personenwagens",
				"http://www.ecoscore.be/NL");
		ecoScoreNLCom = communityFactory.makeCommunity(ecoScoreCom, "Ecoscore V�hicules", "http://www.ecoscore.be/FR");
	}

	private void createEcoscoreVocabularies() {
		ecoscorePWVoc = representationFactory.makeVocabulary(ecoScoreNLCom, "Ecoscore Personenwagens",
				"http://www.ecoscore.be/NL/");
		ecoscoreVeVoc = representationFactory.makeVocabulary(ecoScoreFRCom, "Ecoscore V�hicules",
				"http://www.ecoscore.be/NL/");
	}

	private void createEcoscoreTerms() {
		citroenNL = representationFactory.makeTerm(ecoscorePWVoc, "Citroen 1.0 C1 - Tentation");
		citroenFR = representationFactory.makeTerm(ecoscoreVeVoc, "Citroen 1.0 C1 - Tentation");
		hondaNL = representationFactory.makeTerm(ecoscorePWVoc, "Honda Insight 1.3 Hybrid");
		hondaFR = representationFactory.makeTerm(ecoscorePWVoc, "Honda Insight 1.3 Hybrid");
	}

	private void createEcoscoreAttributes() {
		// Citroen
		representationFactory.makeStringAttribute(ccNL, citroenNL, "998");
		representationFactory.makeStringAttribute(ccFR, citroenFR, "998");
		representationFactory.makeStringAttribute(ecoscore, citroenNL, "74");
		representationFactory.makeStringAttribute(ecoscore, citroenFR, "74");
		representationFactory.makeStringAttribute(brandstof, citroenNL, "Benzine");
		representationFactory.makeStringAttribute(combustible, citroenFR, "Benzine");

		// Honda
		representationFactory.makeStringAttribute(ccNL, hondaNL, "1339");
		representationFactory.makeStringAttribute(ccFR, hondaFR, "1339");
		representationFactory.makeStringAttribute(ecoscore, hondaNL, "76");
		representationFactory.makeStringAttribute(ecoscore, hondaFR, "76");
		representationFactory.makeStringAttribute(brandstof, hondaNL, "Benzine");
		representationFactory.makeStringAttribute(combustible, hondaFR, "Benzine");
	}

	private Community createCommunity(String name, String uri) {
		communityService.createCommunity(name, uri);
		resetTransaction();

		Community semComm = communityService.findCommunityByUri(uri);
		return semComm;
	}

	private Community createCommunity(Community parent, String name, String uri) {
		communityService.createCommunity(parent, name, uri);
		resetTransaction();

		Community spComm = communityService.findCommunityByUri(uri);
		return spComm;
	}

}
