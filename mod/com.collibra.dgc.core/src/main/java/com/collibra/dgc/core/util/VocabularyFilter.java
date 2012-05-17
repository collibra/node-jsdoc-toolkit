/**
 * 
 */
package com.collibra.dgc.core.util;

/**
 * @author fvdmaele
 * 
 *         This is an abstract class that contains details of a filter that can be applied on a vocabulary. It contains
 *         the value of the filter It contains the operator of the filter
 */
public abstract class VocabularyFilter {

	// supported operators - these will be translated to SQL match operators
	public enum Operator {
		INCLUDES, STARTS_WITH, EQUALS, ENDS_WITH
	};

	protected final String value;
	protected final Operator operator;

	VocabularyFilter(Operator operator, String value) {
		this.operator = operator;
		this.value = value;
	}

	VocabularyFilter(String operator, String value) {
		this.operator = Operator.valueOf(operator);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Operator getOperator() {
		return operator;
	}

	public abstract String getSQLMatchOperaterValue();
}
