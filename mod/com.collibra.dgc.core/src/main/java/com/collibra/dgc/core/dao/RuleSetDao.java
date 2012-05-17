package com.collibra.dgc.core.dao;

import java.util.List;

import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;

public interface RuleSetDao extends AbstractDao<RuleSet> {

	/**
	 * Retrieves a {@link RuleSet} by its {@link Vocabulary} and name
	 * 
	 * @param Vocabulary The {@link Vocabulary}.
	 * @param name The {@link RuleSet} name.
	 * @return The {@link RuleSet}.
	 */
	RuleSet findByName(Vocabulary vocabulary, String name);

	/**
	 * Find the {@link RuleSet}s for specified {@link RuleStatement}.
	 * @param rs The {@link RuleStatement}.
	 * @return The {@link List} {@link RuleSet}s.
	 */
	List<RuleSet> find(RuleStatement rs);

	/**
	 * 
	 * @param vocabulary
	 * @return
	 */
	List<RuleSet> findAll(Vocabulary vocabulary);

	// /**
	// * Check if the rule set is persisted or not.
	// * @param ruleSet The {@link RuleSet}
	// * @return {@code true} if it is persisted, {@code false} otherwise
	// */
	// boolean isPersisted(final RuleSet ruleSet);
}
