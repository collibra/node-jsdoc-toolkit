/**
 * 
 */
package com.collibra.dgc.core.dao.reporting.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.Session;

import com.collibra.dgc.core.dao.reporting.ReportConfigVisitor;
import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.reporting.ColumnConfig;
import com.collibra.dgc.core.dto.reporting.ReportConfig;
import com.collibra.dgc.core.dto.reporting.SingleValueListAttributeColumnConfig;
import com.collibra.dgc.core.dto.reporting.StatusColumnConfig;
import com.collibra.dgc.core.dto.reporting.StringAttributeColumnConfig;
import com.collibra.dgc.core.dto.reporting.TermColumnConfig;
import com.collibra.dgc.core.dto.reporting.VocabularyColumnConfig;
import com.collibra.dgc.core.model.representation.Term;

/**
 * @author fvdmaele
 * 
 */
public abstract class AbstractReportHibernateVisitor implements ReportConfigVisitor {

	private final Session session;

	protected Map<String, Map<String, String>> hqlSelectToReportHeader;
	protected StringBuilder termSelectHQL;
	protected StringBuilder termWhereHQL;
	private StringBuilder stringAttributeHQL;
	private StringBuilder singleValueListAttributeHQL;
	protected Map<String, List<String>> hqlSelectFieldIndexes;
	private Map<String, Boolean> sorters;
	private Map<String, Map<String, Object>> hqlParameters;
	private Map<String, Map<String, Collection>> hqlListParameters;
	private List<String> reportHeaderFields;
	private boolean firstWhereClause = true;
	private boolean handledStringAttributes = false;
	private boolean handledSingleValueListAttributes = false;
	private List<Term> stringAttributeLabels;
	private List<Term> singleValueListAttributeLabels;
	protected HashMap<String, String> attributeTypeRIdNameMap;
	private int displayStart = 0;
	private int displayLength = -1;
	private ReportConfig reportConfig;

	public AbstractReportHibernateVisitor(Session s) {
		this.session = s;
	}

	@Override
	public void visit(ReportConfig config) {
		initialize(config);
		reportHeaderFields = config.getFields();
		this.displayStart = config.getDisplayStart();
		this.displayLength = config.getDisplayLength();
	}

	@Override
	public void visit(TermColumnConfig config) {

		StringBuilder countHql = new StringBuilder();

		StringBuilder hql = new StringBuilder();
		// handle term resource id
		addField("term", config.getResourceIdField(), "term.id");
		// handle term signifier
		addField("term", config.getContentField(), "term.signifier");

		// add ordering
		buildOrder("term.signifier", config);

		// build select hql
		hql.append("select ");
		boolean first = true;
		for (String hqlIndex : hqlSelectFieldIndexes.get("term")) {
			if (first)
				first = false;
			else
				hql.append(", ");
			hql.append(hqlIndex);
		}

		// add rest of SELECT hql
		hql.append(" from TermImpl as term ");
		hql.append(termSelectHQL);

		countHql.append("select count(*) from TermImpl as term ");
		countHql.append(termSelectHQL);

		// build filter hql
		buildFilter("term.signifier", config, termWhereHQL);

		// add rest of WHERE hql
		hql.append(termWhereHQL);
		countHql.append(termWhereHQL);

		// build sorting hql
		hql.append(" order by ");
		boolean firstSort = true;
		if (sorters.isEmpty()) {
			boolean sortTermsDesc = false;
			if (config.isOrdered())
				sortTermsDesc = config.getPropertyOrder().sortDesc();
			buildSorting("term.signifier", sortTermsDesc, hql, true);
			firstSort = false;
		} else {
			for (Entry<String, Boolean> entry : sorters.entrySet()) {
				buildSorting(entry.getKey(), entry.getValue(), hql, firstSort);
				if (firstSort)
					firstSort = false;
			}
		}

		// make the Query
		Query query = session.createQuery(hql.toString());

		// fill parameters
		fillParameters("term", query);

		Long totalSize = 0l;

		// set paging on query
		if (displayLength > 0) {
			query.setFirstResult(displayStart);
			query.setMaxResults(displayLength);

			// get the total size (without paging)
			Query sizeQuery = session.createQuery(countHql.toString());
			// fill parameters
			fillParameters("term", sizeQuery);
			totalSize = (Long) sizeQuery.uniqueResult();
		}

		// get the result
		List<Object[]> resultTuples = query.list();

		// initialize report
		initializeReport(reportHeaderFields, totalSize, resultTuples.size());

		// fill report
		List<String> foundTerms = addTuplesToReport("term", resultTuples);

		// handle string attributes
		if (handledStringAttributes && !foundTerms.isEmpty()) {
			stringAttributeHQL.append(" AND term.id in (:" + addListParameter("stringattribute", foundTerms) + ")");
			query = session.createQuery(stringAttributeHQL.toString());
			fillParameters("stringattribute", query);
			resultTuples = query.list();
			addStringAttributeTuplesToReport(config.getName(), resultTuples);
		}

		// handle single value list attributes
		if (handledSingleValueListAttributes && !foundTerms.isEmpty()) {
			singleValueListAttributeHQL.append(" AND term.id in (:"
					+ addListParameter("singlevaluelistattribute", foundTerms) + ")");
			query = session.createQuery(singleValueListAttributeHQL.toString());
			fillParameters("singlevaluelistattribute", query);
			resultTuples = query.list();
			addStringAttributeTuplesToReport(config.getName(), resultTuples);
		}
	}

