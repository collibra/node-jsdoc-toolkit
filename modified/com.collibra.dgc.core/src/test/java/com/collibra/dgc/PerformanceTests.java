package com.collibra.dgc;

import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.collibra.dgc.api.representation.TestTermApiPerformance;
import com.collibra.dgc.api.representation.TestTermApiPerformance2;
import com.collibra.dgc.runner.DGCSuite;

@RunWith(DGCSuite.class)
@Ignore
@SuiteClasses({ TestTermApiPerformance.class, TestTermApiPerformance2.class })
/**
 * Note that every class that is a performance test must be listed in the annotation at the top.
 * To write a performance test place a {@link PerformanceTest} annotation on every test specifying the number of repititions
 * make tests extend from{@link AbstractDGCBootstrappedApiPerformanceTest} and use it's report method to report the infromation.
 * After all tests are run, the information will be outputted
 **/
public class PerformanceTests {
	@AfterClass
	public static void tearDown() {
		StatsReporter.getInstance().print();
	}

}
