package com.collibra.dgc.service;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;

@Ignore
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestInnodbConstraints extends AbstractServiceTest {

	// @Test
	// public void testAddAttributeToNewVocabularyCreateVocabularyOnly() {
	// Vocabulary vocabulary = representationFactory.makeVocabulary("URI", "Test", sp);
	// Term car = representationFactory.makeTerm(vocabulary, "Car");
	// Term status = representationFactory.makeTerm(vocabulary, "Status");
	// representationFactory.makeCustomAttribute(vocabulary, status, car, "Car Reviewer 1");
	// representationFactory.makeCustomAttribute(vocabulary, status, car, "Car Reviewer 2");
	//
	// communityService.save(sc);
	// resetTransaction();
	//
	// car = representationService.findLatestTermByResourceId(car.getResourceId());
	// vocabulary = representationService.findLatestVocabularyByResourceId(vocabulary.getResourceId());
	// // we expect car to have version 2 since it is preferred term and that should increase version after an
	// // attribute was added
	// assertEquals(1, car.getVersion().intValue());
	// // meaning should not change versions for adding attributes
	// assertEquals(1, car.getMeaning().getVersion().intValue());
	//
	// // vocabulary should still be 2 since we don't want vocabulary version to increment for added attributes (+2 for
	// // 2 added terms). Also the there will be 'Status' attribute will always be there for terms.
	// assertEquals(1, vocabulary.getVersion().intValue());
	// assertEquals(3, car.getAttributes().size());
	//
	// RuleSet ruleSet = ruleFactory.makeRuleSet(vocabulary, "TestRuleSet");
	//
	// ruleSet.addRuleStatement(ruleFactory.makeMandatoryRuleStatement(vocabulary));
	//
	// ruleSet = rulesService.saveRuleSet(ruleSet);
	//
	// resetTransaction();
	//
	// vocabulary = representationService.findVocabularyByUri("URI");
	//
	// car = representationService.findTermBySignifier(vocabulary, "Car");
	// representationFactory.makeCustomAttribute(vocabulary, status, car, "Car Reviewer 3");
	// representationFactory.makeCustomAttribute(vocabulary, status, car, "Car Reviewer 4");
	//
	// vocabulary = representationService.saveVocabulary(vocabulary);
	//
	// resetTransaction();
	//
	// System.out.println();
	// }
}
