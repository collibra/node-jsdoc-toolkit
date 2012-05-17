/**
 * 
 */
package com.collibra.dgc.core.dao.reporting.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.collibra.dgc.core.dao.reporting.ReportConfigVisitor;
import com.collibra.dgc.core.dto.reporting.Report;

/**
 * @author fvdmaele
 * 
 */
public class ReportTermHibernateVisitor extends AbstractReportHibernateVisitor implements ReportConfigVisitor {

	private final Report report;

	public ReportTermHibernateVisitor(Report report, Session s) {
		super(s);
		this.report = report;
	}

	@Override
	protected List<String> addTuplesToReport(String kind, List<Object[]> resultTuples) {
		List<String> foundTerms = new ArrayList<String>(resultTuples.size());
		List<String> hqlSelectIndexes = hqlSelectFieldIndexes.get(kind);
		int rIdIndex = hqlSelectIndexes.indexOf("term.id");
		Map<String, String> selectToReportHeader = hqlSelectToReportHeader.get(kind);

		for (Object tuple : resultTuples) {
			Object[] summary = (Object[]) tuple;
			String resourceId = (String) summary[rIdIndex];
			foundTerms.add(resourceId);

			for (String hqlField : hqlSelectIndexes) {
				report.addEntry(resourceId.toString(), selectToReportHeader.get(hqlField),
						summary[hqlSelectIndexes.indexOf(hqlField)]);
			}
		}
		return foundTerms;
	}

	@Override
	protected void addStringAttributeTuplesToReport(String parentId, List<Object[]> resultTuples) {
		int rIdIndex = 0;

		for (Object tuple : resultTuples) {
			Object[] summary = (Object[]) tuple;
			String resourceId = (String) summary[rIdIndex];
			report.addEntry(resourceId.toString(), attributeTypeRIdNameMap.get(summary[1].toString()), summary[2]);
			report.addEntry(resourceId.toString(), attributeTypeRIdNameMap.get(summary[1].toString()) + "_rid",
					summary[3]);
		}
	}

	@Override
	protected void initializeReport(List<String> reportHeaderFields, long totalSize, int size) {
		report.setTotalSize(totalSize);
		report.initializeMatrix(reportHeaderFields, size);
	}
}