	@Override
	public void visit(VocabularyColumnConfig config) {
		// add fields
		addField("term", config.getResourceIdField(), "voc.id");
		addField("term", config.getContentField(), "voc.name");

		// add SELECT hql
		termSelectHQL.append(" inner join term.vocabulary as voc");

		// add WHERE hql
		buildFilter("voc.name", config, termWhereHQL);

		// add ordering
		buildOrder("voc.name", config);

		// add vocabulary resource list
		if (!config.getVocabularies().isEmpty())
			termWhereHQL.append(" and voc in (:" + addListParameter("term", config.getVocabularies()) + ")");
	}

	@Override
	public void visit(StatusColumnConfig config) {
		// add fields
		addField("term", config.getResourceIdField(), "stat.id");
		addField("term", config.getContentField(), "stat.signifier");

		// add SELECT hql
		termSelectHQL.append(" left join term.statusRepresentation as stat");

		// add WHERE hql
		buildFilter("stat.signifier", config, termWhereHQL);

		// add ordering
		buildOrder("stat.signifier", config);

		// add status list
		if (!config.getStatusses().isEmpty()) {
			termWhereHQL.append(" and stat in (:" + addListParameter("term", config.getStatusses()) + ")");
		}
	}

	@Override
	public void visit(StringAttributeColumnConfig config) {
		String attrName = getHQLConfigID(config);

		// if we need to filter or sort on the attribute, we need to include it in the term query
		if (config.isFiltered() || config.isOrdered()) {
			// add attribute to term SELECT clause
			if (config.isFiltered())
				termSelectHQL.append(" inner join term.attributes as " + attrName);
			else
				termSelectHQL.append(" left join term.attributes as " + attrName);

			if (firstWhereClause) {
				termWhereHQL.append(" WHERE ");
				firstWhereClause = false;
			} else {
				termWhereHQL.append(" AND ");
			}
			termWhereHQL.append(attrName + ".label = :" + addParameter("term", config.getAttributeLabel()));
			if (config.isFiltered())
				termWhereHQL.append(" AND upper(" + attrName + ".value) like :"
						+ addParameter("term", getSQLMatchOperaterValue(config)) + " ");
		}

		// add ordering
		buildOrder(attrName + ".value", config);

		// actually return the attribute content
		// if this is the first string attribute to handle, we create the initial HQL
		// else, we just add the attribute label to the label list parameter
		if (!handledStringAttributes) {
			stringAttributeHQL
					.append("SELECT term.id, stringattribute.label.signifier, stringattribute.longExpression, stringattribute.id ");
			stringAttributeHQL.append("from TermImpl as term ");
			stringAttributeHQL.append("inner join term.attributes as stringattribute ");

			stringAttributeHQL.append("WHERE stringattribute.class = StringAttributeImpl ");
			stringAttributeHQL.append("AND stringattribute.label in (:"
					+ addListParameter("stringattribute", stringAttributeLabels) + ") ");

			handledStringAttributes = true;
		}

		// add string attribute label to list of string attribute labels
		stringAttributeLabels.add(config.getAttributeLabel());
		// map the attribute label rid to its attribute config column name
		attributeTypeRIdNameMap.put(config.getAttributeLabel().getSignifier(), config.getName());
	}

