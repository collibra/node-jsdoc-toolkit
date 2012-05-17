package com.collibra.dgc.ui.selenium.test;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;

import com.collibra.dgc.ui.selenium.AbstractSeleniumTest;
import com.collibra.dgc.ui.selenium.page.elements.TextEditor;

public class TestTextEditor extends AbstractSeleniumTest {

	public static final String URL = "/test/texteditor";
	String defaultTextInFullEditor = "Here goes label for full editor";

	@Ignore
	@Test
	public void isPresentTextDefaultText() {
		driver.get(BASE_URL + URL);
		TextEditor textEditor = new TextEditor(driver);
		Assert.assertTrue("TextEditor is not visible on page",
				textEditor.isVisible(driver));
		Assert.assertTrue("Text in Editor does not equal to default text: "
				+ defaultTextInFullEditor,
				textEditor.hasDefaultText(defaultTextInFullEditor, driver));

	}

	@Ignore
	@Test
	public void isRemovedTextDefaultTextInFocus() {
		driver.get(BASE_URL + URL);
		TextEditor textEditor = new TextEditor(driver);
		Assert.assertTrue("Text Editor is not empty",
				textEditor.isRemovedTextDefaultOnClick(driver));
	}

	@Ignore
	@Test
	public void isRestoredTextDefaultTextAfterFocus() {
		driver.get(BASE_URL + URL);
		TextEditor textEditor = new TextEditor(driver);
		Assert.assertTrue("Text in Editor does not equal to default text: "
				+ defaultTextInFullEditor, textEditor.isRestoredTextDefault(
				defaultTextInFullEditor, driver));
	}

	@Ignore
	@Test
	// typing some text and checking is it equal typed or not
	public void isEditableTextEditor() {
		driver.get(BASE_URL + URL);
		TextEditor textEditor = new TextEditor(driver);
		Assert.assertTrue("Text is not the same as typed",
				textEditor.isEditable(driver));
	}

	@Ignore //works for firefox and chrome
	@Test
	// typing some text using formating bold
	public void isEditableTextBold() {
		driver.get(BASE_URL + URL);
		TextEditor textEditor = new TextEditor(driver);
		Assert.assertTrue("Text is not bold",
				textEditor.isTypedTextBold(driver));
		//textEditor.setBold(driver);
	}
	

}
