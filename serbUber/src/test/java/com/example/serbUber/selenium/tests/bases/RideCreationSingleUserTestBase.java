package com.example.serbUber.selenium.tests.bases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;

@PropertySource(
        ignoreResourceNotFound = false,
        value = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RideCreationSingleUserTestBase {

    public static WebDriver chromeDriver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "srdjanchromedriver");

        chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().maximize();
    }

    @AfterEach
    public void destroy() {
        chromeDriver.quit();
    }

}
