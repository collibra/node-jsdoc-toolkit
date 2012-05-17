package com.collibra.dgc.core.service;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;

import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.rules.FrequencyRuleStatement;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;
import com.collibra.dgc.core.model.rules.SemiparsedRuleStatement;
import com.collibra.dgc.core.model.rules.SimpleRuleStatement;

public interface RulesService {
	public static String RULES_MANAGER_CONTEXT = "rulesmanager";

	/**
	 * Persists specified {@link RuleSet}.
	 * @param ruleSet The {@link RuleSet} to be persisted.
	 * @return The persisted {@link RuleSet}.
	 */
	RuleSet saveRuleSet(final RuleSet ruleSet);

	/**
	 * Persists specified {@link SemiparsedRuleStatement}.
	 * @param ruleSet
	 * @param ruleStatement The {@link SemiparsedRuleStatement} to be persisted.
	 * @return The persisted {@link SemiparsedRuleStatement}.
	 */
	SemiparsedRuleStatement saveSemiparsedRuleStatement(RuleSet ruleSet, SemiparsedRuleStatement ruleStatement);

	/**
	 * Persists specified {@link SimpleRuleStatement}.
	 * @param ruleSet
	 * @param ruleStatement The {@link SimpleRuleStatement} to be persisted.
	 * @return The persisted {@link SimpleRuleStatement}.
	 */
	SimpleRuleStatement saveSimpleRuleStatement(RuleSet ruleSet, SimpleRuleStatement ruleStatement);

	/**
	 * To get {@link RuleSet} with specified name from specified {@link Vocabulary}.
	 * @param rulesetName The {@link RuleSet} name.
	 * @param vocabulary The {@link Vocabulary} to which the {@link RuleSet} belongs.
	 * @return The {@link RuleSet}
	 */
	RuleSet findRuleSetByName(String rulesetName, Vocabulary vocabulary);

	/**
	 * To get the {@link RuleSet} with specified resource id
	 * @param resourceId The resource id of the {@link RuleSet} to get.
	 * @return The {@link RuleSet} with specified resource id
	 */
	RuleSet findRuleSetByResourceId(String resourceId);

	/**
	 * Removes the specified {@link RuleSet}.
	 * @param ruleSet The {@link RuleSet} to be removed.
	 */
	void removeRuleSet(RuleSet ruleSet);

	/**
	 * Removes the specified {@link RuleStatement} from the specified {@link RuleSet}.
	 * @param ruleSet The {@link RuleSet}
	 * @param ruleStatement The {@link RuleStatement} to be removed.
	 */
	void removeRuleStatement(RuleSet ruleSet, RuleStatement ruleStatement);

	/**
	 * Persists specified {@link FrequencyRuleStatement}.
	 * @param ruleSet
	 * @param ruleStatement
	 * @return
	 */
	FrequencyRuleStatement saveFrequencyRuleStatement(final RuleSet ruleSet, final FrequencyRuleStatement ruleStatement);

	/**
	 * To get the latest {@link RuleStatement}.
	 * @param resourceId The resource id of the {@link RuleStatement}.
	 * @return The {@link RuleStatement}.
	 */
	RuleStatement findLatestRuleStatementByResourceId(String resourceId);

	/**
	 * Removes {@link SimpleStatement}.
	 * @param ruleStatement The {@link RuleStatement} from which the {@link SimpleStatement} to be removed.
	 * @param ss The {@link SimpleStatement}.
	 */
	void removeSimpleStatement(final RuleStatement ruleStatement, final SimpleStatement ss);

	/**
	 * Find {@link RuleSet}s for the specified {@link RuleStatement}.
	 * @param rs The {@link RuleStatement}.
	 * @return The {@link List} of {@link RuleSet}s.
	 */
	List<RuleSet> findRuleSets(RuleStatement rs);

	/**
	 * Find {@link RuleStatement}s for the specified {@link SimpleStatement}.
	 * @param ss The {@link SimpleStatement}.
	 * @return The {@link List} of {@link RuleStatement}s.
	 */
	List<RuleStatement> findRuleStatements(SimpleStatement ss);

