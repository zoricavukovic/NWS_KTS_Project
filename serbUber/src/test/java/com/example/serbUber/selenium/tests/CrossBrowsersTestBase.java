package com.example.serbUber.selenium.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class CrossBrowsersTestBase {
    public static WebDriver edgeDriver;
    public static WebDriver chromeDriver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "srdjanchromedriver");

        chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().maximize();

        System.setProperty("webdriver.edge.driver", "srdjanedgedriver.exe");
        edgeDriver = new EdgeDriver();
    }

    @AfterEach
    public void destroy() {
        chromeDriver.quit();
        edgeDriver.quit();
    }
}
