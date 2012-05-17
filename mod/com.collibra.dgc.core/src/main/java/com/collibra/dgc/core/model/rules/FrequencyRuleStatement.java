package com.collibra.dgc.core.model.rules;

/**
 * Frequrency constraint.
 * @author amarnath
 * 
 */
public interface FrequencyRuleStatement extends RuleStatement {
	public static final int DO_NOT_USE = -1;

	/**
	 * 
	 * @return
	 */
	int getMax();

	/**
	 * 
	 * @param max
	 */
	void setMax(int max);

	/**
	 * 
	 * @return
	 */
	int getMin();

	/**
	 * 
	 * @param min
	 */
	void setMin(int min);
}
