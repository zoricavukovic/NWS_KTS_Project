package com.example.serbUber.selenium.tests.bases;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class OneBrowserTestBase {

    public static WebDriver chromeDriver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
//        System.setProperty("webdriver.chrome.driver", "srdjanchromedriver");
        chromeDriver = new ChromeDriver();
        chromeDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
//        chromeDriver.manage().deleteAllCookies();
        chromeDriver.manage().window();
    }

    @AfterEach
    public void destroy() {
        chromeDriver.quit();
    }
}
