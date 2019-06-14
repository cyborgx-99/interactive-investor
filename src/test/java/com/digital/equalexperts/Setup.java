package com.digital.equalexperts;

import com.digital.equalexperts.pages.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;

public class Setup {
    HomePage homePage;

    WebDriver webDriver;

    private String baseUrl = "http://hotel-test.equalexperts.io/";

    public void init() throws IOException {
        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/seleniumdrivers/geckodriver");

        webDriver = new FirefoxDriver();
        homePage = new HomePage(webDriver);
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
