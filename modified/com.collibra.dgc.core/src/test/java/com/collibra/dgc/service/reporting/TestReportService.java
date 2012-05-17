package com.collibra.dgc.service.reporting;

import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import au.com.bytecode.opencsv.CSVReader;

import com.collibra.dgc.core.dto.filters.PropertyFilter;
import com.collibra.dgc.core.dto.filters.PropertyFilter.FilterOperator;
import com.collibra.dgc.core.dto.reporting.ColumnConfig;
import com.collibra.dgc.core.dto.reporting.Report;
import com.collibra.dgc.core.dto.reporting.ReportConfig;
import com.collibra.dgc.core.dto.reporting.StatusColumnConfig;
import com.collibra.dgc.core.dto.reporting.StringAttributeColumnConfig;
import com.collibra.dgc.core.dto.reporting.TermColumnConfig;
import com.collibra.dgc.core.dto.reporting.VocabularyColumnConfig;
import com.collibra.dgc.core.model.representation.Term;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.service.AbstractServiceTest;

/**
 * @author fvdmaele
 * 
 */
@ContextConfiguration("/com/collibra/dgc/applicationContext-test.xml")
public class TestReportService extends AbstractServiceTest {

	private static String netappcsv = "src/test/resources/import/NetAppCorporateDictionary.csv";

	Vocabulary voc;
	long start;
	long duration;
	Report report;
	ReportConfig config;
	Term sourceLabel;

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
	public void testTermReport() throws Exception {
		VocabularyColumnConfig vConfig;

		setUpNetApp();

		TermColumnConfig tConfig = new TermColumnConfig();
		config = new ReportConfig(tConfig);
		report = getReport(config, "all terms");

		tConfig = new TermColumnConfig();
		config = new ReportConfig(tConfig);
		config.setDisplayLength(30);
		report = getReport(config, "all terms, from 0 to 30");
		assertEquals(30, report.size());
		for (Entry<String, LinkedList[]> entry : report.getEntries())
			printEntry(entry);

		tConfig = new TermColumnConfig();
		config = new ReportConfig(tConfig);
		config.setDisplayLength(50);
		config.setDisplayStart(100);
		report = getReport(config, "all terms, from 100 to 150");
		assertEquals(50, report.size());
		for (Entry<String, LinkedList[]> entry : report.getEntries())
			printEntry(entry);
		
		tConfig = new TermColumnConfig("DNS", FilterOperator.INCLUDES, true);
		config = new ReportConfig(tConfig);
		report = getReport(config, "All terms filtered and sorted");
		System.out.println(report.getHeader());
		for (Entry<String, LinkedList[]> entry : report.getEntries())
			printEntry(entry);

		tConfig = new TermColumnConfig("DNS", FilterOperator.INCLUDES, true);
		vConfig = new VocabularyColumnConfig(tConfig);
		config = new ReportConfig(tConfig);
		report = getReport(config, "All terms with vocabulary");
		System.out.println(report.getHeader());
		for (Entry<String, LinkedList[]> entry : report.getEntries())
			printEntry(entry);
	}

	@Test
	public void testTermReportWithVocabulary() throws Exception {
		VocabularyColumnConfig vConfig;
		TermColumnConfig tConfig;

		setUpNetApp();

		tConfig = new TermColumnConfig();
		vConfig = new VocabularyColumnConfig(tConfig);
		config = new ReportConfig(tConfig);
		report = getReport(config, "All terms with vocabulary");

		tConfig = new TermColumnConfig();
		vConfig = new VocabularyColumnConfig(tConfig, "My Voc", FilterOperator.EQUALS, false);
		config = new ReportConfig(tConfig);
		report = getReport(config, "All terms with vocabulary");

		for (Entry<String, LinkedList[]> entry : report.getEntries())
			printEntry(entry);
	}

	@Test
	public void testTermReportWithStringAttributes() throws Exception {
		VocabularyColumnConfig vConfig;
		TermColumnConfig tConfig;

		setUpNetApp();

		sourceLabel = representationService.findTermByResourceId(sourceLabel.getId());

		tConfig = new TermColumnConfig();
		StringAttributeColumnConfig aConfig = new StringAttributeColumnConfig(tConfig, sourceLabel);
		config = new ReportConfig(tConfig);
		report = getReport(config, "All terms with source string attribute");

//		for (Entry<String, LinkedList[]> entry : report.getEntries())
//			printEntry(entry);

		tConfig = new TermColumnConfig();
		tConfig.setPropertyFilter(new PropertyFilter("", FilterOperator.INCLUDES, "disk"));
		ColumnConfig sConfig = new StatusColumnConfig(tConfig);
		aConfig = new StringAttributeColumnConfig(tConfig, sourceLabel);
		StringAttributeColumnConfig defConfig = new StringAttributeColumnConfig(tConfig,
				attributeService.findMetaDefinition());
		vConfig = new VocabularyColumnConfig(tConfig, "My Voc", FilterOperator.EQUALS, false);
		config = new ReportConfig(tConfig);
		report = getReport(config, "Voc 'My Voc' terms with source, definition, status, filtered terms 'INCLUDES' disk");

		for (Entry<String, LinkedList[]> entry : report.getEntries())
			printEntry(entry);

		tConfig = new TermColumnConfig();
		tConfig.setSortDESC(false);
		aConfig = new StringAttributeColumnConfig(tConfig, sourceLabel);
		defConfig = new StringAttributeColumnConfig(tConfig, attributeService.findMetaDefinition(), "facility",
				FilterOperator.INCLUDES);
		vConfig = new VocabularyColumnConfig(tConfig, "My Voc", FilterOperator.EQUALS, false);
		config = new ReportConfig(tConfig);
		report = getReport(config, "Voc 'My Voc' terms with source, definition, description");

//		for (Entry<String, LinkedList[]> entry : report.getEntries())
//			printEntry(entry);

		// tConfig = new TermColumnConfig();
		// vConfig = new VocabularyColumnConfig(tConfig, "My Voc", FilterOperator.EQUALS, false);
		// config = new ReportConfig(tConfig);
		// report = getReport(config, "All terms with vocabulary");
		//
		// for(Entry<String, LinkedList[]> entry : report.getEntries())
		// printEntry(entry);
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
}
