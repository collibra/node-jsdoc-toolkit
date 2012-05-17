package com.collibra.dgc.rest.test.v1_0.reporting;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.collibra.dgc.core.dto.filters.PropertyFilter.FilterOperator;
import com.collibra.dgc.core.dto.reporting.ReportConfig;
import com.collibra.dgc.core.dto.reporting.StatusColumnConfig;
import com.collibra.dgc.core.dto.reporting.StringAttributeColumnConfig;
import com.collibra.dgc.core.dto.reporting.TermColumnConfig;
import com.collibra.dgc.core.dto.reporting.VocabularyColumnConfig;
import com.collibra.dgc.rest.test.v1_0.AbstractBootstrappedRestTest;

@Ignore
public class TestReportConfigJSONBuilder extends AbstractBootstrappedRestTest {

	private ReportConfig config;
	private ReportConfig config2;
	private TermColumnConfig tConfig;
	private TermColumnConfig tConfig2;
	private VocabularyColumnConfig vConfig;
	private StringAttributeColumnConfig saConfig;

	@Test
	public void testJSONBuilder() {
		tConfig = new TermColumnConfig("DNS", FilterOperator.INCLUDES, true);
		vConfig = new VocabularyColumnConfig(tConfig);
		saConfig = new StringAttributeColumnConfig(tConfig, attributeTypeComponent.getDefinitionAttributeType());
		StatusColumnConfig statusConfig = new StatusColumnConfig(tConfig);
		config = new ReportConfig(tConfig);

		String jsonConfig = reportComponent.buildJSONReportConfig(config);
		System.out.println(jsonConfig);

		config2 = reportComponent.buiderReportConfigFromJSON(jsonConfig);
		assertEquals(config2.getTermColumnConfig().getName(), config.getTermColumnConfig().getName());

		// assertEquals(saConfig, tConfig.getColumnConfigs().get(1));
	}
}
