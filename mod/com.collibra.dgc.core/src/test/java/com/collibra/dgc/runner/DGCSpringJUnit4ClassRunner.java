package com.collibra.dgc.runner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.collibra.dgc.annotation.PerformanceTest;

public class DGCSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {

	public DGCSpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
		EachTestNotifier eachNotifier = springMakeNotifier(frameworkMethod, notifier);
		if (isTestMethodIgnored(frameworkMethod)) {
			eachNotifier.fireTestIgnored();
			return;
		}

		eachNotifier.fireTestStarted();
		try {
			Statement s = methodBlock(frameworkMethod);
			PerformanceTest pt = frameworkMethod.getAnnotation(PerformanceTest.class);
			if (pt == null) {
				s.evaluate();
			} else {
				for (int i = 0; i < pt.repetitions(); i++) {
					s.evaluate();
				}
			}
		} catch (AssumptionViolatedException e) {
			eachNotifier.addFailedAssumption(e);
		} catch (Throwable e) {
			eachNotifier.addFailure(e);
		} finally {
			eachNotifier.fireTestFinished();
		}
	}

	private EachTestNotifier springMakeNotifier(FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		return new EachTestNotifier(notifier, description);
	}

	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		List<FrameworkMethod> methods = super.computeTestMethods();
		methods.addAll(getTestClass().getAnnotatedMethods(PerformanceTest.class));
		// Remove duplicates
		methods = new ArrayList<FrameworkMethod>(new HashSet<FrameworkMethod>(methods));
		return methods;
	}

}
