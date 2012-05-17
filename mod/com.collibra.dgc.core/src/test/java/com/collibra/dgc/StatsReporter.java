package com.collibra.dgc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class StatsReporter {

	final static StatsReporter instance;
	Map<Class, Map<String, List<Long>>> results = new HashMap<Class, Map<String, List<Long>>>();
	static {
		instance = new StatsReporter();
	}

	private StatsReporter() {

	}

	public static StatsReporter getInstance() {
		return instance;
	}

	public void report(String method, Class testClass, Long runtime) {
		Map<String, List<Long>> resultsForClass = results.get(testClass);
		if (resultsForClass == null) {
			resultsForClass = new HashMap<String, List<Long>>();
			results.put(testClass, resultsForClass);
		}
		List<Long> times = resultsForClass.get(method);
		if (times == null) {
			times = new ArrayList<Long>();
			resultsForClass.put(method, times);
		}
		times.add(runtime);
	}

	public void print() {
		for (Entry<Class, Map<String, List<Long>>> entry : results.entrySet()) {
			System.out.println(entry.getKey().getCanonicalName());
			for (Entry<String, List<Long>> entry1 : entry.getValue().entrySet()) {
				System.out.println("\t" + entry1.getKey());
				Long total = 0L;
				Long max = null;
				Long min = null;
				int size = entry1.getValue().size();
				List<Long> values = entry1.getValue();
				for (Long value : values) {
					if (max == null) {
						max = value;
						min = value;
					}
					if (max < value) {
						max = value;
					}
					if (min > value) {
						min = value;
					}
					total += value;
				}
				System.out.println("\t\tExecutions : \t\t\t" + size + " executions");
				System.out.println("\t\tTotal : \t\t\t" + total + " ms ");
				System.out.println("\t\tMean : \t\t\t\t" + total / size + " ms");
				System.out.println("\t\tMax : \t\t\t\t" + max + " ms");
				System.out.println("\t\tMin : \t\t\t\t" + min + " ms");
				System.out.println("\t\tOperations Per Minute : \t" + 60000 / ((double) total / size));
				System.out.println("\t\tOperations Per Second : \t" + 1000 / ((double) total / size));
			}
		}

	}
}
