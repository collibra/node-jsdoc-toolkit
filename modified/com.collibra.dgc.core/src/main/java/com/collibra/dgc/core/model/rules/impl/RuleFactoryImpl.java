package com.collibra.dgc.core.model.rules.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.impl.SimpleStatementImpl;
import com.collibra.dgc.core.model.rules.FrequencyRuleStatement;
import com.collibra.dgc.core.model.rules.Rule;
import com.collibra.dgc.core.model.rules.RuleFactory;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.SemiparsedRuleStatement;
import com.collibra.dgc.core.model.rules.SimpleRuleStatement;

@Service
public class RuleFactoryImpl implements RuleFactory {

	@Autowired
	private MeaningFactory meaningFactory;

	public RuleSet makeRuleSet(Vocabulary vocabulary, String name) {
		RuleSetImpl ruleSet = new RuleSetImpl(vocabulary, name);
		vocabulary.addRuleSet(ruleSet);
		return ruleSet;
	}

	public Rule makeMandatoryRule() {
		return makeMandatoryRule(UUID.randomUUID().toString());
	}

	public Rule makeMandatoryRule(String uuid) {
		RuleImpl rule = new RuleImpl(uuid);
		rule.setGlossaryConstraintType(Rule.MANDATORY);
		return rule;
	}

	public Rule makeUniquesnessRule() {
		return makeUniquesnessRule(UUID.randomUUID().toString());
	}

	public Rule makeUniquesnessRule(String uuid) {
		RuleImpl rule = new RuleImpl(uuid);
		rule.setGlossaryConstraintType(Rule.UNIQUENESS);
		return rule;
	}

	public Rule makeSemiparsedRule() {
		return makeSemiparsedRule(UUID.randomUUID().toString());
	}

	public Rule makeSemiparsedRule(final String uuid) {
		RuleImpl rule = new RuleImpl(uuid);
		rule.setGlossaryConstraintType(Rule.SEMI_PARSED);
		return rule;
	}

	public SimpleStatement makeSimpleStatement(Vocabulary vocabulary) {
		SimpleStatementImpl ss = new SimpleStatementImpl(vocabulary);
		ss.setSimpleProposition(meaningFactory.makeSimpleProposition());
		ss.getSimpleProposition().addSimpleStatement(ss);
		return ss;
	}

	public Rule makeFrequencyRule() {
		return makeFrequencyRule(UUID.randomUUID().toString());
	}

	public Rule makeFrequencyRule(final String uuid) {
		RuleImpl rule = new RuleImpl(uuid);
		rule.setGlossaryConstraintType(Rule.FREQUENCY);
		return rule;
	}

	public SimpleRuleStatement makeMandatoryRuleStatement(Vocabulary vocabulary) {
		SimpleRuleStatementImpl rs = new SimpleRuleStatementImpl(vocabulary);
		rs.setRule(makeMandatoryRule());
		return rs;
	}

	public SimpleRuleStatement makeUniquenessRuleStatement(Vocabulary vocabulary) {
		SimpleRuleStatementImpl rs = new SimpleRuleStatementImpl(vocabulary);
		rs.setRule(makeUniquesnessRule());
		return rs;
	}

	public SemiparsedRuleStatement makeSemiparsedRuleStatement(Vocabulary vocabulary) {
		SemiparsedRuleStatementImpl sprs = new SemiparsedRuleStatementImpl(vocabulary);
		sprs.setRule(makeSemiparsedRule());
		return sprs;
	}

	public FrequencyRuleStatement makeFrequencyRuleStatement(Vocabulary vocabulary) {
		FrequencyRuleStatementImpl fqrs = new FrequencyRuleStatementImpl(vocabulary);
		fqrs.setRule(makeFrequencyRule());
		return fqrs;
	}
}
