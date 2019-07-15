package com.digital.interactiveinvestor;

import com.digital.interactiveinvestor.pages.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;

public class Setup {
    HomePage homePage;
    HeaderPage header;
    PortfolioPage portfolioPage;

    WebDriver webDriver;

    private String baseUrl = "https://www.ii.co.uk/indices/ftse-100-index";

    public void init() throws IOException {
        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/seleniumdrivers/geckodriver");

        webDriver = new FirefoxDriver();
        homePage = new HomePage(webDriver);
        header = new HeaderPage(webDriver);
        portfolioPage = new PortfolioPage(webDriver);
    }

    protected void navigateToStartPage() {
        webDriver.navigate().to(baseUrl);
    }

    protected void closeBrowser()
    {
        webDriver.close();
        webDriver = null;
    }
}
