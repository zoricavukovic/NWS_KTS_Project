package com.example.serbUber.selenium.tests.bases;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.TimeUnit;


@SpringBootTest
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
        chromeDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
//        chromeDriver.manage().deleteAllCookies();
        chromeDriver.manage().window();

        System.setProperty("webdriver.edge.driver", "msedgedriver");
//        System.setProperty("webdriver.edge.driver", "srdjanedgedriver.exe");
        edgeDriver = new EdgeDriver();
        edgeDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
//        edgeDriver.manage().deleteAllCookies();
        edgeDriver.manage().window();
    }

    @AfterEach
    public void destroy() {
        chromeDriver.quit();
        edgeDriver.quit();
    }
}
