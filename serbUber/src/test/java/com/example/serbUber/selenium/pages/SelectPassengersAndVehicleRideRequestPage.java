package com.example.serbUber.selenium.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    @FindBy(how = How.XPATH, using = "//div[contains(@aria-label, 'Ride is rejected because payment was not successful.')]")
    private WebElement notEnoughTokensToast;

    @FindBy(how = How.XPATH, using="//h1")
    private WebElement titleFilterVehicleDiv;

    @FindBy(how = How.XPATH, using = "//div[contains(@aria-label, 'Ride is rejected because driver is not found.')]")
    private WebElement driverNotFoundToast;

    @FindBy(how = How.XPATH, using = "//div[contains(@aria-label, 'Invalid chosen time')]")
    private WebElement invalidChosenTimeToast;

    @FindBy(how = How.XPATH, using = "//div[contains(@aria-label, 'You can not add passenger!')]")
    private WebElement invalidNumberOfPassengersToast;

    @FindBy(how = How.XPATH, using = "//input[@ng-reflect-name='hour']")
    private WebElement scheduleHourInput;

    @FindBy(how = How.XPATH, using = "//input[@ng-reflect-name='minute']")
    private WebElement scheduleMinuteInput;

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


    public boolean isVisibleNoDriverFound(){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOf(driverNotFoundToast));

        return webElement != null;
    }

    public boolean isVisibleInvalidChosenTime(){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOf(invalidChosenTimeToast));

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

    public void selectReservationTime(String hour, String minutes) {
        scheduleHourInput.clear();
        scheduleHourInput.sendKeys(hour);
        scheduleHourInput.sendKeys(Keys.ENTER);

        scheduleMinuteInput.clear();
        scheduleMinuteInput.sendKeys(minutes);
        scheduleHourInput.sendKeys(Keys.ENTER);
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

    public boolean isVisibleNoEnoughTokensToast(){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOf(notEnoughTokensToast));

        return webElement != null;
    }

    public boolean isVisibleInvalidNumberOfPassengersToast(){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOf(invalidNumberOfPassengersToast));

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
