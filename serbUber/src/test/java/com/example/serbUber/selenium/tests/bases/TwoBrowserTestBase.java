package com.example.serbUber.selenium.tests.bases;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TwoBrowserTestBase {
    public static WebDriver chromeDriver;
    public static WebDriver edgeDriver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
//        System.setProperty("webdriver.chrome.driver", "srdjanchromedriver");
        chromeDriver = new ChromeDriver();
//        chromeDriver.manage().deleteAllCookies();
        chromeDriver.manage().window();

        System.setProperty("webdriver.edge.driver", "msedgedriver");
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