	@Override
	public void visit(SingleValueListAttributeColumnConfig config) {
		String attrName = getHQLConfigID(config);

		// if we need to filter or sort on the attribute, we need to include it in the term query
		if (config.isFiltered() || config.isOrdered()) {
			// add attribute to term SELECT clause
			if (config.isFiltered())
				termSelectHQL.append(" inner join term.attributes as " + attrName);
			else
				termSelectHQL.append(" left join term.attributes as " + attrName);

			if (firstWhereClause) {
				termWhereHQL.append(" WHERE ");
				firstWhereClause = false;
			} else {
				termWhereHQL.append(" AND ");
			}
			termWhereHQL.append(attrName + ".label = :" + addParameter("term", config.getAttributeLabel()));
			if (config.isFiltered())
				termWhereHQL.append(" AND upper(" + attrName + ".value) like :"
						+ addParameter("term", getSQLMatchOperaterValue(config)) + " ");
		}

		// add ordering
		buildOrder(attrName + ".value", config);

		// actually return the attribute content
		// if this is the first single value list attribute to handle, we create the initial HQL
		// else, we just add the attribute label to the label list parameter
		if (!handledSingleValueListAttributes) {
			singleValueListAttributeHQL
					.append("SELECT term.id, singlevaluelistattribute.label.signifier, singlevaluelistattribute.longExpression, singlevaluelistattribute.id ");
			singleValueListAttributeHQL.append("from TermImpl as term ");
			singleValueListAttributeHQL.append("inner join term.attributes as singlevaluelistattribute ");

			singleValueListAttributeHQL.append("WHERE singlevaluelistattribute.class = SingleValueListAttributeImpl ");
			singleValueListAttributeHQL.append("AND singlevaluelistattribute.label in (:"
					+ addListParameter("singlevaluelistattribute", singleValueListAttributeLabels) + ") ");

			handledSingleValueListAttributes = true;
		}

		// add string attribute label to list of string attribute labels
		singleValueListAttributeLabels.add(config.getAttributeLabel());
		// map the attribute label rid to its attribute config column name
		attributeTypeRIdNameMap.put(config.getAttributeLabel().getSignifier(), config.getName());
	}

	public ReportConfig getReportConfig() {
		return reportConfig;
	}

	/** Helper methods */

	protected abstract List<String> addTuplesToReport(String kind, List<Object[]> resultTuples);

	protected abstract void addStringAttributeTuplesToReport(String parentId, List<Object[]> resultTuples);

	protected abstract void initializeReport(List<String> reportHeaderFields, long totalSize, int size);

