package com.collibra.dgc.core.component;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;

import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.model.representation.facttypeform.BinaryFactTypeForm;
import com.collibra.dgc.core.model.representation.facttypeform.ReadingDirection;
import com.collibra.dgc.core.model.rules.RuleSet;
import com.collibra.dgc.core.model.rules.RuleStatement;

/**
 * Rules API
 * @author amarnath
 */
public interface RulesComponent {
	/**
	 * To get all {@link RuleSet}s for a specified {@link Vocabulary}.
	 * @param rId The resource id of the {@link Vocabulary}.
	 * @return The {@link List} of {@link RuleSet}s.
	 */
	Collection<RuleSet> getRuleSetsForVocabulary(String rId);

	/**
	 * To get the latest {@link RuleSet} with a specific resource id.
	 * @param rId The resource id of the {@link RuleSet}.
	 * @return {@link RuleSet}.
	 */
	RuleSet getRuleSet(String rId);

	/**
	 * To get {@link RuleStatement} with a specific resource id.
	 * @param rId The resource id of the {@link RuleStatement}.
	 * @return {@link RuleStatement}.
	 */
	RuleStatement getRuleStatement(String rId);

	/**
	 * Removes specified {@link RuleSet}.
	 * @param ruleSetRId The resource id of the {@link RuleSet} to be removed.
	 * @return <code>true</code> if the {@link RuleSet} is removed.
	 */
	boolean removeRuleSet(String ruleSetRId);

	/**
	 * Removes specified {@link RuleStatement} from the {@link RuleSet}
	 * @param ruleSetRId The resource id of {@link RuleSet}
	 * @param ruleStatement The resource id of {@link RuleStatement}.
	 */
	boolean removeRuleStatement(String ruleSetRId, String ruleStatementRId);

	/**
	 * Creates new {@link RuleSet} with specified name for specified {@link Vocabulary}.
	 * @param vocabularyRId The resource id {@link Vocabulary} who owns the newly created {@link RuleSet}.
	 * @param rulesetName The {@link RuleSet} name.
	 * @return newly created {@link RuleSet}
	 */
	RuleSet addRuleSet(String vocabularyRId, String rulesetName);

	/**
	 * Creates a {@link RuleStatement} with the provided information for the given {@link ReadingDirection}.
	 * @param ruleSetRId The resource id of {@link RuleSet} to which the {@link RuleStatement} is to be added.
	 * @param binaryFactTypeFormRId {@link BinaryFactTypeForm} on which rule is being.
	 * @param isLeftReadingDirection If true rule will be applied on the left to right {@link ReadingDirection} of the
	 *            {@link BinaryFactTypeForm}, otherwise applied on the right to left {@link ReadingDirection}.
	 * @param type The {@link String} type required to build the type of the {@link RuleStatement}.
	 * @param frequency The integer type of the frequency which is required to build the type of {@link RuleStatement}.
	 * @param isUniquenessConstraint <code>true</code> if {@link RuleStatement} is unique.
	 * @return newly created {@link Set} of {@link RuleStatement}.
	 */
	Collection<RuleStatement> addRuleStatement(String ruleSetRId, String binaryFactTypeFormRId,
			boolean isLeftReadingDirection, String type, int frequency, boolean isUniquenessConstraint);

	/**
	 * Create {@link RuleStatement} with the details specified as parameters
	 * @param ruleSetRId {@link RuleSet} resource id.
	 * @param headTermRId Head {@link Term} resource id.
	 * @param role Role string.
	 * @param corole Corole string.
	 * @param tailTermRId Tail {@link Term} resource id.
	 * @param type Constraint type.
	 * @param frequency The frequency number.
	 * @param isUniquenessConstraint If true then enforce unique identifier constraint.
	 * @return {@link Set} of {@link RuleStatement}s created.
	 */
	Collection<RuleStatement> addRuleStatement(String ruleSetRId, String headTermRId, String role, String corole,
			String tailTermRId, String type, int frequency, boolean isUniquenessConstraint);

	/**
	 * returns true if the current {@link RuleSet} is a general rule set.
	 * @param ruleSetRId Resource id of the {@link RuleSet} which is to be tested.
	 * @return <code>true</code> if the {@link RuleSet} is general.
	 */
	boolean isGeneralRuleSet(String ruleSetRId);

	/**
	 * To remove {@link SimpleStatement} from {@link RuleStatement}.
	 * @param ruleStatementRId Resource id of the {@link RuleStatement} from which the {@link SimpleStatement} to be
	 *            removed.
	 * @param simpleStatementRId Resource id of the {@link SimpleStatement} to be removed.
	 * @return True if success otherwise false.
	 */
	boolean removeSimpleStatement(String ruleStatementRId, String simpleStatementRId);

	/**
	 * To get all rule references for the specified {@link BinaryFactTypeForm}.
	 * @param binaryFactTypeFormRId Resource id of the {@link BinaryFactTypeForm}
	 * @return The {@link MultiValueMap} with details as [[RuleSet] -> [[RuleStatement] --> [[Statement]] ] ]
	 */
	MultiValueMap findRulesRefferingFactType(String binaryFactTypeFormRId);

	/**
	 * Remove all rules referring to {@link Term}.
	 * @param termRId The {@link Term} resource id.
	 * @return True if successful, otherwise false.
	 */
	boolean removeRulesReferringTerm(String termRId);

	/**
	 * Get {@link SimpleStatement}s of a {@link RuleStatement}.
	 * @param ruleStatementRId The {@link RuleStatement} resource id.
	 * @return {@link SimpleStatement}s.
	 */
	Collection<SimpleStatement> getStatements(String ruleStatementRId);
}