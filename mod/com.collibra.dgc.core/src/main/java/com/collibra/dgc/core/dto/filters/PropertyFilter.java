/**
 * 
 */
package com.collibra.dgc.core.dto.filters;

import com.collibra.dgc.core.dao.filter.FilterVisitor;

/**
 * @author fvdmaele
 * 
 */
public class PropertyFilter {

	/**
	 * A FilterOperator specifies the matching operator between the property and its value.
	 */
	public enum FilterOperator {
		INCLUDES, STARTS_WITH, EQUALS, ENDS_WITH
	};

	private final String property;
	private final FilterOperator op;
	private final String value;

	public PropertyFilter(String property, FilterOperator op, String value) {
		this.property = property;
		this.op = op;
		this.value = value;
	}

	/**
	 * @return the property {@link String} on which will be filtered
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @return the value of the property
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the {@link FilterOperator}
	 */
	public FilterOperator getFilterOperator() {
		return op;
	}

	public void accept(FilterVisitor visitor) {
		visitor.visit(this);
	}
}
