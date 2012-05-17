package com.collibra.dgc.api.rules;

import java.util.Collection;

import junit.framework.Assert;

import org.apache.commons.collections.map.MultiValueMap;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.component.RulesComponent;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;

/**
 * {@link RulesComponent} API tests.
 * @author amarnath
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRulesComponent extends AbstractDGCBootstrappedApiTest {

	@Test
	public void testGetRuleSetsForVocabulary() {
		Vocabulary voc = createVocabulary();
		Collection<RuleSet> ruleSets = rulesComponent.getRuleSetsForVocabulary(voc.getId().toString());
		Assert.assertEquals(1, ruleSets.size());

		RuleSet result = rulesComponent.addRuleSet(voc.getId().toString(), "MyRuleSet");
		Assert.assertNotNull(result);

		ruleSets = rulesComponent.getRuleSetsForVocabulary(voc.getId().toString());
		Assert.assertEquals(2, ruleSets.size());
	}

	@Test
	public void testGetRuleSet() {
		RuleSet rs = rulesComponent.addRuleSet(createVocabulary().getId().toString(), "MyRuleSet");
		RuleSet result = rulesComponent.getRuleSet(rs.getId().toString());
		Assert.assertNotNull(result);
		Assert.assertEquals(rs.getName(), result.getName());
	}

	@Test
	public void testGetRuleStatement() {
		RuleStatement rs = createRuleStatement();
		RuleStatement result = rulesComponent.getRuleStatement(rs.getId().toString());
		Assert.assertEquals(rs.getId(), result.getId());
	}

	@Test
	public void testRemoveRuleSet() {
		RuleSet rs = createRuleSet();
		boolean result = rulesComponent.removeRuleSet(rs.getId().toString());
		Assert.assertTrue(result);
		Assert.assertNull(rulesComponent.getRuleSet(rs.getId().toString()));
	}

	@Test
	public void testRemoveRuleStatement() {
		Vocabulary voc = createVocabulary();
		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeForm(voc.getId().toString(),
				"Person", "drives", "driven by", "Car");
		RuleSet ruleSet = rulesComponent.addRuleSet(voc.getId().toString(), RULESET_NAME);
		Collection<RuleStatement> rules = rulesComponent.addRuleStatement(ruleSet.getId().toString(), bftf
				.getId().toString(), true, "ATLEAST", 1, false);

		boolean result = rulesComponent.removeRuleStatement(ruleSet.getId().toString(), rules.iterator().next()
				.getId().toString());
		Assert.assertTrue(result);
		RuleStatement resultSt = rulesComponent.getRuleStatement(rules.iterator().next().getId().toString());
		Assert.assertNull(resultSt);
	}

	@Test
	public void testAddRuleSet() {
		RuleSet rs = createRuleSet();
		Assert.assertNotNull(rs);
		Assert.assertEquals(RULESET_NAME, rs.getName());
	}

	@Test
	public void testAddRuleStatement() {
		RuleStatement rs = createRuleStatement();
		Assert.assertNotNull(rs);
	}

	@Test
	public void testIsGeneralRuleSet() {
		Collection<RuleSet> ruleSets = rulesComponent.getRuleSetsForVocabulary(createVocabulary().getId()
				.toString());
		Assert.assertTrue(rulesComponent.isGeneralRuleSet(ruleSets.iterator().next().getId().toString()));
	}

	@Test
	public void testGetStatements() {
		RuleStatement rs = createRuleStatement();
		Collection<SimpleStatement> statements = rulesComponent.getStatements(rs.getId().toString());
		Assert.assertEquals(1, statements.size());
	}

	@Test
	public void testRemoveSimpleStatement() {
		RuleStatement rs = createRuleStatement();
		Collection<SimpleStatement> statements = rulesComponent.getStatements(rs.getId().toString());
		boolean result = rulesComponent.removeSimpleStatement(rs.getId().toString(), statements.iterator()
				.next().getId().toString());
		Assert.assertTrue(result);
	}

	@Test
	public void testFindRulesRefferingFactType() {
		Vocabulary voc = createVocabulary();
		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeForm(voc.getId().toString(),
				"Person", "drives", "driven by", "Car");
		RuleSet ruleSet = rulesComponent.addRuleSet(voc.getId().toString(), RULESET_NAME);
		Collection<RuleStatement> rules = rulesComponent.addRuleStatement(ruleSet.getId().toString(), bftf
				.getId().toString(), true, "ATLEAST", 1, false);

		MultiValueMap result = rulesComponent.findRulesRefferingFactType(bftf.getId().toString());
		for (Object key : result.keySet()) {
			Collection values = result.getCollection(key);
			if (values == null) {
				continue;
			}

			for (Object value : values) {
				MultiValueMap map = (MultiValueMap) value;
				for (Object subMapKey : map.keySet()) {
					RuleStatement rs = (RuleStatement) subMapKey;
					boolean exists = false;
					for (RuleStatement rule : rules) {
						if (rule.getId().equals(rs.getId())) {
							exists = true;
							break;
						}
					}

					if (!exists) {
						Assert.fail("RuleStatement not found.");
					}
				}
			}
		}
	}

	@Test
	public void testRemoveRulesReferringTerm() {
		Vocabulary voc = createVocabulary();
		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeForm(voc.getId().toString(),
				"Person", "drives", "driven by", "Car");
		RuleSet ruleSet = rulesComponent.addRuleSet(voc.getId().toString(), RULESET_NAME);
		Collection<RuleStatement> rules = rulesComponent.addRuleStatement(ruleSet.getId().toString(), bftf
				.getId().toString(), true, "ATLEAST", 1, false);
		Assert.assertTrue(rules.size() > 0);

		boolean result = rulesComponent.removeRulesReferringTerm(bftf.getHeadTerm().getId().toString());
		Assert.assertTrue(result);

		MultiValueMap references = rulesComponent.findRulesRefferingFactType(bftf.getId().toString());
		Assert.assertEquals(0, references.size());
	}

	private RuleStatement createRuleStatement() {
		Vocabulary voc = createVocabulary();
		BinaryFactTypeForm bftf = binaryFactTypeFormComponent.addBinaryFactTypeForm(voc.getId().toString(),
				"Person", "drives", "driven by", "Car");
		RuleSet ruleSet = rulesComponent.addRuleSet(voc.getId().toString(), RULESET_NAME);
		Collection<RuleStatement> rules = rulesComponent.addRuleStatement(ruleSet.getId().toString(), bftf
				.getId().toString(), true, "ATLEAST", 1, false);

		return rules.iterator().next();
	}
}
