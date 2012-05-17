package com.collibra.dgc.core.dao.reporting.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.collibra.dgc.core.dao.reporting.ReportConfigVisitor;

public class ReportSQLExportVisitor extends AbstractReportHibernateVisitor implements ReportConfigVisitor {

	private final Connection dbConn;
	private final String reportName;

	public ReportSQLExportVisitor(Session s, Connection connection, String reportName) {
		super(s);
		this.dbConn = connection;
		this.reportName = reportName;
	}

	@Override
	protected List<String> addTuplesToReport(String kind, List<Object[]> resultTuples) {

		List<String> foundTerms = new ArrayList<String>(resultTuples.size());

		List<String> hqlSelectIndexes = hqlSelectFieldIndexes.get(kind);
		int rIdIndex = hqlSelectIndexes.indexOf("term.id");
		Map<String, String> selectToReportHeader = hqlSelectToReportHeader.get(kind);

		try {
			StringBuilder insertSQL = new StringBuilder();
			insertSQL.append("INSERT INTO " + reportName + " (");
			boolean first = true;
			for (String hqlField : hqlSelectIndexes) {
				if (first) {
					first = false;
				} else {
					insertSQL.append(", ");
				}
				insertSQL.append(cleanColumnName(selectToReportHeader.get(hqlField)));
			}
			insertSQL.append(") VALUES (");
			first = true;
			for (String hqlField : hqlSelectIndexes) {
				if (first) {
					first = false;
				} else {
					insertSQL.append(", ");
				}
				insertSQL.append("?");
			}
			insertSQL.append(");");

			PreparedStatement statement = dbConn.prepareStatement(insertSQL.toString());

			for (Object tuple : resultTuples) {
				Object[] summary = (Object[]) tuple;
				String resourceId = (String) summary[rIdIndex];
				foundTerms.add(resourceId);

				int i = 1;
				for (String hqlField : hqlSelectIndexes) {
					statement.setString(i, (String) summary[hqlSelectIndexes.indexOf(hqlField)]);
					i = i + 1;
				}
				statement.executeUpdate();
			}

			dbConn.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return foundTerms;
	}

	@Override
	protected void addStringAttributeTuplesToReport(String parentId, List<Object[]> resultTuples) {
		int rIdIndex = 0;

		Map<String, PreparedStatement> attributeTypeStatementMap = new HashMap<String, PreparedStatement>();

		try {

			for (Object tuple : resultTuples) {
				Object[] summary = (Object[]) tuple;
				String resourceId = (String) summary[rIdIndex];

				String attrType = summary[1].toString();
				PreparedStatement stmt = attributeTypeStatementMap.get(attrType);

				if (stmt == null) {
					StringBuilder insertStringAttributeSQL = new StringBuilder();
					insertStringAttributeSQL.append("UPDATE " + reportName + " SET ");
					insertStringAttributeSQL.append(cleanColumnName(attributeTypeRIdNameMap.get(summary[1].toString()))
							+ " = ?, ");
					insertStringAttributeSQL.append(cleanColumnName(attributeTypeRIdNameMap.get(summary[1].toString()))
							+ "_rid = ? ");
					insertStringAttributeSQL.append("WHERE " + parentId + "_rid = ?;");
					stmt = dbConn.prepareStatement(insertStringAttributeSQL.toString());
					attributeTypeStatementMap.put(attrType, stmt);

				}

				stmt.setString(1, (String) summary[2]);
				stmt.setString(2, (String) summary[3]);
				stmt.setString(3, resourceId);
				stmt.execute();
			}
			dbConn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initializeReport(List<String> reportHeaderFields, long totalSize, int size) {
		try {
			Statement statement = dbConn.createStatement();

			StringBuilder createTableSQL = new StringBuilder();
			createTableSQL.append("CREATE TABLE " + reportName + " ( ");

			boolean first = true;
			for (String column : reportHeaderFields) {
				if (first)
					first = false;
				else
					createTableSQL.append(", ");
				createTableSQL.append(cleanColumnName(column));
				createTableSQL.append(" VARCHAR(250)");
			}

			createTableSQL.append(");");

			statement.executeUpdate(createTableSQL.toString());
			dbConn.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected String cleanColumnName(String column) {
		return column.toUpperCase().replaceAll("-", "");
	}
}
