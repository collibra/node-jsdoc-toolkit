package com.collibra.dgc.core.model.rules;

import java.util.Set;

import com.collibra.dgc.core.model.Verbalisable;
import com.collibra.dgc.core.model.Resource;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * A Rule Set is a Set of {@link RuleStatement}s.
 * 
 * @author dtrog
 * 
 */
public interface RuleSet extends Resource, Verbalisable {

	/**
	 * 
	 * @return The {@link Vocabulary} to which the {@link RuleSet} applies.
	 */
	Vocabulary getVocabulary();

	/**
	 * 
	 * @param vocabulary The {@link Vocabulary} to which the {@link RuleSet} applies.
	 */
	void setVocabulary(Vocabulary vocabulary);

	/**
	 * 
	 * @return The name of the {@link RuleSet}.
	 */
	String getName();

	/**
	 * Set the name of the {@link RuleSet}.
	 * @param name The name of the {@link RuleSet}
	 */
	void setName(String name);

	/**
	 * 
	 * @return The {@link RuleStatement}s contained by this {@link RuleSet}.
	 */
	Set<RuleStatement> getRuleStatements();

	/**
	 * Add a {@link RuleStatement}.
	 * @param ruleStatement The {@link RuleStatement} to add to this {@link RuleSet}.
	 */
	void addRuleStatement(RuleStatement ruleStatement);

	/**
	 * 
	 * @return A field to field copy of this {@link RuleSet}.
	 */
	RuleSet clone();
}
