/**
 * 
 */
package com.collibra.dgc.core.dto.reporting;

import java.util.LinkedList;
import java.util.List;

import com.collibra.dgc.core.dao.reporting.ReportConfigVisitor;
import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.filters.PropertyFilter.FilterOperator;
import com.collibra.dgc.core.dto.sorters.PropertyOrder;

/**
 * @author fvdmaele
 *
 */
public class TermColumnConfig extends AbstractColumnConfig implements ColumnConfig {

	List<ColumnConfig> columns = new LinkedList<ColumnConfig>();
	List<TermColumnConfig> designationColumnConfigs = new LinkedList<TermColumnConfig>();

	public TermColumnConfig() {
		super(null);
	}

	public TermColumnConfig(String filterValue, FilterOperator filterOp, Boolean sortASC) {
		super(null, filterValue, filterOp, sortASC);
	}
	
	public void addColumnConfig(ColumnConfig cc) {
		columns.add(cc);
	}

	public void addDesignationConfig(TermColumnConfig dc) {
		designationColumnConfigs.add(dc);
	}
	
	public void accept(ReportConfigVisitor visitor) {
		// visit column children (e.g. vocabulary, attribute, concept type, ...)
		for(ColumnConfig cc : columns)
			cc.accept(visitor);
		
		visitor.visit(this);
		
		// visit other designation column configs
		for(TermColumnConfig cc : designationColumnConfigs)
			cc.accept(visitor);
	}

	protected String getId() {
		return "term";
	}
	
	public List<TermColumnConfig> getDesignationColumnChildren() {
		return designationColumnConfigs;
	}
	
	public List<ColumnConfig> getColumnConfigs() {
		return columns;
	}

	@Override
	protected void addPropertyFilter(String filterValue, FilterOperator filterOp) {
		this.propertyFilter = new PropertyFilter("signifier", filterOp, filterValue);
	}

	@Override
	protected void addPropertyOrder(Boolean sortDesc) {
		this.propertyOrder = new PropertyOrder("signifier", sortDesc);
	}

	@Override
	public String getColumnType() {
		return "term";
	}
}
