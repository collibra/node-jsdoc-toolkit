/**
 * 
 */
package com.collibra.dgc.core.dao.reporting;

import com.collibra.dgc.core.dto.reporting.ReportConfig;
import com.collibra.dgc.core.dto.reporting.SingleValueListAttributeColumnConfig;
import com.collibra.dgc.core.dto.reporting.StatusColumnConfig;
import com.collibra.dgc.core.dto.reporting.StringAttributeColumnConfig;
import com.collibra.dgc.core.dto.reporting.TermColumnConfig;
import com.collibra.dgc.core.dto.reporting.VocabularyColumnConfig;

/**
 * @author fvdmaele
 *
 */
public interface ReportConfigVisitor {

	void visit(ReportConfig config);
	
	void visit(TermColumnConfig config);
	
	void visit(StatusColumnConfig config);
	
	void visit(VocabularyColumnConfig config);
	
	void visit(StringAttributeColumnConfig config);
	
	void visit(SingleValueListAttributeColumnConfig config);
}
