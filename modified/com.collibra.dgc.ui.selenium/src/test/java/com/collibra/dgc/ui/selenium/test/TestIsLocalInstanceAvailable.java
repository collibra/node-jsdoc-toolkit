package com.collibra.dgc.ui.selenium.test;

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.collibra.dgc.ui.selenium.AbstractSeleniumTest;

public class TestIsLocalInstanceAvailable extends AbstractSeleniumTest {

	@Test
	public void TestDGCStartup() {
		driver.get(BASE_URL);
		String defaultTitle = "Data Governance Center";
		Assert.assertTrue("DGC has loaded", new WebDriverWait(driver, 30)
				.until(ExpectedConditions.titleIs(defaultTitle)));
	}
}