	protected String getSQLMatchOperaterValue(ColumnConfig config) {
		PropertyFilter filter = config.getPropertyFilter();
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

	protected void buildFilter(String hqlSelectField, ColumnConfig config, StringBuilder hql) {
		if (config.isFiltered()) {
			if (firstWhereClause) {
				firstWhereClause = false;
				hql.append(" WHERE");
			} else {
				hql.append(" AND");
			}
			hql.append(" upper(");
			hql.append(hqlSelectField);
			hql.append(") like :");
			hql.append(addParameter("term", getSQLMatchOperaterValue(config)));
			hql.append(" ");
		}
	}

	protected void buildOrder(String hqlSelectField, ColumnConfig config) {
		if (config.isOrdered()) {
			sorters.put(hqlSelectField, config.getPropertyOrder().sortDesc());
		}
	}

	protected void buildSorting(String hqlSelectField, Boolean sortDesc, StringBuilder hql, boolean first) {
		if (!first)
			hql.append(", ");
		hql.append(hqlSelectField);
		if (sortDesc)
			hql.append(" DESC");
		else
			hql.append(" ASC");
	}

	protected void initialize(ReportConfig rConfig) {
		hqlSelectToReportHeader = new HashMap<String, Map<String, String>>();
		hqlSelectFieldIndexes = new HashMap<String, List<String>>();
		termSelectHQL = new StringBuilder();
		termWhereHQL = new StringBuilder();
		stringAttributeHQL = new StringBuilder();
		sorters = new HashMap<String, Boolean>();
		hqlParameters = new HashMap<String, Map<String, Object>>();
		hqlListParameters = new HashMap<String, Map<String, Collection>>();
		stringAttributeLabels = new LinkedList<Term>();
		singleValueListAttributeLabels = new LinkedList<Term>();
		attributeTypeRIdNameMap = new HashMap<String, String>();
		reportConfig = rConfig;
	}

	protected void addField(String kind, String field, String hqlSelectField) {
		Map<String, String> selectToReportHeader = hqlSelectToReportHeader.get(kind);
		List<String> hqlSelectIndex = hqlSelectFieldIndexes.get(kind);
		if (hqlSelectIndex == null || selectToReportHeader == null) {
			selectToReportHeader = new HashMap<String, String>();
			hqlSelectToReportHeader.put(kind, selectToReportHeader);
			hqlSelectIndex = new ArrayList<String>();
			hqlSelectFieldIndexes.put(kind, hqlSelectIndex);
		}
		selectToReportHeader.put(hqlSelectField, field);
		hqlSelectIndex.add(hqlSelectField);
	}

	protected int findSelectFieldIndex(String kind, String sqlSelectField) {
		return hqlSelectFieldIndexes.get(kind).indexOf(sqlSelectField);
	}

	protected String getSQLSelectField(String kind, String field) {
		return hqlSelectToReportHeader.get(kind).get(field);
	}

	protected String addParameter(String type, Object param) {
		Map<String, Object> typeMap = hqlParameters.get(type);
		if (typeMap == null) {
			typeMap = new HashMap<String, Object>();
			hqlParameters.put(type, typeMap);
		}
		int index = typeMap.size() + 1;
		String key = type + index;
		typeMap.put(key, param);
		return key;
	}

	protected String addListParameter(String type, Collection param) {
		Map<String, Collection> typeMap = hqlListParameters.get(type);
		if (typeMap == null) {
			typeMap = new HashMap<String, Collection>();
			hqlListParameters.put(type, typeMap);
		}
		int index = typeMap.size() + 1;
		String key = type + index;
		typeMap.put(key, param);
		return key;
	}

	protected void fillParameters(String kind, Query q) {
		if (hqlParameters.containsKey(kind))
			for (Entry<String, Object> paramEntry : hqlParameters.get(kind).entrySet())
				q.setParameter(paramEntry.getKey(), paramEntry.getValue());

		if (hqlListParameters.containsKey(kind))
			for (Entry<String, Collection> paramEntry : hqlListParameters.get(kind).entrySet())
				q.setParameterList(paramEntry.getKey(), paramEntry.getValue());
	}

	protected String getHQLConfigID(ColumnConfig config) {
		return config.getName().replaceAll("_", "").replaceAll("-", "");
	}
}
