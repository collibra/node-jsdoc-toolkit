/**
 *
 */
package com.collibra.dgc.core.application;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import com.collibra.dgc.core.service.ConfigurationService;
import com.collibra.dgc.core.service.license.LicenseService;

/**
 * The main application component containing the user home, version ...
 * 
 * @author dieterwachters
 */
@Controller
@Lazy(true)
public class Application extends HttpServlet implements InitializingBean {
	private static final long serialVersionUID = 2090003317650784126L;
	private final Logger log = LoggerFactory.getLogger(Application.class);

	public static final String PROP_USER_HOME = "com.collibra.dgc.user.home";
	private static final String PROP_LOG_DIR = "com.collibra.dgc.log.directory";

	public static File USER_HOME = new File(System.getProperty("user.home"), "collibra/dgc");
	public static File CONFIG_DIR = new File(USER_HOME, "config");
	public static File EMAIL_TEMPLATES_DIR = new File(USER_HOME, "email-templates");
	public static File BOOTSTRAP_DIR = new File(USER_HOME, "bootstrap");
	public static File ATTACHMENTS_DIR = new File(USER_HOME, "attachments");
	public static File TRANSLATIONS_DIR = new File(USER_HOME, "translations");
	public static File PAGE_DEFINITIONS_DIR = new File(USER_HOME, "page-definitions");

	public static String VERSION = "0.0.0";
	public static String BUILD_NUMBER = "";

	public static final long STARTUP_DATE = System.currentTimeMillis();

	// Indicating that this is the developer edition.
	public static boolean DEVELOPER = true;

	@Autowired(required = false)
	private ServletContext servletContext;

	@Autowired
	private LicenseService licenseService;

	@RequestMapping("/core")
	public ModelAndView fallback(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		try {
			final String dir = System.getProperty(PROP_USER_HOME);
			if (dir != null && dir.length() != 0) {
				USER_HOME = new File(dir);
				if (!USER_HOME.exists()) {
					USER_HOME.mkdirs();
				}
			} else {
				String webAppPath = servletContext == null ? null : servletContext.getRealPath("/");
				if (webAppPath == null) {
					log.warn("Unable to determine web application context directory (probably running from an unpacked war). The default 'bsg' will be used");
					webAppPath = "bsg";
				} else {
					if (webAppPath.endsWith(File.separator)) {
						webAppPath = webAppPath.substring(0, webAppPath.length() - 1);
					}
					final String subContext = File.separator + "src" + File.separator + "main" + File.separator
							+ "webapp";
					if (webAppPath.endsWith(subContext)) {
						webAppPath = webAppPath.substring(0, webAppPath.length() - subContext.length());
					}
					if (webAppPath.contains(File.separator)) {
						webAppPath = webAppPath.substring(webAppPath.lastIndexOf(File.separator) + 1);
					}
				}

				USER_HOME = new File(System.getProperty("user.home"), "collibra/" + webAppPath);
				log.info("Using '" + USER_HOME.getAbsolutePath() + "' as user home directory.");
				if (!USER_HOME.exists()) {
					USER_HOME.mkdirs();
				}

				if (!USER_HOME.exists()) {
					log.error("Unable to create user.home directory '" + USER_HOME.getAbsolutePath()
							+ "'. Please check write permissions for this directory.");
					return;
				}
				log.info("User home directory initialized at '" + USER_HOME.getAbsolutePath() + "'.");
			}
		} catch (Exception e) {
			log.error("Error while retrieving application data directory.", e);
			return;
		}

		licenseService.validateLicense();

		CONFIG_DIR = new File(USER_HOME, "config");
		if (!CONFIG_DIR.exists()) {
			CONFIG_DIR.mkdirs();
		}

		EMAIL_TEMPLATES_DIR = new File(USER_HOME, "email-templates");
		if (!EMAIL_TEMPLATES_DIR.exists()) {
			EMAIL_TEMPLATES_DIR.mkdirs();
		}

		BOOTSTRAP_DIR = new File(USER_HOME, "bootstrap");
		if (!BOOTSTRAP_DIR.exists()) {
			BOOTSTRAP_DIR.mkdirs();
		}

		ATTACHMENTS_DIR = new File(USER_HOME, "attachements");
		if (!ATTACHMENTS_DIR.exists()) {
			ATTACHMENTS_DIR.mkdirs();
		}

		TRANSLATIONS_DIR = new File(USER_HOME, "translations");
		if (!TRANSLATIONS_DIR.exists()) {
			TRANSLATIONS_DIR.mkdirs();
		}

		PAGE_DEFINITIONS_DIR = new File(USER_HOME, "page-definitions");
		if (!PAGE_DEFINITIONS_DIR.exists()) {
			PAGE_DEFINITIONS_DIR.mkdirs();
		}

		// Configure logging.
		final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		try {
			final File logDir;
			final String logDirectory = System.getProperty(PROP_LOG_DIR);
			if (logDirectory != null && logDirectory.length() != 0) {
				logDir = new File(logDirectory);
			} else {
				logDir = new File(USER_HOME, "logs");
			}
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			final JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			// the context was probably already configured by default
			// configuration rules
			lc.reset();

			System.setProperty("DGC_LOG_DIR", logDir.getAbsolutePath());
			final File logConfigFile = new File(CONFIG_DIR, "logging.xml");
			if (!logConfigFile.exists()) {
				FileUtils.copyInputStreamToFile(
						ConfigurationService.class.getResourceAsStream("/com/collibra/dgc/core/log-config.xml"),
						logConfigFile);
			}
			configurator.doConfigure(logConfigFile);
		} catch (JoranException je) {
			log.error("Error while configuring logger.", je);
		}

		final InputStream vin = getClass().getResourceAsStream("/version.properties");
		if (vin != null) {
			Properties vp = new Properties();
			vp.load(vin);
			final String version = vp.getProperty("version");
			// The version number has been patched, so this means that we are running a built version.
			if (version.trim().charAt(0) != '$') {
				VERSION = version;
				BUILD_NUMBER = vp.getProperty("buildNumber");
				DEVELOPER = false;
			}
		}

		log.info("================================================================================");
		log.info("Version : " + VERSION + "\tBuild number: " + BUILD_NUMBER);
		log.info("User home directory: " + USER_HOME.getAbsolutePath());
		log.info("Memory: " + Runtime.getRuntime().totalMemory() + " allocated of " + Runtime.getRuntime().maxMemory()
				+ " maximum");
		log.info("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + " ("
				+ System.getProperty("os.arch") + ")");
		log.info("Java: " + System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
		log.info("================================================================================");
	}
}
