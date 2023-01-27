package com.example.serbUber.selenium.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RideRequestPage {

    private WebDriver driver;

    private Actions actions;

    @FindBy(how = How.XPATH, using="//h1/strong")
    private WebElement requestRideTitle;

    @FindBy(how = How.XPATH, using="//input[@formControlName='inputPlace']")
    private List<WebElement> locationInputFields;

    @FindBy(how=How.ID, using="add-location-btn")
    private WebElement addLocationButton;

    @FindBy(how=How.ID, using="view-possible-routes")
    private WebElement viewPossibleRoutesButton;

    @FindBy(how = How.ID, using = "request-now-btn")
    private WebElement requestNowButton;

    @FindBy(how = How.ID, using = "request-later-btn")
    private WebElement requestLaterButton;

    @FindBy(how = How.ID, using = "route-div")
    private WebElement routeDiv;

    @FindBy(how = How.CSS, using = ".pac-item-query")
    private List<WebElement> placesAutocompleteOptions;

    @FindBy(how = How.XPATH, using="//p-carousel/div/div/div/button[2]")
    private WebElement secondRouteButton;

    @FindBy(how = How.ID, using="possible-route")
    private WebElement secondRouteDiv;

    @FindBy(how = How.XPATH, using = "//div[contains(@aria-label, 'Invalid form')]")
    private WebElement errorToastNotSelectedLocation;

    public RideRequestPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean isRequestFormPresent(String requestRideLabel){
        return new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.textToBePresentInElement(requestRideTitle, requestRideLabel));
    }

    public boolean isVisibleEnterLocationsErrorToast(){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOf(errorToastNotSelectedLocation));

        return webElement != null;
    }

    public void enterLocations(List<String> locations) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(locationInputFields.get(0)));
        if(locations.size() > 2){
            addMoreLocations(locations.size() - 2);
        }

        for(int i=0; i<locations.size(); i++){
            locationInputFields.get(i).clear();
            locationInputFields.get(i).sendKeys(locations.get(i));
            this.selectFirstLocationOption();
        }
    }

    public void addMoreLocations(int numberOfLocations){
        int i = 0;
        while(i < numberOfLocations){
            clickOnAddToLocationButton();
            i += 1;
        }
    }

    public void clickOnAddToLocationButton(){
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(addLocationButton));
        addLocationButton.click();
    }

    public void clickOnViewPossibleRoutesButton(){
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(viewPossibleRoutesButton));
        viewPossibleRoutesButton.click();
    }

    public void clickOnRequestNowButton(){
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(requestNowButton));
        requestNowButton.click();
    }

    public void clickOnRequestLaterButton(){
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(requestLaterButton));
        requestLaterButton.click();
    }

    public void scrollRouteDiv(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", routeDiv);
    }

    private void selectFirstLocationOption() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfAllElements(placesAutocompleteOptions));

        this.actions.moveToElement(placesAutocompleteOptions.get(0)).click().perform();
    }

    public void clickOnSecondRouteButton(){
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(secondRouteButton));
        secondRouteButton.click();
    }

    public void clickOnSecondRouteDiv(){
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(secondRouteDiv));
        actions.moveToElement(secondRouteDiv).click().perform();
    }
}
