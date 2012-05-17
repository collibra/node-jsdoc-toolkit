package com.collibra.dgc.core.dto.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.impl.util.json.JSONObject;

import com.collibra.dgc.core.dao.reporting.ReportConfigVisitor;

/**
 * @author fvdmaele
 *
 */
public class ReportConfig {
	
	TermColumnConfig dConfig;
	HashMap<String, JSONObject> jsonMap;
	int displayStart = 0;
	int displayLength = -1;
	List<String> viewOrder = new LinkedList();
	
	public ReportConfig(final TermColumnConfig dConfig) {
		this.dConfig = dConfig;
	}
	
	public TermColumnConfig getTermColumnConfig() {
		return dConfig;
	}
	
	public void accept(ReportConfigVisitor visitor) {
		visitor.visit(this);
		
		dConfig.accept(visitor);
	}
	
	public void setDisplayStart(int i) {
		this.displayStart = i;
	}
	
	public int getDisplayStart() {
		return this.displayStart;
	}
	
	public void setDisplayLength(int i) {
		this.displayLength = i;
	}
	
	public int getDisplayLength() {
		return this.displayLength;
	}
	
	public void setColumnViewOrder(List<String> columnNames) {
		this.viewOrder = columnNames;
	}
	
	public List<String> getColumnViewOrder() {
		if(!viewOrder.isEmpty())
			return viewOrder;
		
		return getColumnNames();
	}
	
	public List<String> getContentFields() {
		final List<String> result = new ArrayList<String>();
		accept(new ReportConfigVisitor() {

			@Override
			public void visit(ReportConfig config) {
			}

			@Override
			public void visit(TermColumnConfig config) {
				result.add(0, config.getContentField());
			}

			@Override
			public void visit(VocabularyColumnConfig config) {
				result.add(config.getContentField());
			}
			
			@Override
			public void visit(StringAttributeColumnConfig config) {
				result.add(config.getContentField());
			}

			@Override
			public void visit(StatusColumnConfig config) {
				result.add(config.getContentField());
			}

			@Override
			public void visit(SingleValueListAttributeColumnConfig config) {
				result.add(config.getContentField());
			}
		});
		return result;
	}
	
	public List<String> getResourceIdFields() {
		final List<String> result = new ArrayList<String>();
		accept(new ReportConfigVisitor() {

			@Override
			public void visit(ReportConfig config) {
			}

			@Override
			public void visit(TermColumnConfig config) {
				result.add(0, config.getResourceIdField());
			}

			@Override
			public void visit(VocabularyColumnConfig config) {
				result.add(config.getResourceIdField());
			}
			
			@Override
			public void visit(StringAttributeColumnConfig config) {
				result.add(config.getResourceIdField());
			}

			@Override
			public void visit(StatusColumnConfig config) {
				result.add(config.getResourceIdField());
			}

			@Override
			public void visit(SingleValueListAttributeColumnConfig config) {
				result.add(config.getResourceIdField());
			}
		});
		return result;
	}
	
	public List<String> getFields() {
		final List<String> result = new ArrayList<String>();
		accept(new ReportConfigVisitor() {

			@Override
			public void visit(ReportConfig config) {
			}

			@Override
			public void visit(TermColumnConfig config) {
				result.add(0, config.getResourceIdField());
				result.add(1, config.getContentField());
			}

			@Override
			public void visit(VocabularyColumnConfig config) {
				result.add(config.getContentField());
				result.add(config.getResourceIdField());
			}

			@Override
			public void visit(StringAttributeColumnConfig config) {
				result.add(config.getContentField());
				result.add(config.getResourceIdField());
			}

			@Override
			public void visit(StatusColumnConfig config) {
				result.add(config.getContentField());
				result.add(config.getResourceIdField());
			}

			@Override
			public void visit(SingleValueListAttributeColumnConfig config) {
				result.add(config.getContentField());
				result.add(config.getResourceIdField());
			}
		});
		return result;
	}
	
	public List<String> getColumnNames() {
		final List<String> result = new ArrayList<String>();
		accept(new ReportConfigVisitor() {

			@Override
			public void visit(ReportConfig config) {
			}

			@Override
			public void visit(TermColumnConfig config) {
				result.add(0, config.getName());
			}

			@Override
			public void visit(VocabularyColumnConfig config) {
				result.add(config.getName());
			}

			@Override
			public void visit(StringAttributeColumnConfig config) {
				result.add(config.getName());
			}

			@Override
			public void visit(StatusColumnConfig config) {
				result.add(config.getName());
			}

			@Override
			public void visit(SingleValueListAttributeColumnConfig config) {
				result.add(config.getName());
			}
		});
		return result;
	}
}
