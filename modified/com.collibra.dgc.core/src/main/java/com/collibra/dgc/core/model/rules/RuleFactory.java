package com.collibra.dgc.core.model.rules;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.collibra.dgc.core.model.meaning.Meaning;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * Factory for creating resources for {@link Rule}s. This includes {@link Meaning} and {@link Representation} resources.
 * 
 * @author dtrog
 * 
 */
@Service
public interface RuleFactory {

	/**
	 * Creates a new {@link RuleSet}.
	 * 
	 * @param vocabulary The {@link Vocabulary} that should contain this {@link RuleSet}.
	 * @param name The name for the {@link RuleSet}.
	 * @return The new {@link RuleSet}.
	 */
	RuleSet makeRuleSet(Vocabulary vocabulary, String name);

	/**
	 * Creates a new {@link Rule} of type mandatory constraint.
	 * @return
	 */
	Rule makeMandatoryRule();

	/**
	 * Creates a new {@link Rule} of type uniqueness constraint.
	 * @return
	 */
	Rule makeUniquesnessRule();

	/**
	 * Creates a new {@link Rule} of type mandatory constraint with the specified {@link UUID}.
	 * @param uuid The {@link UUID} for the rule.
	 * @return
	 */
	Rule makeMandatoryRule(String uuid);

	/**
	 * Creates a new {@link Rule} of type uniqueness constraint with specified {@link UUID}.
	 * @param uuid The {@link UUID} for the rule.
	 * @return
	 */
	Rule makeUniquesnessRule(String uuid);

	/**
	 * @return Creates new {@link SimpleRuleStatement} of type mandatory {@link GlossaryConstraintType}
	 */
	SimpleRuleStatement makeMandatoryRuleStatement(Vocabulary vocabulary);

	/**
	 * @return Creates new {@link SimpleRuleStatement} of type uniqueness {@link GlossaryConstraintType}
	 */
	SimpleRuleStatement makeUniquenessRuleStatement(Vocabulary vocabulary);

	/**
	 * @return Creates new {@link SemiparsedRuleStatement}.
	 */
	SemiparsedRuleStatement makeSemiparsedRuleStatement(Vocabulary vocabulary);

	/**
	 * @return Creates a new {@link SimpleStatement}
	 */
	SimpleStatement makeSimpleStatement(Vocabulary vocabulary);

	/**
	 * 
	 * @return
	 */
	Rule makeFrequencyRule();

	/**
	 * 
	 * @return
	 */
	Rule makeFrequencyRule(String uuid);

	/**
	 * 
	 * @return
	 */
	FrequencyRuleStatement makeFrequencyRuleStatement(Vocabulary vocabulary);
}
