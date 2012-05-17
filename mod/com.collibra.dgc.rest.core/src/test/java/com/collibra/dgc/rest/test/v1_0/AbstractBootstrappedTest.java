/**
 * 
 */
package com.collibra.dgc.rest.test.v1_0;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import com.collibra.dgc.core.service.bootstrapper.BootstrapService;
import com.collibra.dgc.core.service.job.IJobProgressHandler;

/**
 * @author dieterwachters
 * @author pmalarme
 * 
 */
@Ignore
public class AbstractBootstrappedTest extends AbstractDGCTest {

	private static final Logger log = LoggerFactory.getLogger(AbstractBootstrappedTest.class);

	@Autowired
	protected PlatformTransactionManager txManager;

	@Autowired
	protected BootstrapService bootstrapService;

	@Before
	public void bootstrap() throws Exception {
		bootstrapService.bootstrap("basic", new IJobProgressHandler() {
			@Override
			public boolean isCanceled() {
				return false;
			}

			@Override
			public void reportProgress(double progress, String message) {
			}

			@Override
			public void reportCompleted(String message) {
			}

			@Override
			public void reportError(String message) {
				log.error(message);
			}
		});
	}

	@After
	public void cleanup() {
	}
}
