/**
 * 
 */
package com.collibra.dgc.core.dto.reporting;

import java.util.Collection;
import java.util.LinkedList;

import com.collibra.dgc.core.dao.reporting.ReportConfigVisitor;
import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.filters.PropertyFilter.FilterOperator;
import com.collibra.dgc.core.dto.sorters.PropertyOrder;
import com.collibra.dgc.core.model.representation.Term;

/**
 * @author fvdmaele
 *
 */
public class StatusColumnConfig extends AbstractColumnConfig implements ColumnConfig {

	private Collection<Term> statusses = new LinkedList<Term>();
	
	public StatusColumnConfig(TermColumnConfig parent) {
		super(parent);
	}
	
	public StatusColumnConfig(TermColumnConfig parent, Collection<Term> statusses) {
		super(parent);
		this.setStatusses(statusses);
	}
	
	public StatusColumnConfig(TermColumnConfig parent, String filterValue, FilterOperator filterOp, boolean sortDESC) {
		super(parent, filterValue, filterOp, sortDESC);
	}
	
	/* (non-Javadoc)
	 * @see com.collibra.dgc.core.dto.reporting.ColumnConfig#accept(com.collibra.dgc.core.dao.reporting.ReportConfigVisitor)
	 */
	@Override
	public void accept(ReportConfigVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected void addPropertyFilter(String filterValue, FilterOperator filterOp) {
		this.propertyFilter = new PropertyFilter("status", filterOp, filterValue);
	}

	@Override
	protected void addPropertyOrder(Boolean sortDesc) {
		this.propertyOrder = new PropertyOrder("status", sortDesc);
	}

	/* (non-Javadoc)
	 * @see com.collibra.dgc.core.dto.reporting.AbstractColumnConfig#getId()
	 */
	@Override
	protected String getId() {
		return getParent().getId() + "_status";
	}

	public Collection<Term> getStatusses() {
		return statusses;
	}

	public void setStatusses(Collection<Term> statusses) {
		this.statusses = statusses;
	}

	@Override
	public String getColumnType() {
		return "status";
	}

}
