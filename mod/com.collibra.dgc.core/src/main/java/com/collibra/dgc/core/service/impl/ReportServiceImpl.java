/**
 * 
 */
package com.collibra.dgc.core.service.impl;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dao.reporting.impl.ReportTermHibernateVisitor;
import com.collibra.dgc.core.dto.reporting.Report;
import com.collibra.dgc.core.dto.reporting.ReportConfig;
import com.collibra.dgc.core.service.ReportService;

/**
 * @author fvdmaele
 *
 */
@Service
public class ReportServiceImpl implements ReportService {

	private final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Report buildReport(ReportConfig config) {
		
		Report report = new Report();
		
		// fill report with term data
		ReportTermHibernateVisitor dao = new ReportTermHibernateVisitor(report, sessionFactory.getCurrentSession());
		config.accept(dao);
		
		return report;
	}
}
