/**
 * 
 */
package com.collibra.dgc.core.dto.reporting;

import java.util.LinkedList;
import java.util.List;

import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.filters.PropertyFilter.FilterOperator;
import com.collibra.dgc.core.dto.sorters.PropertyOrder;

/**
 * @author fvdmaele
 *
 */
public abstract class AbstractColumnConfig implements ColumnConfig {
	
	protected String contentField;
	protected String rIdField;
	protected TermColumnConfig parent;
	protected String name;
	protected PropertyFilter propertyFilter = null;
	protected PropertyOrder propertyOrder = null;
	
	protected AbstractColumnConfig() {
	}
	
	public AbstractColumnConfig(TermColumnConfig parent) {
		this.parent = parent;
		if(parent != null)
			parent.addColumnConfig(this);
	}
	
	public AbstractColumnConfig(TermColumnConfig parent, String filterValue, FilterOperator filterOp, Boolean sortDESC) {
		this.parent = parent;
		if(parent != null)
			parent.addColumnConfig(this);
		addPropertyFilter(filterValue, filterOp);
		if(sortDESC != null)
			addPropertyOrder(sortDESC);
	}

	protected abstract void addPropertyFilter(String filterValue, FilterOperator filterOp);
	
	public boolean isFiltered() {
		return this.propertyFilter != null;
	}
	
	protected abstract void addPropertyOrder(Boolean sortDesc);
	
	public boolean isOrdered() {
		return this.propertyOrder != null;
	}
	
	public PropertyFilter getPropertyFilter() {
		return this.propertyFilter;
	}
	
	public PropertyOrder getPropertyOrder() {
		return this.propertyOrder;
	}
	
	public void setPropertyFilter(PropertyFilter filter) {
		this.propertyFilter = filter;
	}
	
	public void setPropertyOrder(PropertyOrder order) {
		this.propertyOrder = order;
	}
	
	public TermColumnConfig getParent() {
		return parent;
	}
	
	public void setParent(TermColumnConfig cc) {
		this.parent = cc;
	}
	
	public String getName() {
		if(name != null)
			return name;
		
		return getId();
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getContentField() {
		return getName();
	}

	@Override
	public String getResourceIdField() {
		return getContentField() + "_rid";
	}
	
	public void setSortDESC(boolean sortDESC) {
		propertyOrder = new PropertyOrder("", sortDESC);
	}
	
	protected abstract String getId();
}
