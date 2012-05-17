/**
 * 
 */
package com.collibra.dgc.core.dto.reporting;

import java.util.List;

import com.collibra.dgc.core.dao.reporting.ReportConfigVisitor;
import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.sorters.PropertyOrder;

/**
 * @author fvdmaele
 *
 */
public interface ColumnConfig {
	
	PropertyFilter getPropertyFilter();
	
	boolean isFiltered();
	
	PropertyOrder getPropertyOrder();
	
	boolean isOrdered();
	
	void accept(ReportConfigVisitor visitor);
	
	String getName();
	
	String getContentField();
	
	String getResourceIdField();
	
	void setName(String name);
	
	public TermColumnConfig getParent();
	
	public void setSortDESC(boolean sortDESC);

	public void setPropertyFilter(PropertyFilter filter);
	
	public void setPropertyOrder(PropertyOrder order);
	
	public String getColumnType();
}
