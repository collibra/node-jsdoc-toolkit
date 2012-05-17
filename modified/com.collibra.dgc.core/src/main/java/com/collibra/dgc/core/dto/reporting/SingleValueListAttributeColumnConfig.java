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
public class SingleValueListAttributeColumnConfig extends AbstractColumnConfig implements ColumnConfig {

	private Term attributeLabel = null;
	
	protected SingleValueListAttributeColumnConfig(TermColumnConfig parent, String filterValue, FilterOperator filterOp, Boolean sortASC) {
		super(parent, filterValue, filterOp, sortASC);
	}
	
	public SingleValueListAttributeColumnConfig(TermColumnConfig parent, Term attrLabel) {
		this.parent = parent;
		if(parent != null)
			parent.addColumnConfig(this);
		this.attributeLabel = attrLabel;
	}
	
	public SingleValueListAttributeColumnConfig(TermColumnConfig parent, Term attrLabel, String filterValue, FilterOperator filterOp) {
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
	 * @see com.collibra.dgc.core.dto.reporting.ColumnConfig#getColumnType()
	 */
	@Override
	public String getColumnType() {
		return "singlevaluelistattribute";
	}

	/* (non-Javadoc)
	 * @see com.collibra.dgc.core.dto.reporting.AbstractColumnConfig#addPropertyFilter(java.lang.String, com.collibra.dgc.core.dto.filters.PropertyFilter.FilterOperator)
	 */
	@Override
	protected void addPropertyFilter(String filterValue, FilterOperator filterOp) {
		this.propertyFilter = new PropertyFilter("value", filterOp, filterValue);
	}

	/* (non-Javadoc)
	 * @see com.collibra.dgc.core.dto.reporting.AbstractColumnConfig#addPropertyOrder(java.lang.Boolean)
	 */
	@Override
	protected void addPropertyOrder(Boolean sortDesc) {
		this.propertyOrder = new PropertyOrder("value", sortDesc);
	}

	/* (non-Javadoc)
	 * @see com.collibra.dgc.core.dto.reporting.AbstractColumnConfig#getId()
	 */
	@Override
	protected String getId() {
		return parent.getName() + "_" + attributeLabel.getId().toString();
	}

	public Term getAttributeLabel() {
		return attributeLabel;
	}
}
