package com.collibra.dgc.core.model.rules;

import java.util.Set;

import com.collibra.dgc.core.model.representation.SimpleStatement;
import com.collibra.dgc.core.model.representation.Statement;

/**
 * A Rule Statement is a guidance statement that expresses an operative business rule or a structural rule.
 * 
 * @author dtrog
 * 
 */
public interface RuleStatement extends Statement {

	/**
	 * 
	 * @return The rule that is expressed by the statement.
	 */
	Rule getRule();

	/**
	 * @return The {@link Statement}s this {@link RuleStatement} is composed of.
	 */
	Set<SimpleStatement> getSimpleStatements();

	/**
	 * Adds the given {@link SimpleStatement} to this {@link RuleStatement}.
	 * @param statement {@link SimpleStatement} to add to this {@link RuleStatement}.
	 */
	void addSimpleStatement(SimpleStatement simpleStatement);
}
