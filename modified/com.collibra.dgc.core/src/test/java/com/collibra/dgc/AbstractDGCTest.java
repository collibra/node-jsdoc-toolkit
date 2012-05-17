package com.collibra.dgc;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.collibra.dgc.core.service.SearchService;
import com.collibra.dgc.core.service.email.EmailService;

/**
 * 
 */

/**
 * @author dieterwachters
 * 
 */
@Ignore
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@TransactionConfiguration(transactionManager = "dgcTxManager")
public abstract class AbstractDGCTest extends AbstractJUnit4SpringContextTests {
	private static final Logger log = LoggerFactory.getLogger(AbstractDGCTest.class);

	protected static File homeDir;

	@BeforeClass
	public static void configureDB() throws Exception {
		// We disable email sending.
		System.setProperty(EmailService.EMAIL_DISABLED, "true");
		System.setProperty(SearchService.PROPERTY_PAUSE_INDEXER, "true");
		System.setProperty(SearchService.PROPERTY_FULL_REINDEX_SKIP, "true");

		// Initializing the user home directory.
		String userHome = System.getProperty("com.collibra.dgc.user.home");
		if (userHome == null) {
			homeDir = new File(System.getProperty("user.home"), "collibra/dgc-test");
			if (homeDir.exists()) {
				FileUtils.deleteDirectory(homeDir);
			}
			homeDir.mkdirs();
			new File(homeDir, "config").mkdirs();

			String testConfig = System.getProperty("com.collibra.dgc.test.config");
			if (testConfig != null) {
				if (testConfig.equalsIgnoreCase("mysql") || testConfig.equalsIgnoreCase("hsql")
						|| testConfig.equalsIgnoreCase("oracle") || testConfig.equalsIgnoreCase("mssql")) {
					log.info("Using " + testConfig.toLowerCase() + " test configuration file .");
					FileUtils.copyInputStreamToFile(
							AbstractDGCTest.class.getResourceAsStream("/com/collibra/dgc/" + testConfig.toLowerCase()
									+ "/test-config.xml"), new File(homeDir, "config/configuration.xml"));
				} else {
					log.info("Using test configuration file '" + testConfig + "'.");
					final FileInputStream fis = new FileInputStream(testConfig);
					FileUtils.copyInputStreamToFile(fis, new File(homeDir, "config/configuration.xml"));
					fis.close();
				}
			} else {
				log.info("Using internal test configuration file .");
				FileUtils.copyInputStreamToFile(AbstractDGCTest.class
						.getResourceAsStream("/com/collibra/dgc/mysql/test-config.xml"), new File(homeDir,
						"config/configuration.xml"));
			}

			System.setProperty("com.collibra.dgc.user.home", homeDir.getAbsolutePath());
		}
	}
}
