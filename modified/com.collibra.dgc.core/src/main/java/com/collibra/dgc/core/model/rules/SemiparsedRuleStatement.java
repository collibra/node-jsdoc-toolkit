package com.collibra.dgc.core.model.rules;

/**
 * 
 * @author amarnath
 * 
 */
public interface SemiparsedRuleStatement extends RuleStatement {
	/**
	 * 
	 * @return
	 */
	String getUnparsed();

	/**
	 * 
	 * @param unparsed
	 */
	void setUnparsed(String unparsed);
}
