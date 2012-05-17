package com.collibra.dgc.service.reporting;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import au.com.bytecode.opencsv.CSVReader;

import com.collibra.dgc.core.dao.reporting.impl.ReportSQLExportVisitor;
import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.filters.PropertyFilter.FilterOperator;
import com.collibra.dgc.core.dto.reporting.ColumnConfig;
import com.collibra.dgc.core.dto.reporting.Report;
import com.collibra.dgc.core.dto.reporting.ReportConfig;
import com.collibra.dgc.core.dto.reporting.StatusColumnConfig;
import com.collibra.dgc.core.dto.reporting.StringAttributeColumnConfig;
import com.collibra.dgc.core.dto.reporting.TermColumnConfig;
import com.collibra.dgc.core.dto.reporting.VocabularyColumnConfig;
import com.collibra.dgc.core.dto.reporting.builder.ReportConfigJSONBuilder;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * @author fvdmaele
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestReportExport extends AbstractServiceTest {

	@Autowired
	ReportConfigJSONBuilder jsonConfigBuilder;

	@Autowired
	private ConfigurationService configurationService;
	
	private static String netappcsv = "src/test/resources/import/NetAppCorporateDictionary.csv";

	Vocabulary voc;
	long start;
	long duration;
	Report report;
	ReportConfig config;
	Term sourceLabel;

	Connection connection;
	Statement statement;
	String database = "reporttest";

	@Autowired
	SessionFactory sessionFactory;

	public void setUpNetApp() throws IOException {

		voc = createSampleVocabulary();

		sourceLabel = attributeService.createStringAttributeType("source", "This is netapp source attribute");

		CSVReader reader = new CSVReader(new FileReader(netappcsv), ';');
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			String label = nextLine[0];
			String def = nextLine[1];
			String steward = nextLine[2];
			String source = nextLine[3];
			Term term = voc.getTerm(label);
			if (term == null) {
				term = representationFactory.makeTerm(voc, label);
			}
			representationFactory.makeStringAttribute(attributeService.findMetaDefinition(), term, def);
			// avoid constraint violation exception
			if (term.getAttributes().size() < 3) {
				representationFactory.makeStringAttribute(sourceLabel, term, source);
			}
		}

		representationService.saveVocabulary(voc);

		resetTransaction();

		voc = representationService.findVocabularyByResourceId(voc.getId());
	}

	@Test
	public void testHSQLDBExport() throws Exception {

		try {
			VocabularyColumnConfig vConfig;

			// setUpNetApp();
			//
			TermColumnConfig tConfig = new TermColumnConfig();

			// set-up database connection
			final String driver = configurationService.getString("core/datasource/driver");
			final String dbURL = configurationService.getString("core/datasource/url");
			final String dbUser = configurationService.getString("core/datasource/username");
			final String dbPass = configurationService.getString("core/datasource/password");
			
			Class.forName(driver);
			connection = DriverManager.getConnection(dbURL, dbUser, dbPass);
			connection.setAutoCommit(false);

			// export to HSQLDB
			String table = database + "." + database + "_test";
			config = new ReportConfig(tConfig);
			ReportSQLExportVisitor exporter = new ReportSQLExportVisitor(sessionFactory.getCurrentSession(),
					connection, table.toUpperCase());
			config.accept(exporter);
			cleanup(connection, table.toUpperCase());

			// test with netapp data
			setUpNetApp();

			sourceLabel = representationService.findTermByResourceId(sourceLabel.getId());

			tConfig = new TermColumnConfig();
			tConfig.setPropertyFilter(new PropertyFilter("", FilterOperator.INCLUDES, "disk"));
			ColumnConfig sConfig = new StatusColumnConfig(tConfig);
			StringAttributeColumnConfig aConfig = new StringAttributeColumnConfig(tConfig, sourceLabel);
			StringAttributeColumnConfig defConfig = new StringAttributeColumnConfig(tConfig,
					attributeService.findMetaDefinition());
			vConfig = new VocabularyColumnConfig(tConfig, "My Voc", FilterOperator.EQUALS, false);
			config = new ReportConfig(tConfig);
			report = getReport(config,
					"Voc 'My Voc' terms with source, definition, status, filtered terms 'INCLUDES' disk");

			System.out.println(report.getHeader());
			for (Entry<String, LinkedList[]> entry : report.getEntries())
				printEntry(entry);

			table = database + "." + database + "_test2";
			System.out.println("Config >> " + jsonConfigBuilder.toJSON(config));
			exporter = new ReportSQLExportVisitor(sessionFactory.getCurrentSession(), connection, table.toUpperCase());
			config.accept(exporter);

			cleanup(connection, table.toUpperCase());

		} finally {
			if (connection != null) {
				connection.close();
			}
		}

	}

	protected Report getReport(ReportConfig config, String description) {
		start = System.currentTimeMillis();
		Report result = reportService.buildReport(config);
		duration = (System.currentTimeMillis() - start);
		System.out.println("[" + description + "] " + duration + " (ms)");
		return result;
	}

	protected void printEntry(Entry<String, LinkedList[]> entry) {
		System.out.print(entry.getKey() + " > ");
		for (int i = 0; i < entry.getValue().length; i++)
			System.out.print(entry.getValue()[i] + " | ");
		System.out.println("");
	}

	protected void cleanup(Connection dbConn, String table) {

		try {
			Statement statement = dbConn.createStatement();

			statement.execute("DROP TABLE IF EXISTS " + table.toUpperCase() + " CASCADE;");
			connection.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
