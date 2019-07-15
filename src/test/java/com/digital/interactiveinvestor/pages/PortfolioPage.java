package com.digital.interactiveinvestor.pages;

import com.digital.interactiveinvestor.helpers.Stock;
import com.digital.interactiveinvestor.helpers.StockTotals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class PortfolioPage extends BasePage {

    @FindBy(css = "div.GridRow h1")
    private WebElement heading;

    // @FindBy(css = "div.c01545 > div:nth-child(4)")
    @FindBy(xpath = "//span[text()='Delete Portfolio']")
    private WebElement deletePortfolioBtn;

    // @FindBy(css = "div.c01160 button.c0160")
    @FindBy(xpath = "//span[text()='Delete']")
    private WebElement confirmDeletePortfolioBtn;

    @FindBy(css = "input[name='portfolioName']")
    private WebElement portfolioNameTxtbox;

    @FindBy(css = "table tbody tr")
    private List<WebElement> portfolioTableRows;

    @FindBy(css = "table") // no unique identifier for the table, the css class changes on page refresh, but there is only 1 table on the page
    private WebElement portfolioTable;

    @FindBy(css = "input[name='portfolioName']")
    private List<WebElement> portfolioTxtBox;

    @Override
    public boolean isAt() {
        waitForElementToBeVisible(heading);
        return heading.getText().equals(prop.getProperty("portfolioHeadingText"));
    }

    public PortfolioPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void deletePortfolio() {
        if (portfolioTxtBox.size() == 0) {
            waitForElementToBeVisible(deletePortfolioBtn);
            deletePortfolioBtn.click();
            confirmDeletePortfolioBtn.click();
            waitForElementToBeVisible(portfolioNameTxtbox);
        }
    }

    public Stock getStockDetails() {
        waitForElementToBeVisible(portfolioTable);
        Stock stock = new Stock();
        stock.setName(getStockName(portfolioTableRows.get(0))); //hard coding the first row
        stock.setPrice(getStockAveragePrice(portfolioTableRows.get(0)));
        stock.setShareSize(getStockShareSize(portfolioTableRows.get(0)));
        return stock;
    }

    public Stock getStockDetails(String stockName) {
        waitForElementToBeVisible(portfolioTable);
        Stock stock = new Stock();
        stock.setName(stockName);

        for (WebElement el :
                portfolioTableRows) {
           if(getStockName(el).equals(stockName))
            {
                stock.setPrice(getStockAveragePrice(el));
                stock.setShareSize(getStockShareSize(el));
                break;
            }
        }

        return stock;
    }

    public StockTotals getTotals() {
        waitForElementToBeVisible(portfolioTable);
        StockTotals totals = new StockTotals();
        int totalsRow = portfolioTableRows.size() - 1;
        totals.setBookCost(portfolioTableRows.get(totalsRow).findElement(By.cssSelector("td:nth-child(7)")).getText());
        totals.setBookValue(portfolioTableRows.get(totalsRow).findElement(By.cssSelector("td:nth-child(8)")).getText());
        totals.setGain(portfolioTableRows.get(totalsRow).findElement(By.cssSelector("td:nth-child(9)")).getText());
        totals.setPercentGain(portfolioTableRows.get(totalsRow).findElement(By.cssSelector("td:nth-child(10)")).getText());
        return totals;
    }

    private String getStockName(WebElement el)
    {
        return el.findElement(By.cssSelector("td a")).getText();
    }

    private String getStockAveragePrice(WebElement el)
    {
        return el.findElement(By.cssSelector("td:nth-child(7)")).getText();
    }

    private String getStockShareSize(WebElement el)
    {
        return el.findElement(By.cssSelector("td:nth-child(6)")).getText();
    }
}
