package com.collibra.dgc.ui.selenium.page.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TextEditor {
	
	String someText="Lorem ipsum dolor sit amet, consectetuer adipiscing elit.";
	String textToBold="Nullam dictum felis eu pede mollis pretium.";
	 
	//Location of text editor
	@FindBy(id="wysiwyg_large-wysiwyg-iframe")
	private WebElement textEditor;
	
	//Location of default text on frame "wysiwyg_large-wysiwyg-iframe"
	@FindBy(className="undefined")
	private WebElement defaultTextShown;
	
	//Location of text input on frame "wysiwyg_large-wysiwyg-iframe"
	@FindBy(className="wysiwyg")
	private WebElement textInput;
	
	//Location of empty text input on frame "wysiwyg_large-wysiwyg-iframe"
	@FindBy(css="html")
	private WebElement emptyTextInput;
	
	//Location of empty text input on frame "wysiwyg_large-wysiwyg-iframe"
	@FindBy(xpath="html/body")
	private WebElement emptyTextInput2;
	
	//Location of page background
	@FindBy(id="maincontainer")
	private WebElement background;
	
	//Location of page background
	@FindBy(className="icon-bold")
	private WebElement boldButton;
	
	public TextEditor(WebDriver driver){
		PageFactory.initElements(driver, this);
	}
	
	public  boolean isVisible(WebDriver driver) {
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(textEditor));
		return textEditor.isDisplayed();
		
	}
	
	public boolean hasDefaultText(String defaultText, WebDriver driver) {
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(textEditor));
		driver.switchTo().frame("wysiwyg_large-wysiwyg-iframe");
		return defaultTextShown.getText().equals(defaultText);
		
	}


	public boolean isRemovedTextDefaultOnClick (WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("document.body.wysiwyg = '<br>'");
		driver.switchTo().frame("wysiwyg_large-wysiwyg-iframe");
		textInput.click();
		if (!textInput.getText().isEmpty())
			return false;
		return true;
	}

	public boolean 	isRestoredTextDefault (String defaultText, WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("document.body.wysiwyg = '<br>'");
		driver.switchTo().frame("wysiwyg_large-wysiwyg-iframe");
		textInput.click();
		driver.switchTo().defaultContent();
		background.click();
		driver.switchTo().frame("wysiwyg_large-wysiwyg-iframe");
		if (!defaultTextShown.getText().equals(defaultText))
			return false;
		return true;
	}
	
	public boolean isEditable (WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("document.body.wysiwyg = '<br>'");
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(textEditor));
		driver.switchTo().frame("wysiwyg_large-wysiwyg-iframe");
		textInput.sendKeys(someText, Keys.ENTER);
		if (!textInput.getText().equals(someText))
			return false;
		return true;
	}
	
	public void setBold (WebDriver driver) {
		
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(boldButton));
		boldButton.click();
	}

	public boolean isTypedTextBold(WebDriver driver) {
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(textEditor));
		driver.switchTo().frame("wysiwyg_large-wysiwyg-iframe");
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(emptyTextInput));
		textInput.sendKeys(textToBold, Keys.ENTER);
		textInput.sendKeys(Keys.chord(Keys.LEFT_CONTROL,"a"));
		driver.switchTo().defaultContent();
		setBold(driver);
		driver.switchTo().frame("wysiwyg_large-wysiwyg-iframe");
		String bold=textInput.findElement(By.tagName("b")).getText();
		if (!bold.equals(textToBold))
			return false;
		return true;

	}



}
