package com.collibra.dgc.core.dto.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author fvdmaele
 *
 */
public class Report {

	private LinkedHashMap<String, LinkedList[]> table;
	private ArrayList<String> header;
	private long totalSize = 0;
	
	public void initializeMatrix(List<String> headerStrings) {
		// build table header
		header = new ArrayList<String>();
		header.addAll(headerStrings);
		
		// build table
		table = new LinkedHashMap<String, LinkedList[]>();
	}
	
	public void initializeMatrix(List<String> headerStrings, int tableSize) {
		// build table header
		header = new ArrayList<String>();
		header.addAll(headerStrings);
		
		// build table
		table = new LinkedHashMap<String, LinkedList[]>(tableSize);
	}
	
	public boolean addEntry(String rId, String entryType, Object entryValue) {
		int pos = header.indexOf(entryType);
		if(pos < 0)
			return false;
		
		LinkedList[] entryRow = table.get(rId);
		
		// if row doesnt exist yet, make it
		if(entryRow == null) {
			entryRow = new LinkedList[header.size()];
			for(int i = 0; i < header.size(); i++)
				entryRow[i] = new LinkedList();
			table.put(rId, entryRow);
		}
		
		LinkedList entry = entryRow[pos];
		entry.add(entryValue);
		return true;
	}
	
	public Object getEntryValue(String rId, String entryType) {
		List result = getEntryValues(rId, entryType);
		if(!result.isEmpty())
			return result.get(0);
		
		return null;
	}
	
	public List getEntryValues(String rId, String entryType) {
		int pos = header.indexOf(entryType);
		if(pos < 0)
			return new ArrayList(0);
		
		LinkedList[] entryRow = table.get(rId);
		if(entryRow == null)
			return new ArrayList(0);
		
		return entryRow[pos];
	}
	
	public LinkedList[] getEntry(String rId) {
		return table.get(rId);
	}
	
	public Set<Entry<String,LinkedList[]>> getEntries() {
		return table.entrySet();
	}
	
	public Set<String> getResourceIds() {
		return table.keySet();
	}
	
	public List<String> getHeader() {
		return this.header;
	}
	
	public int size() {
		return table.size();
	}
	
	public long getTotalSize() {
		if(totalSize > 0)
			return this.totalSize;
		else
			return size();
	}
	
	public void setTotalSize(long size) {
		this.totalSize = size;
	}
}
