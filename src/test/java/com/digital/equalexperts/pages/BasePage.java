package com.digital.equalexperts.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.util.NoSuchElementException;

import static com.digital.ConfigProperty.TIMEOUT_DOM;

public abstract class BasePage
{
    @FindBy(css = "header.page-header h1")
    private WebElement heading;

    protected WebDriver webDriver = null;


    public BasePage(WebDriver webDriver){
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public abstract boolean isAt();

    public Wait<WebDriver> waitForDom()
    {
        return new WebDriverWait(webDriver, TIMEOUT_DOM.getValueAsInt())
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    public void waitForElementToBeVisible(WebElement element)
    {
        waitForDom().until(ExpectedConditions.visibilityOf(element));
    }

    protected boolean waitForElementTextToNotEqual(By locator, String originalText) {
        boolean textMatches = true;
        try{
            WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT_DOM.getValueAsInt());
            textMatches = wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(locator, originalText)));
        }
        catch(TimeoutException e1){
            e1.printStackTrace();
        }

        return textMatches;
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}
