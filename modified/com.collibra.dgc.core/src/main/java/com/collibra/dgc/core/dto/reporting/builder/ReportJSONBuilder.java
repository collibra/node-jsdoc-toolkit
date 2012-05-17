/**
 * 
 */
package com.collibra.dgc.core.dto.reporting.builder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.stereotype.Service;

import com.collibra.dgc.core.dto.reporting.Report;

/**
 * @author fvdmaele
 *
 */
@Service
public class ReportJSONBuilder {

	private JSONObject result = new JSONObject();
	
	public String toJSON(Report report, Integer echo) {
		// request echo
		if(echo == null)
			echo = 0;
		result.put("sEcho", echo);
		
		// number of records
		result.put("iTotalRecords", report.size());
		
		// number of filtered records
		result.put("iTotalDisplayRecords", report.size());
		
		// columns
		StringBuilder columns = new StringBuilder();
		boolean first = true;
		for(String column : report.getHeader()) {
			if(first) {
				first = false;
			} else {
				columns.append(",");
			}
			columns.append(column);
		}
		result.put("sColuns", columns.toString());
		
		// data
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>(report.size());
		int entrySize = report.getHeader().size();
		for(Entry<String, LinkedList[]> entry : report.getEntries()) {
			ArrayList<String> dataEntry = new ArrayList<String>(entrySize);
			for(int i = 0; i < entrySize; i++)
				dataEntry.add(entry.getValue()[i].get(0).toString());
			data.add(dataEntry);
		}
		
		result.put("aaData", data);
		
		return result.toString();
	}
}
