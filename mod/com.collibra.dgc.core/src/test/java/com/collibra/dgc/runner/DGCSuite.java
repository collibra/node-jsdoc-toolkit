package com.collibra.dgc.runner;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class DGCSuite extends Suite {

	public DGCSuite(Class<?> klass) throws InitializationError {
		super(klass, getAnnotatedClasses(klass));
	}

	@Override
	protected void runChild(Runner runner, RunNotifier notifier) {
		try {
			// The huge hack making this all possible
			// inject another testrunner when this suite is running instead of the default runner
			runner = new DGCSpringJUnit4ClassRunner(((SpringJUnit4ClassRunner) runner).getTestClass().getJavaClass());

		} catch (InitializationError e) {
			e.printStackTrace();
		}
		super.runChild(runner, notifier);
	}

	private static Class<?>[] getAnnotatedClasses(Class<?> klass) throws InitializationError {
		SuiteClasses annotation = klass.getAnnotation(SuiteClasses.class);
		if (annotation == null)
			throw new InitializationError(String.format("class '%s' must have a SuiteClasses annotation",
					klass.getName()));
		return annotation.value();
	}

}