	/**
	 * Find the {@link RuleStatement}s for the specified {@link BinaryFactTypeForm}.
	 * @param bftf The {@link BinaryFactTypeForm}.
	 * @return The {@link List} of {@link RuleStatement}s.
	 */
	List<RuleStatement> findRuleStatements(BinaryFactTypeForm bftf);

	/**
	 * Find the {@link RuleStatement}s for the specified {@link Term}.
	 * @param term The {@link Term}.
	 * @return The {@link List} of {@link RuleStatement}s.
	 */
	List<RuleStatement> findRuleStatements(Term term);

	/**
	 * Find the {@link SimpleStatement}s for specified {@link BinaryFactTypeForm}.
	 * @param bftf The {@link BinaryFactTypeForm}.
	 * @return The {@link List} of {@link SimpleStatement}s.
	 */
	List<SimpleStatement> findStatements(BinaryFactTypeForm bftf);

	/**
	 * Find the {@link SimpleStatement}s for specified {@link Term}.
	 * @param term The {@link Term}.
	 * @return The {@link List} of {@link SimpleStatement}s.
	 */
	List<SimpleStatement> findStatements(Term term);

	/**
	 * 
	 * @param bftf
	 * @return
	 */
	void removeReferences(BinaryFactTypeForm bftf);

	/**
	 * 
	 * @param term
	 * @return
	 */
	void removeReferences(Term term);

	/**
	 * 
	 * @param statements
	 * @return
	 */
	MultiValueMap findReferences(List<SimpleStatement> statements);

	/**
	 * 
	 * @param bftf
	 * @return
	 */
	MultiValueMap findReferences(BinaryFactTypeForm bftf);

	/**
	 * 
	 * @param term
	 * @return
	 */
	List<SimpleStatement> findAllStatements(Term term);

	/**
	 * 
	 * @param bftf
	 * @return
	 */
	List<SimpleStatement> findAllStatements(BinaryFactTypeForm bftf);

	/**
	 * 
	 * @param ss
	 * @return
	 */
	List<RuleStatement> findAllRuleStatements(SimpleStatement ss);

	/**
	 * 
	 * @param bftf
	 * @return
	 */
	List<RuleStatement> findAllRuleStatements(BinaryFactTypeForm bftf);

	/**
	 * 
	 * @param term
	 * @return
	 */
	List<RuleStatement> findAllRuleStatements(Term term);

	/**
	 * 
	 * @param rs
	 * @return
	 */
	List<RuleSet> findAllRuleSets(RuleStatement rs);

	/**
	 * 
	 * @param ruleSet
	 * @param placeHolder
	 * @param type
	 * @param frequency
	 * @param isUniquenessConstraint
	 * @return
	 */
	Set<RuleStatement> createRuleStatement(RuleSet ruleSet, ReadingDirection placeHolder, String type, int frequency,
			boolean isUniquenessConstraint);

	/**
	 * Create {@link RuleStatement} with the details specified as parameters
	 * @param ruleSet The {@link RuleSet}.
	 * @param head The head {@link Term}.
	 * @param role Role string.
	 * @param corole Corole string.
	 * @param tail The tail {@link Term}
	 * @param type Constraint type.
	 * @param frequency The frequency number.
	 * @param isUniquenessConstraint If true then enforce unique identifier constraint.
	 * @return {@link Set} of {@link RuleStatement}s created.
	 */
	Set<RuleStatement> createRuleStatement(RuleSet ruleSet, Term head, String role, String corole, Term tail,
			String type, int frequency, boolean isUniquenessConstraint);

	/**
	 * 
	 * @param resourceId
	 * @return
	 */
	RuleSet getRuleSetWithError(String resourceId);

	/**
	 * 
	 * @param resourceId
	 * @return
	 */
	RuleStatement getRuleStatementWithError(String resourceId);

	/**
	 * 
	 * @param resourceId
	 * @return
	 */
	SimpleStatement getSimpleStatementWithError(String resourceId);
}
