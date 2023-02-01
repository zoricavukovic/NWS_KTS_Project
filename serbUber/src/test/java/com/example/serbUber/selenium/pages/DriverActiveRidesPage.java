package com.example.serbUber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverActiveRidesPage {
    private WebDriver driver;

    @FindBy(how = How.ID, using="finish-btn")
    private WebElement finishRideButton;

    @FindBy(how = How.ID, using="start-btn")
    private WebElement startRideButton;

    @FindBy(how = How.XPATH, using="//h3")
    private WebElement titleNoActiveOrFutureRides;

    public DriverActiveRidesPage(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    public void clickOnFinishRideButton(){
        new WebDriverWait(driver, Duration.ofSeconds(3))
            .until(ExpectedConditions.elementToBeClickable(finishRideButton));
        finishRideButton.click();
    }

    public boolean isDriverDoesntHaveActiveOrFutureRides(String noActiveOrFutureDrivingMessage){
        return new WebDriverWait(driver, Duration.ofSeconds(2))
            .until(ExpectedConditions.textToBePresentInElement(titleNoActiveOrFutureRides, noActiveOrFutureDrivingMessage));
    }

    public boolean isStartButtonPresent(String startDriveTitle){
        return new WebDriverWait(driver, Duration.ofSeconds(4))
            .until(ExpectedConditions.textToBePresentInElement(startRideButton, startDriveTitle));
    }
}
