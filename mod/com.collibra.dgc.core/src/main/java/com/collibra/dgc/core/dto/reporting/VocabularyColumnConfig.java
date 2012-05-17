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
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * @author fvdmaele
 * 
 */
public class VocabularyColumnConfig extends AbstractColumnConfig implements ColumnConfig {

	private final List<Vocabulary> vocabularies = new LinkedList<Vocabulary>();

	public VocabularyColumnConfig(TermColumnConfig parent) {
		super(parent);
	}

	public VocabularyColumnConfig(TermColumnConfig parent, List<Vocabulary> vocabularies) {
		super(parent);
		if (vocabularies != null)
			for (Vocabulary voc : vocabularies)
				this.vocabularies.add(voc);
	}

	public VocabularyColumnConfig(TermColumnConfig parent, String filterValue, FilterOperator filterOp, Boolean sortASC) {
		super(parent, filterValue, filterOp, sortASC);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.collibra.dgc.core.dto.reporting.ColumnConfig#accept(com.collibra.dgc.core.dao.reporting.ReportConfigVisitor)
	 */
	@Override
	public void accept(ReportConfigVisitor visitor) {
		visitor.visit(this);
	}

	public List<Vocabulary> getVocabularies() {
		return this.vocabularies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.collibra.dgc.core.dto.reporting.ColumnConfig#getId()
	 */
	@Override
	public String getId() {
		return parent.getName() + "_voc";
	}

	@Override
	protected void addPropertyFilter(String filterValue, FilterOperator filterOp) {
		this.propertyFilter = new PropertyFilter("name", filterOp, filterValue);
	}

	@Override
	protected void addPropertyOrder(Boolean sortDesc) {
		this.propertyOrder = new PropertyOrder("name", sortDesc);
	}

	@Override
	public String getColumnType() {
		return "vocabulary";
	}

}
