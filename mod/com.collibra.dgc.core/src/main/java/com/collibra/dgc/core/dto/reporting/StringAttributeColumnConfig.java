/**
 * 
 */
package com.collibra.dgc.core.dto.reporting;

import com.collibra.dgc.core.dao.reporting.ReportConfigVisitor;
import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.filters.PropertyFilter.FilterOperator;
import com.collibra.dgc.core.dto.sorters.PropertyOrder;
import com.collibra.dgc.core.model.representation.Term;

/**
 * @author fvdmaele
 *
 */
public class StringAttributeColumnConfig extends AbstractColumnConfig implements ColumnConfig {

	private Term attributeLabel = null;
	
	protected StringAttributeColumnConfig(TermColumnConfig parent, String filterValue, FilterOperator filterOp,
			Boolean sortASC) {
		super(parent, filterValue, filterOp, sortASC);
	}

	private StringAttributeColumnConfig(TermColumnConfig parent) {
		super(parent);
	}

	public StringAttributeColumnConfig(TermColumnConfig parent, Term attrLabel) {
		this.parent = parent;
		if(parent != null)
			parent.addColumnConfig(this);
		this.attributeLabel = attrLabel;
	}

	public StringAttributeColumnConfig(TermColumnConfig parent, Term attrLabel, String filterValue, FilterOperator filterOp) {
		this.parent = parent;
		if(parent != null)
			parent.addColumnConfig(this);
		this.attributeLabel = attrLabel;
		addPropertyFilter(filterValue, filterOp);
	}
	
	/* (non-Javadoc)
	 * @see com.collibra.dgc.core.dto.reporting.ColumnConfig#accept(com.collibra.dgc.core.dao.reporting.ReportConfigVisitor)
	 */
	@Override
	public void accept(ReportConfigVisitor visitor) {
		visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see com.collibra.dgc.core.dto.reporting.ColumnConfig#getId()
	 */
	@Override
	public String getId() {
		return parent.getName() + "_" + attributeLabel.getId().toString();
	}

	@Override
	protected void addPropertyFilter(String filterValue, FilterOperator filterOp) {
		this.propertyFilter = new PropertyFilter("value", filterOp, filterValue);
	}

	@Override
	protected void addPropertyOrder(Boolean sortDesc) {
		this.propertyOrder = new PropertyOrder("value", sortDesc);
	}

	public Term getAttributeLabel() {
		return this.attributeLabel;
	}

	@Override
	public String getColumnType() {
		return "stringattribute";
	}
}
