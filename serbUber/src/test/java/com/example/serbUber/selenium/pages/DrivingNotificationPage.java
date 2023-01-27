package com.example.serbUber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DrivingNotificationPage {

    private WebDriver driver;
    @FindBy(how = How.ID, using = "reject-ride-btn")
    private WebElement rejectRideButton;

    @FindBy(how = How.ID, using = "accept-ride-btn")
    private WebElement acceptRideButton;

    @FindBy(how = How.XPATH, using = "//div[contains(@aria-label, 'Successfully rejected ride')]")
    private WebElement rideRejectedToast;

    public DrivingNotificationPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickOnAcceptRideButton(){
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(acceptRideButton));
        acceptRideButton.click();
    }

    public void clickOnRejectRideButton(){
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(rejectRideButton));
        rejectRideButton.click();
    }

    public boolean isVisibleRideIsRejectedToast(){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOf(rideRejectedToast));

        return webElement != null;
    }
}
