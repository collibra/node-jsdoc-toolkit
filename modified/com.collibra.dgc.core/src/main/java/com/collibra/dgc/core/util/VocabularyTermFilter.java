/**
 * 
 */
package com.collibra.dgc.core.util;

/**
 * @author fvdmaele
 * 
 */
public class VocabularyTermFilter extends VocabularyFilter {

	@Override
	public String getSQLMatchOperaterValue() {
		switch (getOperator()) {
		case INCLUDES:
			return "%" + getValue().toUpperCase() + "%";
		case STARTS_WITH:
			return getValue().toUpperCase() + "%";
		case EQUALS:
			return getValue().toUpperCase();
		case ENDS_WITH:
			return "%" + getValue().toUpperCase();
		}
		return "%" + getValue().toUpperCase() + "%";
	}

	public VocabularyTermFilter(Operator op, String value) {
		super(op, value);
	}

	public VocabularyTermFilter(String op, String value) {
		super(op, value);
	}
}
