package com.collibra.dgc.core.component.impl;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collibra.dgc.core.component.RulesComponent;
import com.collibra.dgc.core.constants.Constants;
import com.collibra.dgc.core.exceptions.ConstraintViolationException;
import com.collibra.dgc.core.exceptions.DGCErrorCodes;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.rules.RuleFactory;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.RulesService;
import com.collibra.dgc.core.util.Defense;

/**
 * Rules API implementation.
 * @author amarnath
 */
@Service
public class RulesComponentImpl implements RulesComponent {
	@Autowired
	private RulesService rulesService;

	@Autowired
	private RepresentationService representationService;

	@Autowired
	private RuleFactory ruleFactory;

	@Override
	@Transactional
	public Collection<RuleSet> getRuleSetsForVocabulary(String ruleSetRId) {
		Defense.notEmpty(ruleSetRId, DGCErrorCodes.RULESET_ID_NULL, DGCErrorCodes.RULESET_ID_EMPTY, "ruleSetRId");
		return new LinkedList<RuleSet>(representationService.getVocabularyWithError(ruleSetRId).getRuleSets());
	}

	@Override
	@Transactional
	public RuleSet getRuleSet(String ruleSetRId) {
		Defense.notEmpty(ruleSetRId, DGCErrorCodes.RULESET_ID_NULL, DGCErrorCodes.RULESET_ID_EMPTY, "ruleSetRId");
		return rulesService.findRuleSetByResourceId(ruleSetRId);
	}

	@Override
	@Transactional
	public RuleStatement getRuleStatement(String ruleSetRId) {
		Defense.notEmpty(ruleSetRId, DGCErrorCodes.RULE_STATEMENT_ID_NULL, DGCErrorCodes.RULE_STATEMENT_ID_EMPTY,
				"ruleSetRId");
		return rulesService.findLatestRuleStatementByResourceId(ruleSetRId);
	}

	@Override
	@Transactional
	public boolean removeRuleSet(String ruleSetRId) {
		Defense.notEmpty(ruleSetRId, DGCErrorCodes.RULESET_ID_NULL, DGCErrorCodes.RULESET_ID_EMPTY, "ruleSetRId");
		rulesService.removeRuleSet(rulesService.getRuleSetWithError(ruleSetRId));
		return true;
	}

	@Override
	@Transactional
	public boolean removeRuleStatement(String ruleSetRId, String ruleStatementRId) {
		Defense.notEmpty(ruleSetRId, DGCErrorCodes.RULESET_ID_NULL, DGCErrorCodes.RULESET_ID_EMPTY, "ruleSetRId");
		Defense.notEmpty(ruleStatementRId, DGCErrorCodes.RULE_STATEMENT_ID_NULL, DGCErrorCodes.RULE_STATEMENT_ID_EMPTY,
				"ruleStatementRId");
		rulesService.removeRuleStatement(rulesService.getRuleSetWithError(ruleSetRId),
				rulesService.getRuleStatementWithError(ruleStatementRId));
		return true;
	}

	@Override
	@Transactional
	public RuleSet addRuleSet(String vocabularyRId, String rulesetName) {
		Defense.notEmpty(vocabularyRId, DGCErrorCodes.VOCABULARY_ID_NULL, DGCErrorCodes.VOCABULARY_ID_EMPTY,
				"vocabularyRId");
		Defense.notEmpty(rulesetName, DGCErrorCodes.RULESET_NAME_NULL, DGCErrorCodes.RULESET_NAME_EMPTY, "rulesetName");

		Vocabulary vocabulary = representationService.getVocabularyWithError(vocabularyRId);
		RuleSet existingRuleSet = rulesService.findRuleSetByName(rulesetName, vocabulary);
		if (existingRuleSet != null && existingRuleSet.getVocabulary().getId().equals(vocabulary.getId())) {
			throw new ConstraintViolationException("RuleSet '" + rulesetName + "' already exists",
					existingRuleSet.getId(), RuleSet.class.getName(), DGCErrorCodes.RULESET_ALREADY_EXISTS);
		}

		return rulesService.saveRuleSet(ruleFactory.makeRuleSet(vocabulary, rulesetName));
	}

