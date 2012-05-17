package com.collibra.dgc.service.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.rules.FrequencyRuleStatement;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;
import com.collibra.dgc.core.model.rules.SemiparsedRuleStatement;
import com.collibra.dgc.core.model.rules.SimpleRuleStatement;
import com.collibra.dgc.core.model.rules.impl.SemiparsedRuleStatementImpl;
import com.collibra.dgc.core.model.rules.impl.SimpleRuleStatementImpl;
import com.collibra.dgc.service.AbstractServiceTest;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRulesService extends AbstractServiceTest {

	@Test
	public void testCreateRuleSet() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);

		String RULE_SET_NAME = "TestCreateRuleSet";
		RuleSet ruleSet = createRuleSet(RULE_SET_NAME, vocabulary);
		resetTransaction();
		RuleSet reloaded = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertNotNull(reloaded);
		assertEquals(RULE_SET_NAME, reloaded.getName());
	}

	@Test
	public void testCreateSimpleRuleStatement() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);

		String RULE_SET_NAME = "testCreateSimpleRuleStatement";
		RuleSet ruleSet = createRuleSet(RULE_SET_NAME, vocabulary);
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		SimpleRuleStatement ruleStatement = ruleFactory.makeMandatoryRuleStatement(vocabulary);
		ruleStatement.addSimpleStatement(createSimpleStatement(vocabulary));
		ruleSet.addRuleStatement(ruleStatement);

		rulesService.saveRuleSet(ruleSet);
		resetTransaction();
		RuleSet reloaded = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertNotNull(reloaded);
		assertTrue(reloaded.getRuleStatements().size() == 1);
	}

	@Test
	public void testCreateFrequencyRuleStatement() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);
		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		assertNotNull(vocabulary);

		String RULE_SET_NAME = "testCreateFrequencyRuleStatement";
		RuleSet ruleSet = createRuleSet(RULE_SET_NAME, vocabulary);
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		FrequencyRuleStatement ruleStatement = ruleFactory.makeFrequencyRuleStatement(vocabulary);
		ruleStatement.addSimpleStatement(createSimpleStatement(vocabulary));
		ruleStatement.setMax(1);
		ruleStatement.setMin(1);
		ruleSet.addRuleStatement(ruleStatement);

		// rulesService.createFrequencyRuleStatement(ruleSet, ruleStatement);
		rulesService.saveRuleSet(ruleSet);

		resetTransaction();

		RuleSet reloaded = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertNotNull(reloaded);
		assertTrue(reloaded.getRuleStatements().size() == 1);
	}

	@Test
	public void testCreateSemiparsedRuleStatement() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);
		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());

		String RULE_SET_NAME = "testCreateSemiparsedRuleStatement";
		RuleSet ruleSet = createRuleSet(RULE_SET_NAME, vocabulary);
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		SemiparsedRuleStatement ruleStatement = ruleFactory.makeSemiparsedRuleStatement(vocabulary);
		ruleStatement.addSimpleStatement(createSimpleStatement(vocabulary));
		ruleSet.addRuleStatement(ruleStatement);
		rulesService.saveRuleSet(ruleSet);

		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term head = representationFactory.makeTerm(vocabulary, "A");
		representationService.save(head);
		Term tail = representationFactory.makeTerm(vocabulary, "B");
		representationService.save(tail);
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, head, "has", "of", tail);
		representationService.saveBinaryFactTypeForm(bftf);

		resetTransaction();

		// Create more BFTF
		Term latestHead = representationService.findTermByResourceId(head.getId());
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		Term termC = representationFactory.makeTerm(vocabulary, "C");
		representationService.save(termC);
		BinaryFactTypeForm bftf2 = representationFactory.makeBinaryFactTypeForm(vocabulary, latestHead, "has", "of",
				termC);
		representationService.saveBinaryFactTypeForm(bftf2);

		// Create more BFTF
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		termC = representationService.findTermByResourceId(termC.getId());
		tail = representationService.findTermByResourceId(tail.getId());
		BinaryFactTypeForm bftf3 = representationFactory.makeBinaryFactTypeForm(vocabulary, tail, "has", "of", termC);
		representationService.saveBinaryFactTypeForm(bftf3);

		RuleSet reloaded = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertNotNull(reloaded);
		assertEquals(1, reloaded.getRuleStatements().size());

		RuleStatement reloadedRuleStatement = rulesService.findLatestRuleStatementByResourceId(ruleStatement.getId());
		assertNotNull(reloadedRuleStatement);

		latestHead = representationService.findTermByResourceId(head.getId());
		Term latestTail = representationService.findTermByResourceId(tail.getId());
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertEquals(latestHead.getId(), bftf.getHeadTerm().getId());
		assertEquals(latestTail.getId(), bftf.getTailTerm().getId());
	}

	@Test
	public void testCreateMultipleRuleStatements() {
		// Create vocabulary.
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "TestVersioningURI", "TestVersioning");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByUri(vocabulary.getUri());

		String RULE_SET_NAME = "testCreateMultipleRuleStatements";
		RuleSet ruleSet = createRuleSet(RULE_SET_NAME, vocabulary);
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		SemiparsedRuleStatement semiparsedRuleStatement = ruleFactory.makeSemiparsedRuleStatement(vocabulary);
		semiparsedRuleStatement.addSimpleStatement(createSimpleStatement(vocabulary));
		ruleSet.addRuleStatement(semiparsedRuleStatement);

		SimpleRuleStatement simpleRuleStatement = ruleFactory.makeMandatoryRuleStatement(vocabulary);
		simpleRuleStatement.addSimpleStatement(createSimpleStatement(vocabulary));
		ruleSet.addRuleStatement(simpleRuleStatement);

		rulesService.saveRuleSet(ruleSet);
		resetTransaction();
		RuleSet reloaded = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertNotNull(reloaded);
		assertTrue(reloaded.getRuleStatements().size() == 2);

		Set<RuleStatement> ruleStatemetns = reloaded.getRuleStatements();
		assertForRuleStatementType(ruleStatemetns, SimpleRuleStatementImpl.class);
		assertForRuleStatementType(ruleStatemetns, SemiparsedRuleStatementImpl.class);

		for (RuleStatement rs : ruleStatemetns) {
			assertNotNull(rs.getRule());

			Set<SimpleStatement> statements = null;
			if (rs instanceof SimpleRuleStatement) {
				statements = ((SimpleRuleStatement) rs).getSimpleStatements();
			} else if (rs instanceof SemiparsedRuleStatement) {
				statements = ((SemiparsedRuleStatement) rs).getSimpleStatements();
			}

			assertNotNull(statements);

			for (SimpleStatement ss : statements) {
				assertTrue(ss.getReadingDirections().size() == 1);
				assertNotNull(ss.getSimpleProposition());
			}
		}
	}

	@Test
	public void testVersioning() {
		Community sp = communityFactory.makeCommunity("SP", "SP URI");

		// Create vocabulary.
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "TestVersioningURI", "TestVersioning");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		Term Human = representationFactory.makeTerm(vocabulary, "Human");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);
		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "is a", "classification of", Human);
		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByUri(vocabulary.getUri());

		// Create ruleset, The vocabulary version is incremented.
		RuleSet ruleSet = ruleFactory.makeRuleSet(vocabulary, "TestVersioningRuleSet");
		rulesService.saveRuleSet(ruleSet);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ruleSet = rulesService.findRuleSetByResourceId(ruleSet.getId());

		// Add rulestatement, Ruleset version is incremented, however vocabulary version remains unchanged.
		SemiparsedRuleStatement semiparsedRuleStatement = ruleFactory.makeSemiparsedRuleStatement(vocabulary);
		semiparsedRuleStatement.setUnparsed("TestVersioning SemiparsedRuleStatement");
		ruleSet.addRuleStatement(semiparsedRuleStatement);
		rulesService.saveSemiparsedRuleStatement(ruleSet, semiparsedRuleStatement);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ruleSet = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertEquals(1, ruleSet.getRuleStatements().size());
		RuleStatement ruleStatement = ruleSet.getRuleStatements().iterator().next();

		// Add simplestatement. RuleStatement version is incremented, however vocabulary and ruleset versions remain the
		// same.
		SimpleStatement simpleStatement = ruleFactory.makeSimpleStatement(vocabulary);
		ruleStatement.addSimpleStatement(simpleStatement);
		rulesService.saveSemiparsedRuleStatement(ruleSet, ((SemiparsedRuleStatement) ruleStatement));
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ruleSet = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertEquals(1, ruleSet.getRuleStatements().size());
		ruleStatement = ruleSet.getRuleStatements().iterator().next();

		// Remove simple statement. RuleStatement version is incremented, however vocabulary and ruleset versions remain
		// the same.
		ruleStatement = ruleSet.getRuleStatements().iterator().next();
		simpleStatement = ruleStatement.getSimpleStatements().iterator().next();
		rulesService.removeSimpleStatement(ruleStatement, simpleStatement);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ruleSet = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertEquals(1, ruleSet.getRuleStatements().size());
		ruleStatement = ruleSet.getRuleStatements().iterator().next();

		// Remove rule statement. RuleSet version is incremented, however vocabulary version remains the same.
		ruleStatement = ruleSet.getRuleStatements().iterator().next();
		rulesService.removeRuleStatement(ruleSet, ruleStatement);
		resetTransaction();
		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ruleSet = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertEquals(0, ruleSet.getRuleStatements().size());

		// Remove rule set. Vocabulary version is incremented.
		rulesService.removeRuleSet(ruleSet);
		resetTransaction();

		vocabulary = representationService.findVocabularyByUri(vocabulary.getUri());
		// assertEquals(0, vocabulary.getRuleSets().size());
	}

	@Test
	public void testDeleteReferences() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);

		SimpleRuleStatement ruleStatement = ruleFactory.makeMandatoryRuleStatement(vocabulary);
		ruleStatement.addSimpleStatement(createSimpleStatement(vocabulary));
		vocabulary.getRuleSets().iterator().next().addRuleStatement(ruleStatement);

		communityService.save(sp);
		resetTransaction();

		person = representationService.findTermByResourceId(person.getId());
		assertNotNull(person);

		representationService.remove(person);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertTrue(vocabulary.getRuleSets().size() == 1);
		RuleSet ruleset = vocabulary.getRuleSets().iterator().next();
		assertEquals(0, ruleset.getRuleStatements().size());
	}

	@Test
	public void testRemoveReferences() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);

		SimpleRuleStatement ruleStatement = ruleFactory.makeMandatoryRuleStatement(vocabulary);
		ruleStatement.addSimpleStatement(createSimpleStatement(vocabulary));
		vocabulary.getRuleSets().iterator().next().addRuleStatement(ruleStatement);

		communityService.save(sp);
		resetTransaction();

		person = representationService.findTermByResourceId(person.getId());
		assertNotNull(person);

		representationService.remove(person);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertTrue(vocabulary.getRuleSets().size() == 1);
		RuleSet ruleset = vocabulary.getRuleSets().iterator().next();
		assertEquals(0, ruleset.getRuleStatements().size());
	}

	@Test
	public void testCreateFindDelete() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		RuleSet ruleSet = vocabulary.getRuleSets().iterator().next();
		SimpleRuleStatement ruleStatement = ruleFactory.makeMandatoryRuleStatement(vocabulary);
		ruleStatement.addSimpleStatement(createSimpleStatement(vocabulary));
		ruleSet.addRuleStatement(ruleStatement);
		rulesService.saveSimpleRuleStatement(ruleSet, ruleStatement);
		resetTransaction();

		ruleSet = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertEquals(1, ruleSet.getRuleStatements().size());

		SemiparsedRuleStatement semiparsedRuleSt = ruleFactory.makeSemiparsedRuleStatement(ruleSet.getVocabulary());
		semiparsedRuleSt.setUnparsed("Testing");
		semiparsedRuleSt.addSimpleStatement(createSimpleStatement(ruleSet.getVocabulary()));
		ruleSet.addRuleStatement(semiparsedRuleSt);
		rulesService.saveSemiparsedRuleStatement(ruleSet, semiparsedRuleSt);
		resetTransaction();

		ruleSet = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertEquals(2, ruleSet.getRuleStatements().size());

		FrequencyRuleStatement freqRuleStatement = ruleFactory.makeFrequencyRuleStatement(ruleSet.getVocabulary());
		freqRuleStatement.setMax(10);
		freqRuleStatement.setMin(5);
		freqRuleStatement.addSimpleStatement(createSimpleStatement(ruleSet.getVocabulary()));
		ruleSet.addRuleStatement(freqRuleStatement);
		rulesService.saveFrequencyRuleStatement(ruleSet, freqRuleStatement);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		ruleSet = rulesService.findRuleSetByName(ruleSet.getName(), vocabulary);
		assertNotNull(ruleSet);
		assertEquals(vocabulary.getRuleSets().iterator().next(), ruleSet);

		ruleSet = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertNotNull(ruleSet);
		assertEquals(vocabulary.getRuleSets().iterator().next(), ruleSet);

		List<RuleSet> ruleSets = rulesService.findRuleSets(ruleSet.getRuleStatements().iterator().next());
		assertEquals(1, ruleSets.size());
		assertEquals(ruleSet, ruleSets.get(0));

		car = representationService.findTermByResourceId(car.getId());
		List<SimpleStatement> simpleStatements = rulesService.findAllStatements(car);
		assertEquals(3, simpleStatements.size());

		resetTransaction();

		ruleSet = rulesService.findRuleSetByResourceId(ruleSet.getId());
		rulesService.removeRuleSet(ruleSet);
		resetTransaction();

		ruleSet = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertTrue(ruleSet == null);
	}

	@Test
	public void testDeleteTermReferences() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);

		SimpleRuleStatement ruleStatement = ruleFactory.makeMandatoryRuleStatement(vocabulary);
		ruleStatement.addSimpleStatement(createSimpleStatement(vocabulary));
		vocabulary.getRuleSets().iterator().next().addRuleStatement(ruleStatement);

		communityService.save(sp);
		resetTransaction();

		car = representationService.findTermByResourceId(car.getId());
		rulesService.removeReferences(car);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertEquals(0, vocabulary.getRuleSets().iterator().next().getRuleStatements().size());
	}

	@Test
	public void testRemoveRuleSet() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");

		representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives", "driven by", car);

		RuleSet ruleSet = ruleFactory.makeRuleSet(vocabulary, "RemoveRuleSetTest");

		SimpleRuleStatement ruleStatement = ruleFactory.makeMandatoryRuleStatement(vocabulary);
		ruleStatement.addSimpleStatement(createSimpleStatement(vocabulary));
		ruleSet.addRuleStatement(ruleStatement);

		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertEquals(2, vocabulary.getRuleSets().size());

		ruleSet = rulesService.findRuleSetByResourceId(ruleSet.getId());
		assertNotNull(ruleSet);
		rulesService.removeRuleSet(ruleSet);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertEquals(1, vocabulary.getRuleSets().size());
	}

	/**
	 * #6024. Deleting a fact type with rules leads to all sorts of nastyness
	 * <p>
	 * To reproduce:
	 * <p>
	 * Create a fact type in a vocabulary.
	 * <p>
	 * Add three rules to the fact type from the term page Without reloading the term page, delete the fact type
	 * 
	 * Result: Versioning is going all wrong On the vocabulary page, we now have three general rule sets In the
	 * database, the new vocabulary version has three ruleset version id's (in the VOC_RULESET table) Creating a
	 * categorization scheme fails - see bug # 6020
	 */
	@Test
	public void testBug6024() {
		Vocabulary vocabulary = representationFactory.makeVocabulary(sp, "com.collibra.ns", "Vocabulary 1");
		Term person = representationFactory.makeTerm(vocabulary, "Person");
		Term car = representationFactory.makeTerm(vocabulary, "Car");
		BinaryFactTypeForm bftf = representationFactory.makeBinaryFactTypeForm(vocabulary, person, "drives",
				"driven by", car);
		communityService.save(sp);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertEquals(1, vocabulary.getRuleSets().size());
		assertEquals(0, vocabulary.getRuleSets().iterator().next().getRuleStatements().size());

		// Add rule 1
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertNotNull(bftf);

		rulesService.createRuleStatement(vocabulary.getRuleSets().iterator().next(), bftf.getLeftPlaceHolder(),
				"ATMOST", 5, false);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertEquals(1, vocabulary.getRuleSets().size());
		assertEquals(1, vocabulary.getRuleSets().iterator().next().getRuleStatements().size());

		// Add rule 2
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertNotNull(bftf);

		rulesService.createRuleStatement(vocabulary.getRuleSets().iterator().next(), bftf.getLeftPlaceHolder(),
				"ATLEAST", 1, false);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertEquals(1, vocabulary.getRuleSets().size());
		assertEquals(2, vocabulary.getRuleSets().iterator().next().getRuleStatements().size());

		// Add rule 3
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertNotNull(bftf);

		rulesService.createRuleStatement(vocabulary.getRuleSets().iterator().next(), bftf.getLeftPlaceHolder(),
				"EXACTLY", 2, false);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertEquals(1, vocabulary.getRuleSets().size());
		assertEquals(3, vocabulary.getRuleSets().iterator().next().getRuleStatements().size());

		// Delete the BFTF
		bftf = representationService.findBinaryFactTypeFormByResourceId(bftf.getId());
		assertNotNull(bftf);

		representationService.remove(bftf);
		resetTransaction();

		vocabulary = representationService.findVocabularyByResourceId(vocabulary.getId());
		assertEquals(1, vocabulary.getRuleSets().size());
		assertEquals(0, vocabulary.getRuleSets().iterator().next().getRuleStatements().size());
	}

	private final void assertForRuleStatementType(final Set<RuleStatement> ruleStatemetns,
			Class<? extends RuleStatement> ruleClass) {
		for (RuleStatement rs : ruleStatemetns) {
			if (rs.getClass().equals(ruleClass)) {
				return;
			}
		}

		fail("RuleStatement of type: '" + ruleClass.getName() + "' not found.");
	}

	private final SimpleStatement createSimpleStatement(Vocabulary vocabulary) {
		BinaryFactTypeForm bftf = vocabulary.getBinaryFactTypeForms().iterator().next();
		SimpleStatement statement = ruleFactory.makeSimpleStatement(vocabulary);
		statement.addReadingDirection(bftf.getLeftPlaceHolder());
		return statement;
	}

	private final RuleSet createRuleSet(final String name, final Vocabulary vocabulary) {
		assertNotNull(vocabulary);
		RuleSet ruleSet = ruleFactory.makeRuleSet(vocabulary, name);
		ruleSet = rulesService.saveRuleSet(ruleSet);
		assertNotNull(ruleSet);
		assertTrue(ruleSet.isPersisted());
		resetTransaction();
		return ruleSet;
	}
}
