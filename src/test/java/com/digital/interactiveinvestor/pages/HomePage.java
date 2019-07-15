package com.digital.interactiveinvestor.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.digital.ConfigProperty.TIMEOUT_DOM;

public class HomePage extends BasePage{

    @FindBy(css = "div.GridRow h2")
    private WebElement heading;

    @FindBy(css = "select[name='portfolioId']")
    private WebElement portfolioNameSelection;

    @FindBy(css = "input[name='newPortfolioName']")
    private WebElement portfolioName;

    @FindBy(css = "input[name='newPortfolio']")
    private WebElement newPortfolioChkbx;

    //@FindBy(css = "div.c01496 button.c0158")
    @FindBy(xpath = "//span[text()='Add to Virtual Portfolio']")
    private WebElement addToPortfolioBtn;

    @FindBy(css = "input[name='index_member_search']")
    private WebElement searchConstituents;

    @FindBy(css = "input[name='units']")
    private WebElement shares;

    @FindBy(css = "input[name='perShareCost']")
    private WebElement shareUnitPrice;

    private By rows = By.cssSelector("table.c01272 tbody tr");
    private By portfolioPopup = By.cssSelector("div.c01580");
    private By stockName = By.cssSelector("table.c01272 tbody tr:nth-child(1) td a");

    public HomePage(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean isAt(){
        waitForElementToBeVisible(heading);
        return heading.getText().equals(prop.getProperty("homepageHeadingText"));
    }

    public void searchForStock(String name) {
        waitForElementToBeVisible(searchConstituents);
        searchConstituents.clear();
        searchConstituents.sendKeys(name);
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT_DOM.getValueAsInt());
        wait.until(ExpectedConditions.numberOfElementsToBe(rows, 1));

        //second check added due to the speed of the test,
        // it is possible to add barclays to the portfolio twice unless you wait for the page to refresh with the correct symbol
        wait.until(ExpectedConditions.textToBePresentInElementLocated(stockName, name));
    }

    public HomePage addStockToPortFolio() {
        WebElement threeDots = webDriver.findElement(rows).findElement(By.cssSelector("div.c01315"));
        scrollToElement(threeDots);
        threeDots.click();
        webDriver.findElement(By.cssSelector("div.c01315 div.DropdownList__container ul li:nth-child(1)")).click();
        waitForElementToBeVisible(portfolioNameSelection);
        return this;
    }

    public HomePage createNewPortfolio(String name) {
        newPortfolioChkbx.click();
        waitForElementToBeVisible(portfolioName);
        portfolioName.sendKeys(name);
        return this;
    }

    public HomePage withNumberOfShares(String shareSize) {
        shares.sendKeys(shareSize);
        return this;
    }

    public String addToPortfolio() {
        // WebElement sharePrice = webDriver.findElement(rows).findElement(By.cssSelector("td:nth-child(2)"));
        String sharePrice = shareUnitPrice.getAttribute("value");
        addToPortfolioBtn.click();
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT_DOM.getValueAsInt());
        wait.until(ExpectedConditions.invisibilityOfElementLocated(portfolioPopup));
        return sharePrice;
    }

    public HomePage useExistingPortfolio(String name) {
        Select porfolioSelection = new Select(portfolioNameSelection);
        porfolioSelection.selectByVisibleText(name);
        return this;
    }
}
