package com.example.serbUber.selenium.tests.bases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


public class TwoBrowserTestBase {
    public static WebDriver chromeDriver;
    public static WebDriver edgeDriver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "srdjanchromedriver");
        chromeDriver = new ChromeDriver();
//        chromeDriver.manage().deleteAllCookies();
        chromeDriver.manage().window();

        System.setProperty("webdriver.edge.driver", "srdjanedgedriver.exe");
        edgeDriver = new EdgeDriver();
//        edgeDriver.manage().deleteAllCookies();
        edgeDriver.manage().window();
    }

    @AfterEach
    public void destroy() {
        chromeDriver.quit();
        edgeDriver.quit();
    }
}
