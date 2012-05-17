/**
 * 
 */
package com.collibra.dgc.core.dto.filters;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.collibra.dgc.core.dao.filter.FilterVisitor;
import com.collibra.dgc.core.dto.sorters.PropertyOrder;

/**
 * @author fvdmaele
 * 
 */
public class ResourceFilter {

	private final List<PropertyFilter> filters = new LinkedList<PropertyFilter>();
	private final List<PropertyOrder> orders = new LinkedList<PropertyOrder>();

	public ResourceFilter() {
	}

	public ResourceFilter(PropertyFilter filter, PropertyOrder order) {
		if (filter != null)
			filters.add(filter);

		if (order != null)
			orders.add(order);
	}

	public ResourceFilter(Collection<PropertyFilter> filters, Collection<PropertyOrder> orders) {
		if (filters != null)
			this.filters.addAll(filters);
		if (orders != null)
			this.orders.addAll(orders);
	}

	/**
	 * @return a {@link Collection} of {@link PropertyFilter}s on which this {@link Resource} must be filtered
	 */
	public Collection<PropertyFilter> getFilters() {
		return this.filters;
	}

	/**
	 * @return a {@link Collection} of {@link PropertyOrder}s by which the resulting {@link Resource}s must be ordered.
	 */
	public Collection<PropertyOrder> getOrders() {
		return this.orders;
	}

	/**
	 * @param filter the {@link PropertyFilter} to be added
	 */
	public void addFilter(PropertyFilter filter) {
		this.filters.add(filter);
	}

	/**
	 * @param order the {@link PropertyOrder} to be added
	 */
	public void addOrder(PropertyOrder order) {
		this.orders.add(order);
	}

	public void accept(FilterVisitor visitor) {
		visitor.visit(this);
		for (PropertyFilter pf : filters) {
			pf.accept(visitor);
		}
		for (PropertyOrder po : orders) {
			po.accept(visitor);
		}
	}
}
