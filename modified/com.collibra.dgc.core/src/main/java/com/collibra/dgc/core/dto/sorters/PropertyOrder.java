/**
 * 
 */
package com.collibra.dgc.core.dto.sorters;

import com.collibra.dgc.core.dao.filter.FilterVisitor;

/**
 * @author fvdmaele
 * 
 */
public class PropertyOrder {

	private final String property;
	private final boolean sortDesc;

	public PropertyOrder(String property, boolean sortDesc) {
		this.property = property;
		this.sortDesc = sortDesc;
	}

	/**
	 * @return the property on which will be ordered
	 */
	public String getProperty() {
		return this.property;
	}

	/**
	 * @return the sort order, true if descending, false if ascending
	 */
	public boolean sortDesc() {
		return this.sortDesc;
	}

	public void accept(FilterVisitor visitor) {
		visitor.visit(this);
	}
}
