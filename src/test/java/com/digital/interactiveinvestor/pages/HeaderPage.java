package com.digital.interactiveinvestor.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HeaderPage extends BasePage {

    private static final String headerFormElement = "header.c0113";

    // @FindBy(css = "nav[data-testid='site-header-auth-buttons'] button")
    @FindBy(xpath = "//span[text()='Log in']")
    private WebElement loginBtn;

    // @FindBy(css = "nav[data-testid='site-header-auth-buttons'] button.c0160")
    @FindBy(xpath = "//span[text()='Account']")
    private WebElement accountBtn;

    @FindBy(linkText = "Virtual Portfolio")
    private WebElement virtualPortfolio;

    @FindBy(css = "div.DropdownList__container")
    private WebElement accountPopup;

    @FindBy(css = "div.c01158")
    private WebElement loginPop;

    // @FindBy(css = "div.c01158 button.c0158")
    @FindBy(xpath = "//span[text()='research account login']")
    private WebElement researchLoginBtn;

    @FindBy(css = "form.auth0-lock-widget")
    private WebElement researchLoginPopup;

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(css = "input[name='password']")
    private WebElement password;

    @FindBy(css = "button[type='submit']")
    private WebElement researchLoginPopupBtn;

    public HeaderPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public boolean isAt() {
        return false;
    }

    public void loginUser() {
        waitForElementToBeVisible(loginBtn);
        loginBtn.click();
        waitForElementToBeVisible(loginPop);
        researchLoginBtn.click();
        waitForElementToBeVisible(email);
        email.sendKeys(prop.getProperty("email"));
        password.sendKeys(prop.getProperty("password"));
        researchLoginPopupBtn.click();
    }

    public void goToPortfolio() {
        waitForElementToBeVisible(accountBtn);
        accountBtn.click();
        waitForElementToBeVisible(accountPopup);
        virtualPortfolio.click();
    }
}
