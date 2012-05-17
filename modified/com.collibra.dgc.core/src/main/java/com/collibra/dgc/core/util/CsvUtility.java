package com.collibra.dgc.core.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * CSV utility functions.
 * @author amarnath
 * 
 */
public class CsvUtility {
	/**
	 * Converts list of string to CSV string.
	 * @param values {@link List} of strings.
	 * @return The CSV string.
	 */
	public static String getCSV(Collection<String> values) {
		return getCSV(values, CSVWriter.DEFAULT_QUOTE_CHARACTER);
	}

	/**
	 * Covert CSV to list.
	 * @param csvString The CSV string.
	 * @return The list of strings.
	 */
	public static List<String> getList(String csvString) {
		StringReader sr = new StringReader(csvString);
		CSVReader reader = new CSVReader(sr);
		try {
			List<String[]> newlist = reader.readAll();
			if (newlist.size() > 0) {
				List<String> values = new LinkedList<String>();
				for (String val : newlist.get(0)) {
					values.add(val.trim());
				}
				return values;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	/**
	 * Converts list of strings to CSV with specified quote character.
	 * @param values The string collection.
	 * @param quoteChar The quote character.
	 * @return The CSV string.
	 */
	public static String getCSV(Collection<String> values, char quoteChar) {
		String[] p = values.toArray(new String[0]);
		List<String[]> newlist = new LinkedList<String[]>();
		newlist.add(p);
		StringWriter sw = new StringWriter();
		CSVWriter writer = new CSVWriter(sw, CSVWriter.DEFAULT_SEPARATOR, quoteChar);
		writer.writeAll(newlist);
		return sw.getBuffer().toString();
	}
}
