/**
 * 
 */
package com.collibra.dgc.core.component.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collibra.dgc.core.component.ReportComponent;
import com.collibra.dgc.core.dto.reporting.Report;
import com.collibra.dgc.core.dto.reporting.ReportConfig;
import com.collibra.dgc.core.dto.reporting.builder.ReportConfigJSONBuilder;
import com.collibra.dgc.core.service.ReportService;
import com.collibra.dgc.core.service.RepresentationService;

/**
 * @author fvdmaele
 *
 */
@Component
public class ReportComponentImpl implements ReportComponent {

	@Autowired
	ReportService reportService;
	
	@Autowired
	RepresentationService representationService;
	
	@Autowired
	ReportConfigJSONBuilder jsonConfigBuilder;
	
	@Override
	public Report buildReport(ReportConfig config) {
		return reportService.buildReport(config);
	}
	
	public ReportConfig buiderReportConfigFromJSON(String json) {
		return jsonConfigBuilder.fromJSON(json);
	}
	
	public String buildJSONReportConfig(ReportConfig config) {
		return jsonConfigBuilder.toJSON(config);
	}
}