package com.digital.interactiveinvestor.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import static com.digital.ConfigProperty.TIMEOUT_DOM;

public abstract class BasePage
{


    @FindBy(css = "div.GridRow h2")
    private WebElement formTitle;

    protected WebDriver webDriver = null;
    protected Properties prop = new Properties();

    public BasePage(WebDriver webDriver){
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);

        try (InputStream input = new FileInputStream("resources/resources.properties")) {
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    protected abstract boolean isAt();

    protected Wait<WebDriver> waitForDom()
    {
        return new WebDriverWait(webDriver, TIMEOUT_DOM.getValueAsInt())
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    protected void waitForElementToBeVisible(WebElement element)
    {
        waitForDom().until(ExpectedConditions.visibilityOf(element));
    }

    protected void scrollToElement(WebElement element)
    {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
