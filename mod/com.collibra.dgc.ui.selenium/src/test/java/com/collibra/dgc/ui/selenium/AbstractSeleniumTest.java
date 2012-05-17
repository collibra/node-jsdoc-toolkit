package com.collibra.dgc.ui.selenium;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author
 * 
 */
public abstract class AbstractSeleniumTest {

	protected static RemoteWebDriver driver;
	protected static ChromeDriverService service;
	// public static final String BASE_URL = "http://localhost:8080/com.collibra.dgc.war"; //uncomment for local
	// instance run by jetty
	public static final String BASE_URL = "http://localhost:8080/dgc";// was changed for vm's purposes
	public static final String MAIN_PAGE = "/bin/view/";

	public enum BrowserType {
		FIREFOX, IE, CHROME, HTMLUNIT
	}

	@BeforeClass
	public static void initializeDriver() throws ClassNotFoundException, IllegalAccessException,
			InstantiationException, IOException {
		driver = setUpDriver(BrowserType.FIREFOX);
	}

	public static RemoteWebDriver setUpDriver(BrowserType browser) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException, IOException {
		switch (browser) {
		case FIREFOX:
			openFirefoxDriver();
			return driver;
		case IE:
			openIEDriver();
			return driver;
		case CHROME:
			openChromeDriver();
			return driver;
		default:
			throw new RuntimeException("Browser type unsupported");
		}
	}

	static public RemoteWebDriver openFirefoxDriver() throws IOException {
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		driver = new FirefoxDriver(firefoxProfile);
		return driver;
	}

	static public RemoteWebDriver openIEDriver() throws IOException {
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		driver = new InternetExplorerDriver(capabilities);
		return driver;
	}

	static public RemoteWebDriver openChromeDriver() throws IOException {

		service = new ChromeDriverService.Builder()
				.usingChromeDriverExecutable(new File("src/main/resources/chromedriver.exe")).usingAnyFreePort()
				.build();
		service.start();

		driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());

		return driver;
	}

	@AfterClass
	public static void tearDown() throws IOException {
		driver.quit();
	}
}
