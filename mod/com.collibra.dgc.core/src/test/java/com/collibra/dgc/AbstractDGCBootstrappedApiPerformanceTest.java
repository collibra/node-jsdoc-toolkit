package com.collibra.dgc;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import com.collibra.dgc.api.AbstractDGCBootstrappedApiTest;

public class AbstractDGCBootstrappedApiPerformanceTest extends AbstractDGCBootstrappedApiTest {
	private final Random random = new Random();

	@Test
	@Ignore
	public void ignore() {
	}

	protected void report(String methodName, Long time) {
		StatsReporter.getInstance().report(methodName, this.getClass(), time);
	}

	public String randomText() {
		return new BigInteger(130, random).toString(32);
	}

}
