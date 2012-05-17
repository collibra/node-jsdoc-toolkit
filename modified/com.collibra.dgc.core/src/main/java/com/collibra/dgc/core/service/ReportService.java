package com.collibra.dgc.core.service;

import com.collibra.dgc.core.dto.reporting.Report;
import com.collibra.dgc.core.dto.reporting.ReportConfig;

public interface ReportService {

	Report buildReport(ReportConfig config);

}