	@Override
	@Transactional
	public Collection<RuleStatement> addRuleStatement(String ruleSetRId, String binaryFactTypeFormRId,
			boolean isLeftReadingDirection, String type, int frequency, boolean isUniquenessConstraint) {

		Defense.notEmpty(ruleSetRId, DGCErrorCodes.RULESET_ID_NULL, DGCErrorCodes.RULESET_ID_EMPTY, "ruleSetRId");
		Defense.notEmpty(binaryFactTypeFormRId, DGCErrorCodes.BFTF_ID_NULL, DGCErrorCodes.BFTF_ID_EMPTY,
				"binaryFactTypeFormRId");

		RuleSet ruleSet = rulesService.getRuleSetWithError(ruleSetRId);
		BinaryFactTypeForm bftf = representationService.getBftfWithError(binaryFactTypeFormRId);
		ReadingDirection readingDirection = bftf.getLeftPlaceHolder();
		if (!isLeftReadingDirection) {
			readingDirection = bftf.getRightPlaceHolder();
		}
		return rulesService.createRuleStatement(ruleSet, readingDirection, type, frequency, isUniquenessConstraint);
	}

	@Override
	@Transactional
	public Collection<RuleStatement> addRuleStatement(String ruleSetRId, String headTermRId, String role,
			String corole, String tailTermRId, String type, int frequency, boolean isUniquenessConstraint) {

		Defense.notEmpty(ruleSetRId, DGCErrorCodes.RULESET_ID_NULL, DGCErrorCodes.RULESET_ID_EMPTY, "ruleSetRId");
		Defense.notEmpty(headTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "headTermRId");
		Defense.notEmpty(tailTermRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "tailTermRId");
		Defense.notEmpty(role, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "role");
		Defense.notEmpty(corole, DGCErrorCodes.ROLE_NAME_NULL, DGCErrorCodes.ROLE_NAME_EMPTY, "corole");

		RuleSet ruleSet = rulesService.getRuleSetWithError(ruleSetRId);
		Term head = representationService.getTermWithError(headTermRId);
		Term tail = representationService.getTermWithError(tailTermRId);
		return rulesService.createRuleStatement(ruleSet, head, role, corole, tail, type, frequency,
				isUniquenessConstraint);
	}

	@Override
	@Transactional
	public boolean isGeneralRuleSet(String ruleSetRId) {
		Defense.notEmpty(ruleSetRId, DGCErrorCodes.RULESET_ID_NULL, DGCErrorCodes.RULESET_ID_EMPTY, "ruleSetRId");
		RuleSet ruleSet = rulesService.getRuleSetWithError(ruleSetRId);
		return ruleSet.getName().equals(Constants.DEFAULT_RULE_SET_NAME);
	}

	@Override
	@Transactional
	public boolean removeSimpleStatement(String ruleStatementRId, String simpleStatementRId) {
		Defense.notEmpty(ruleStatementRId, DGCErrorCodes.RULE_STATEMENT_ID_NULL, DGCErrorCodes.RULE_STATEMENT_ID_EMPTY,
				"ruleStatementRId");
		Defense.notEmpty(simpleStatementRId, DGCErrorCodes.SIMPLE_STATEMENT_ID_NULL,
				DGCErrorCodes.SIMPLE_STATEMENT_ID_EMPTY, "simpleStatementRId");

		rulesService.removeSimpleStatement(rulesService.getRuleStatementWithError(ruleStatementRId),
				rulesService.getSimpleStatementWithError(simpleStatementRId));
		return true;
	}

	@Override
	@Transactional
	public MultiValueMap findRulesRefferingFactType(String binaryFactTypeFormRId) {
		Defense.notEmpty(binaryFactTypeFormRId, DGCErrorCodes.BFTF_ID_NULL, DGCErrorCodes.BFTF_ID_EMPTY,
				"binaryFactTypeFormRId");
		return rulesService.findReferences(representationService.getBftfWithError(binaryFactTypeFormRId));
	}

	@Override
	@Transactional
	public boolean removeRulesReferringTerm(String termRId) {
		Defense.notEmpty(termRId, DGCErrorCodes.TERM_ID_NULL, DGCErrorCodes.TERM_ID_EMPTY, "termRId");
		rulesService.removeReferences(representationService.getTermWithError(termRId));
		return true;
	}

	@Override
	@Transactional
	public Collection<SimpleStatement> getStatements(String ruleStatementRId) {
		Defense.notEmpty(ruleStatementRId, DGCErrorCodes.RULE_STATEMENT_ID_NULL, DGCErrorCodes.RULE_STATEMENT_ID_EMPTY,
				"ruleStatementRId");
		RuleStatement rs = rulesService.getRuleStatementWithError(ruleStatementRId);
		return new LinkedList<SimpleStatement>(rs.getSimpleStatements());
	}
}
