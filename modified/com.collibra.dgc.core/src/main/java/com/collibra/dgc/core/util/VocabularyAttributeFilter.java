/**
 * 
 */
package com.collibra.dgc.core.util;

/**
 * @author fvdmaele
 * 
 */
public class VocabularyAttributeFilter extends VocabularyFilter {

	private final String attrTypeResourceId;

	public VocabularyAttributeFilter(String attrTypeResourceId, Operator op, String value) {
		super(op, value);
		this.attrTypeResourceId = attrTypeResourceId;
	}

	public VocabularyAttributeFilter(String attrTypeResourceId, String op, String value) {
		super(op, value);
		this.attrTypeResourceId = attrTypeResourceId;
	}

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

	/*
	 * Creates a general filter that will be applied on every attribute
	 */
	public VocabularyAttributeFilter(String op, String value) {
		super(op, value);
		this.attrTypeResourceId = null;
	}

	public String getAttrTypeResourceId() {
		return attrTypeResourceId;
	}
}
