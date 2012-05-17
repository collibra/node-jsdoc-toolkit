/**
 * 
 */
package com.collibra.dgc.core.dao.filter;

import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.filters.ResourceFilter;
import com.collibra.dgc.core.dto.sorters.PropertyOrder;

/**
 * @author dieterwachters
 * 
 */
public interface FilterVisitor {
	void visit(ResourceFilter filter);

	void visit(PropertyFilter propertyFilter);

	void visit(PropertyOrder propertyOrder);
}
