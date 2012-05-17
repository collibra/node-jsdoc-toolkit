/**
 * 
 */
package com.collibra.dgc.core.component;

import com.collibra.dgc.core.dto.reporting.Report;
import com.collibra.dgc.core.dto.reporting.ReportConfig;
import com.collibra.dgc.core.dto.reporting.builder.ReportConfigJSONBuilder;

/**
 * @author fvdmaele
 *
 */
public interface ReportComponent {

	Report buildReport(ReportConfig config);
	
	String buildJSONReportConfig(ReportConfig config);
	
	ReportConfig buiderReportConfigFromJSON(String json);
}
