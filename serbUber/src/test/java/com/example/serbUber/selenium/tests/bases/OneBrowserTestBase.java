package com.example.serbUber.selenium.tests.bases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;


public class OneBrowserTestBase {

    public static WebDriver chromeDriver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "srdjanchromedriver");
        chromeDriver = new ChromeDriver();
//        chromeDriver.manage().deleteAllCookies();
        chromeDriver.manage().window();
    }

    @AfterEach
    public void destroy() {
        chromeDriver.quit();
    }
}
