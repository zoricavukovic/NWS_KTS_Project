package com.example.serbUber.selenium.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

import java.time.Duration;

import static com.example.serbUber.selenium.helper.Constants.FILTER_VEHICLE_TITLE;

public class SelectPassengersAndVehicleRideRequestPage {

    private WebDriver driver;

    private Actions actions;

    @FindBy(how = How.XPATH, using = "//img[contains(@src,'van')][@class='img-vehicle-types']")
    private WebElement vanVehicleType;

    @FindBy(how = How.XPATH, using = "//img[contains(@src,'suv')][@class='img-vehicle-types']")
    private WebElement suvVehicleType;

    @FindBy(how = How.XPATH, using = "//img[contains(@src,'car')][@class='img-vehicle-types']")
    private WebElement carVehicleType;

    @FindBy(how = How.ID, using = "request-ride")
    private WebElement requestRideButton;

    @FindBy(how = How.XPATH, using="//h1")
    private WebElement titleFilterVehicleDiv;

    @FindBy(how = How.XPATH, using = "//button[contains(@aria-label, 'expand_less')]")
    private List<WebElement> upArrowScheduleTime;

    @FindBy(how = How.XPATH, using = "//button[contains(@aria-label, 'expand_more')]")
    private List<WebElement> downArrowScheduleTime;

    @FindBy(how = How.TAG_NAME, using = "mat-panel-title")
    private WebElement passengersExpansionPanel;

    @FindBy(how = How.NAME, using = "passenger-email")
    private WebElement passengerEmailInput;

    @FindBy(how = How.XPATH, using = "//mat-option")
    private WebElement passengerAutocompleteOption;

    @FindBy(how = How.ID, using = "baby-seat-cb")
    private WebElement babySeatCheckBox;

    public SelectPassengersAndVehicleRideRequestPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);

        PageFactory.initElements(driver, this);
    }


    public void clickOnSuvVehicleType(){
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(suvVehicleType));
        suvVehicleType.click();
    }


    public boolean isVisibleNoDriverFound(String message){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@aria-label, '%s')]", message))));

        return webElement != null;
    }

    public boolean isVisibleInvalidChosenTime(String message){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@aria-label, '%s')]", message))));

        return webElement != null;
    }

    public void clickOnPassengersExpansionPanel() {
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(passengersExpansionPanel));

        passengersExpansionPanel.click();
    }

    public void clickOnAddPassengerInput(){
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(passengerEmailInput));

        actions.moveToElement(passengerEmailInput).click().perform();
    }

    public void addLinkedPassenger(String email) {
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(passengerEmailInput));

        this.passengerEmailInput.clear();
        this.passengerEmailInput.sendKeys(email);
    }

    public void clickOnFirstPassengerOption() {
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(passengerAutocompleteOption));

        this.actions.moveToElement(passengerAutocompleteOption).click().perform();
    }

    public boolean increaseTime(int numOfHours) {
        if (upArrowScheduleTime.size() == 0) {
            return false;
        }

        for (int i = 0; i < numOfHours; i++) {
            actions.moveToElement(upArrowScheduleTime.get(0)).click().perform();
        }

        return true;
    }

    public boolean decreaseTime(int numOfHours) {
        if (downArrowScheduleTime.size() == 0) {
            return false;
        }

        for (int i = 0; i < numOfHours; i++) {
            actions.moveToElement(downArrowScheduleTime.get(0)).click().perform();
        }

        return true;
    }

    public boolean setTimeForInvalidReservation(int increaseMinutes) {
        if (upArrowScheduleTime.size() == 0 || downArrowScheduleTime.size() == 0) {
            return false;
        }

        actions.moveToElement(downArrowScheduleTime.get(0)).click().perform();  //prvo se skida jedan sat
        for (int i = 0; i < increaseMinutes; i++) {
            actions.moveToElement(upArrowScheduleTime.get(1)).click().perform();
        }

        return true;
    }

    public void clickOnVanVehicleType(){
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(vanVehicleType));
        vanVehicleType.click();
    }

    public void clickOnCarVehicleType(){
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(carVehicleType));
        vanVehicleType.click();
    }

    public void clickOnRequestRideButton(){
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(requestRideButton));
        requestRideButton.click();
    }

    public boolean isVisibleNoEnoughTokensToast(String message){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@aria-label, '%s')]", message))));

        return webElement != null;
    }

    public boolean isVisibleInvalidNumberOfPassengersToast(String message){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@aria-label, '%s')]", message))));

        return webElement != null;
    }

    public boolean isFilterVehicleDiv(){
        return new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.textToBePresentInElement(titleFilterVehicleDiv, FILTER_VEHICLE_TITLE));

    }

    public void clickOnBabySeatCheckBox() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOf(babySeatCheckBox));
        actions.moveToElement(babySeatCheckBox).click().perform();
    }
}
