package com.collibra.dgc.api.rules;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.exceptions.EntityNotFoundException;
import com.collibra.dgc.core.exceptions.IllegalArgumentException;

@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestRulesComponentDefense extends AbstractDGCBootstrappedApiTest {
	private static final String NULL = null;
	private static final String EMPTY = "";
	private static final String NOT_EMPTY = "not empty";
	private static final String NON_EXISTANT = "if this actualy is a valid resource id it would be very strange indeed.";

	@Test
	public void testAddRuleSetDefenseVocabularyRIdNull() {
		try {
			rulesComponent.addRuleSet(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleSetDefenseVocabularyRIdEmpty() {
		try {
			rulesComponent.addRuleSet(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleSetDefenseVocabularyRIdNotFound() {
		try {
			rulesComponent.addRuleSet(NON_EXISTANT, NOT_EMPTY);
			Assert.fail();
		} catch (EntityNotFoundException ex) {
			Assert.assertEquals(DGCErrorCodes.VOCABULARY_NOT_FOUND_ID, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleSetDefenseVocabularyRuleSetNameNull() {
		try {

			rulesComponent.addRuleSet(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleSetDefenseVocabularyRuleSetNameEmpty() {
		try {
			rulesComponent.addRuleSet(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatementDefenseRuleSetRIdNull() {
		try {
			rulesComponent.addRuleStatement(NULL, NOT_EMPTY, false, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatementDefenseRuleSetRIdEmpty() {
		try {
			rulesComponent.addRuleStatement(EMPTY, NOT_EMPTY, false, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatementDefenseBinaryFactTypeFormRIdNull() {
		try {
			rulesComponent.addRuleStatement(NOT_EMPTY, NULL, false, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatementDefenseBinaryFactTypeFormRIdEmpty() {
		try {
			rulesComponent.addRuleStatement(NOT_EMPTY, EMPTY, false, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatement2DefenseRuleSetRIdNull() {
		try {
			rulesComponent.addRuleStatement(NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatement2DefenseRuleSetRIdEmpty() {
		try {
			rulesComponent.addRuleStatement(EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatement2DefenseHeadTermRIdNull() {
		try {
			rulesComponent.addRuleStatement(NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatement2DefenseHeadTermRIdEmpty() {
		try {
			rulesComponent.addRuleStatement(NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatement2DefenseRoleNull() {
		try {
			rulesComponent.addRuleStatement(NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.ROLE_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatement2DefenseRoleEmpty() {
		try {
			rulesComponent.addRuleStatement(NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.ROLE_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatement2DefenseCoRoleNull() {
		try {
			rulesComponent.addRuleStatement(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.ROLE_NAME_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatement2DefenseCoRoleEmpty() {
		try {
			rulesComponent.addRuleStatement(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.ROLE_NAME_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatement2DefenseTailTermRIdNull() {
		try {
			rulesComponent.addRuleStatement(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NULL, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testAddRuleStatement2DefenseTailTermRIdEmpty() {
		try {
			rulesComponent.addRuleStatement(NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, NOT_EMPTY, EMPTY, NOT_EMPTY, 0, false);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRulesRefferingFactTypeDefenseBinaryFactTypeFormRIdNull() {
		try {
			rulesComponent.findRulesRefferingFactType(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testFindRulesRefferingFactTypeDefenseBinaryFactTypeFormRIdEmpty() {
		try {
			rulesComponent.findRulesRefferingFactType(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.BFTF_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRuleSetDefenseRIdNull() {
		try {
			rulesComponent.getRuleSet(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRuleSetDefenseRIdEmpty() {
		try {
			rulesComponent.getRuleSet(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRuleSetsForVocabularyDefenseRIdNull() {
		try {
			rulesComponent.getRuleSetsForVocabulary(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRuleSetsForVocabularyDefenseRIdEmpty() {
		try {
			rulesComponent.getRuleSetsForVocabulary(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRuleStatementDefenseRIdNull() {
		try {
			rulesComponent.getRuleStatement(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetRuleStatementDefenseRIdEmpty() {
		try {
			rulesComponent.getRuleStatement(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStatementsDefenseRuleStatementRIdNull() {
		try {
			rulesComponent.getStatements(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testGetStatementsDefenseRuleStatementRIdEmpty() {
		try {
			rulesComponent.getStatements(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testIsGeneralRuleSetDefenseRuleSetRIdNull() {
		try {
			rulesComponent.isGeneralRuleSet(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testIsGeneralRuleSetDefenseRuleSetRIdEmpty() {
		try {
			rulesComponent.isGeneralRuleSet(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveRuleSetDefenseRuleSetRIdNull() {
		try {
			rulesComponent.removeRuleSet(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveRuleSetDefenseRuleSetRIdEmpty() {
		try {
			rulesComponent.removeRuleSet(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveRulesReferringTermDefenseTermRIdNull() {
		try {
			rulesComponent.removeRulesReferringTerm(NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveRulesReferringTermDefenseTermRIdEmpty() {
		try {
			rulesComponent.removeRulesReferringTerm(EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.TERM_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveRuleStatementDefenseRuleSetRIdNull() {
		try {
			rulesComponent.removeRuleStatement(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveRuleStatementDefenseRuleSetRIdEmpty() {
		try {
			rulesComponent.removeRuleStatement(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULESET_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveRuleStatementDefenseRuleStatementRIdNull() {
		try {
			rulesComponent.removeRuleStatement(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveRuleStatementDefenseRuleStatementRIdEmpty() {
		try {
			rulesComponent.removeRuleStatement(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveSimpleStatementDefenseRuleStatementRIdNull() {
		try {
			rulesComponent.removeSimpleStatement(NULL, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveSimpleStatementDefenseRuleStatementRIdEmpty() {
		try {
			rulesComponent.removeSimpleStatement(EMPTY, NOT_EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.RULE_STATEMENT_ID_EMPTY, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveSimpleStatementDefenseSimpleStatementRIdNull() {
		try {
			rulesComponent.removeSimpleStatement(NOT_EMPTY, NULL);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.SIMPLE_STATEMENT_ID_NULL, ex.getErrorCode());
		}
	}

	@Test
	public void testRemoveSimpleStatementDefenseSimpleStatementRIdEmpty() {
		try {
			rulesComponent.removeSimpleStatement(NOT_EMPTY, EMPTY);
			Assert.fail();
		} catch (IllegalArgumentException ex) {
			Assert.assertEquals(DGCErrorCodes.SIMPLE_STATEMENT_ID_EMPTY, ex.getErrorCode());
		}
	}

}
