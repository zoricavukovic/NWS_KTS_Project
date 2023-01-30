package com.example.serbUber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.example.serbUber.selenium.helper.Constants.DRIVING_DETAILS_TITLE;
import static com.example.serbUber.selenium.helper.Constants.FILTER_VEHICLE_TITLE;

public class DrivingDetailsPage {

    private WebDriver driver;

    @FindBy(how = How.XPATH, using="//h1/b")
    private WebElement drivingDetailsTitle;

    @FindBy(how = How.XPATH, using="//mat-card[@id='driver-card']/mat-card-header/div[@class='mat-card-header-text']/mat-card-title")
    private WebElement driverCardName;


    public DrivingDetailsPage(WebDriver driver){
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    public boolean isDrivingDetailsPage(String title){
        return new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.textToBePresentInElement(drivingDetailsTitle, title));

    }

    public boolean isCorrectDriver(String driverName){
        return new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.textToBePresentInElement(driverCardName, driverName));
    }

}
