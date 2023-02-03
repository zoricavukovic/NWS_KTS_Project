package com.example.serbUber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DrivingDetailsPage {

    private WebDriver driver;

    @FindBy(how = How.XPATH, using="//h1/b")
    private WebElement drivingDetailsTitle;

    public DrivingDetailsPage(WebDriver driver){
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    public boolean isDrivingDetailsPage(String title){
        return new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.textToBePresentInElement(drivingDetailsTitle, title));

    }

}
