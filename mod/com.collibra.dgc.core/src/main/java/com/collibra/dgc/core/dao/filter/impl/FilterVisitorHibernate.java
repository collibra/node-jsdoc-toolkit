/**
 * 
 */
package com.collibra.dgc.core.dao.filter.impl;

import java.util.HashMap;
import java.util.Map;

import com.collibra.dgc.core.dao.filter.FilterVisitor;
import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.filters.ResourceFilter;
import com.collibra.dgc.core.dto.sorters.PropertyOrder;

/**
 * @author dieterwachters
 */
public class FilterVisitorHibernate implements FilterVisitor {
	private String entityPrefix;

	private Map<String, Object> hqlParameters = new HashMap<String, Object>();
	private boolean firstWhereClause = true;
	private boolean firstOrderClause = true;

	private StringBuffer hql = new StringBuffer();

	public FilterVisitorHibernate(String entityPrefix, StringBuffer hql) {
		this.entityPrefix = entityPrefix;
		this.hql = hql;
	}

	public Map<String, Object> getHqlParameters() {
		return hqlParameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.dao.filter.FilterVisitor#visit(com.collibra.dgc.core.dto.filters.ResourceFilter)
	 */
	@Override
	public void visit(ResourceFilter filter) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.dao.filter.FilterVisitor#visit(com.collibra.dgc.core.dto.filters.PropertyFilter)
	 */
	@Override
	public void visit(PropertyFilter propertyFilter) {
		if (!firstWhereClause)
			hql.append(" AND");
		else {
			firstWhereClause = false;
			hql.append(" WHERE");
		}
		hql.append(" upper(");
		if (entityPrefix != null && !entityPrefix.isEmpty()) {
			hql.append(entityPrefix).append(".");
		}
		hql.append(propertyFilter.getProperty());
		hql.append(") like :");
		hql.append(addParameter(getSQLMatchOperaterValue(propertyFilter)));
		hql.append(" ");
	}

	protected String getSQLMatchOperaterValue(PropertyFilter filter) {
		switch (filter.getFilterOperator()) {
		case INCLUDES:
			return "%" + filter.getValue().toUpperCase() + "%";
		case STARTS_WITH:
			return filter.getValue().toUpperCase() + "%";
		case EQUALS:
			return filter.getValue().toUpperCase();
		case ENDS_WITH:
			return "%" + filter.getValue().toUpperCase();
		}
		return "%" + filter.getValue().toUpperCase() + "%";
	}

	protected String addParameter(Object param) {
		int index = hqlParameters.size() + 1;
		String key = "param" + index;
		hqlParameters.put(key, param);
		return key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.dao.filter.FilterVisitor#visit(com.collibra.dgc.core.dto.sorters.PropertyOrder)
	 */
	@Override
	public void visit(PropertyOrder propertyOrder) {
		if (!firstOrderClause)
			hql.append(",");
		else {
			firstOrderClause = false;
			hql.append(" ORDER BY ");
		}

		if (entityPrefix != null && !entityPrefix.isEmpty()) {
			hql.append(entityPrefix).append(".");
		}
		hql.append(propertyOrder.getProperty());
		if (propertyOrder.sortDesc())
			hql.append(" DESC");
		else
			hql.append(" ASC");
	}
}
