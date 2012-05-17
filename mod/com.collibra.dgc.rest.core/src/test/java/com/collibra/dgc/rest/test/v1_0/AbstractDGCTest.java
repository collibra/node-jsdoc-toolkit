package com.collibra.dgc.rest.test.v1_0;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import com.collibra.dgc.core.service.email.EmailService;
import com.collibra.dgc.rest.test.AbstractSpringAwareJerseyTest;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.WebAppDescriptor;

/**
 * @author dieterwachters
 * 
 */
@Ignore
public class AbstractDGCTest extends AbstractSpringAwareJerseyTest {

	private static final Logger log = LoggerFactory.getLogger(AbstractDGCTest.class);

	protected static File homeDir;

	public AbstractDGCTest() {
		super(new WebAppDescriptor.Builder("com.collibra.dgc.rest.core").contextPath("dgc-test")
				.contextParam("contextConfigLocation", "/com/collibra/dgc/applicationContext-test.xml")
				.servletClass(SpringServlet.class).contextListenerClass(ContextLoaderListener.class).build());
	}

	@BeforeClass
	public static void configureDB() throws Exception {
		// We disable email sending.
		System.setProperty(EmailService.EMAIL_DISABLED, "true");

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
