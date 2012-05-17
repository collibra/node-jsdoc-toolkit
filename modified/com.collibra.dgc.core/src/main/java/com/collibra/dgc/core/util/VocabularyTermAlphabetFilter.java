/**
 * 
 */
package com.collibra.dgc.core.util;

/**
 * @author fvdmaele
 * 
 */
public class VocabularyTermAlphabetFilter {

	String firstLetter = "";
	String secondLetter = "";
	Operator op1 = null;
	Operator op2 = null;

	// supported operators - these will be translated to SQL match operators
	public enum Operator {
		SMALLER, SMALLEROREQUAL, LARGER, LARGEROREQUAL
	};

	public VocabularyTermAlphabetFilter(String firstLetter, Operator op) {
		this.firstLetter = firstLetter;
		this.op1 = op;
	}

	public VocabularyTermAlphabetFilter(String firstLetter, String op) {
		this.firstLetter = firstLetter;
		this.op1 = Operator.valueOf(op);
		;
	}

	public VocabularyTermAlphabetFilter(String firstLetter, Operator op1, String secondLetter, Operator op2) {
		this.firstLetter = firstLetter;
		this.op1 = op1;
		this.secondLetter = secondLetter;
		this.op2 = op2;
	}

	public VocabularyTermAlphabetFilter(String firstLetter, String op1, String secondLetter, String op2) {
		this.firstLetter = firstLetter;
		this.op1 = Operator.valueOf(op1);
		this.secondLetter = secondLetter;
		this.op2 = Operator.valueOf(op2);
	}

	public String getSQLMatchOperaterValue(String columnName) {
		if (op2 == null) {
			return "" + columnName + " " + getOperatorString(op1) + " '" + firstLetter + "'";
		} else {
			return "" + columnName + " " + getOperatorString(op1) + " '" + firstLetter + "' AND " + columnName + " "
					+ getOperatorString(op2) + " '" + secondLetter + "'";
		}
	}

	protected String getOperatorString(Operator op) {
		switch (op) {
		case SMALLER:
			return "<";
		case SMALLEROREQUAL:
			return "<=";
		case LARGER:
			return ">";
		case LARGEROREQUAL:
			return ">=";
		}
		return "";
	}
}
