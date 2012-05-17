package com.collibra.dgc.core.model.rules;

import java.util.Set;

import com.collibra.dgc.core.model.meaning.ObjectType;
import com.collibra.dgc.core.model.meaning.Proposition;

/**
 * A Rule is a proposition that is a claim of obligation or of necessity.
 * 
 * @author dtrog
 * 
 */
public interface Rule extends Proposition {
	public static String MANDATORY = "Mandatory"; // Mandatory constraint
	public static String UNIQUENESS = "Uniqueness"; // Uniqueness constraint
	public static String SEMI_PARSED = "Semiparsed"; // Semi-parsed rule type
	public static String FREQUENCY = "Frequency";

	/**
	 * 
	 * @return The {@link RuleStatement}s that represent this {@link Rule}.
	 */
	Set<RuleStatement> getRuleStatements();

	/**
	 * 
	 * @return The {@link ObjectType} that indicates the type of the constraint.
	 */
	ObjectType getContraintType();

	/**
	 * 
	 * @return The constraint type {@link GlossaryConstraintType}.
	 */
	String getGlossaryConstraintType();
}
