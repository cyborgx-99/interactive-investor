package com.digital.interactiveinvestor;

import com.digital.interactiveinvestor.helpers.Stock;
import com.digital.interactiveinvestor.helpers.StockTotals;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

public class SimpleTest extends Setup
{
    @Before
    public void before() throws IOException {
        super.init();
        navigateToStartPage();
        Assert.assertTrue(homePage.isAt());
        header.loginUser();
    }

    @Test
    public void addStockToVirtualPortfolio() throws ParseException {
        header.goToPortfolio();
        portfolioPage.isAt();
        portfolioPage.deletePortfolio();
        navigateToStartPage();

        //only automating the fields i require, others can be added later on when required
        homePage.searchForStock("BARC");
        String shareprice = homePage.addStockToPortFolio()
                            .createNewPortfolio("Test1")
                            .withNumberOfShares("100")
                            .addToPortfolio();

        System.out.println("share price " + shareprice);

        header.goToPortfolio();
        Stock stock = portfolioPage.getStockDetails();

        Assert.assertTrue(String.format("Stock name does not match Barclays, %s", stock.getName()),
                stock.getName().equals("Barclays"));
        Assert.assertTrue(String.format("Expected stock price %s does not match %s", shareprice, stock.getPrice()),
                stock.getPrice().contains(shareprice));
        Assert.assertTrue(String.format("share size does not match", stock.getShareSize()),
                stock.getShareSize().equals("100"));
    }

    @Test
    public void addMultipleStocksToVirtualPortfolio() throws ParseException {
        header.goToPortfolio();
        portfolioPage.isAt();
        portfolioPage.deletePortfolio();
        navigateToStartPage();
        homePage.searchForStock("BARC");
        String barcShareprice = homePage.addStockToPortFolio()
                .createNewPortfolio("Test1")
                .withNumberOfShares("100")
                .addToPortfolio();

        homePage.searchForStock("HSBC");
        String hsbcShareprice = homePage.addStockToPortFolio()
                .useExistingPortfolio("Test1")
                .withNumberOfShares("200")
                .addToPortfolio();


        header.goToPortfolio();

        Stock barcStock = portfolioPage.getStockDetails("Barclays");
        Stock hsbcStock = portfolioPage.getStockDetails("HSBC Holdings");
        StockTotals total = portfolioPage.getTotals();


        System.out.println("Barc stock price " + barcStock.getPrice());
        System.out.println("HSBC stock price " + hsbcStock.getPrice());

        Assert.assertTrue(String.format("Expected stock price %s does not match %s", barcShareprice, barcStock.getPrice()),
                barcStock.getPrice().contains(barcShareprice));
        Assert.assertTrue(String.format("share size does not match", barcStock.getShareSize()),
                barcStock.getShareSize().equals("100"));

        Assert.assertTrue(String.format("Expected stock price %s does not match %s", hsbcShareprice, hsbcStock.getPrice()),
                hsbcStock.getPrice().contains(hsbcShareprice));
        Assert.assertTrue(String.format("share size does not match", hsbcStock.getShareSize()),
                hsbcStock.getShareSize().equals("200"));

        //check if the values from the toals row are not 0.00
        Assert.assertFalse(String.format("Book cost is 0"),
                total.getBookCost().equals("£0.00"));

        Assert.assertFalse(String.format("Book value is 0"),
                total.getBookValue().equals("£0.00"));

        // although unlikely, but it is possible to get a value of 0% so comparing to blank value instead
        Assert.assertFalse(String.format("total gain is blank"),
                total.getGain().equals(""));

        Assert.assertFalse(String.format("total percent gain is blank"),
                total.getPercentGain().equals(""));

        // Going further, i would extend the test to pick up the other values in the table, add them up and then compare to the totals row
        // This assumes the values are correct, as it stands, I have no way of verifying the values displayed
    }

    @After
    public void close()
    {
        closeBrowser();
    }
}
