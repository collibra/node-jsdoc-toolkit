package com.collibra.dgc.service.facttypeform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.rules.RuleStatement;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * Test for creating rules on derived fact types.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRulesOnDerivedFacts extends AbstractServiceTest {
	@Test
	public void testCreateRuleOnDerivedFact() {
		testAPI(false);
	}

	@Test
	public void testCreateRuleOnDerivedFactsWithBFTFDetails() {
		testAPI(true);
	}

	private void testAPI(boolean useBftfDetails) {
		Community sc = communityFactory.makeCommunity("SC", "SC URI");
		Community sp = communityFactory.makeCommunity(sc, "SP", "SP URI");
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "Rules on derived Fact Types URI",
				"Rules on derived Fact Types");

		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term driver = representationFactory.makeTerm(vocabulary, "Driver");
		Term car = representationFactory.makeTerm(vocabulary, "Car");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		driver.getObjectType().setGeneralConcept(person.getObjectType());

		communityService.save(sc);

		resetTransaction();

		driver = representationService.findTermByResourceId(driver.getId());
		assertNotNull(driver);

		Collection<BinaryFactTypeForm> derived = representationService.findDerivedFacts(driver);
		assertEquals(1, derived.size());

		// Create rule on derived bftf
		BinaryFactTypeForm bftf = derived.iterator().next();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		Set<RuleStatement> rules = null;
		if (useBftfDetails) {
			rules = rulesService.createRuleStatement(vocabulary.getRuleSets().iterator().next(), bftf.getHeadTerm(),
					bftf.getRole(), bftf.getCoRole(), bftf.getTailTerm(), "EXACTLY", 1, true);
		} else {
			rules = rulesService.createRuleStatement(vocabulary.getRuleSets().iterator().next(),
					bftf.getLeftPlaceHolder(), "ATLEAST", 1, false);
		}

		assertEquals(1, rules.size());
		BinaryFactTypeForm temp = rules.iterator().next().getSimpleStatements().iterator().next()
				.getReadingDirections().iterator().next().getBinaryFactTypeForm();
		assertEquals(bftf.getHeadTerm(), temp.getHeadTerm());
		assertEquals(bftf.getTailTerm(), temp.getTailTerm());
		assertEquals(bftf.getRole(), temp.getRole());
		assertEquals(bftf.getCoRole(), temp.getCoRole());
	}
}
