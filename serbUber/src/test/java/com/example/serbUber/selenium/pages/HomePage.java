package com.example.serbUber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private WebDriver driver;

    private Actions actions;
    private final static String PAGE_URL= "http://localhost:4200/serb-uber/user/map-page-view/-1";

    @FindBy(how = How.ID, using="login")
    private WebElement loginButton;

    @FindBy(how = How.XPATH, using="//h1/strong")
    private WebElement title;

    @FindBy(how = How.XPATH, using = "//img[contains(@class, 'img-circle-small')]")
    private WebElement profileIconMenuButton;

    @FindBy(how = How.XPATH, using = "//button/span[contains(text(), 'My profile')]")
    private WebElement myProfileMenuOption;

    @FindBy(how = How.XPATH, using="//div[contains(@class, 'title')]")
    private WebElement regularUserFinishedRideTitle;

    @FindBy(how = How.XPATH, using = "//div[contains(@aria-label, 'add you as linked passenger.Tap to accept!')]")
    private WebElement acceptRideToast;

    @FindBy(how = How.XPATH, using = "//div[contains(@aria-label, 'Your ride reserved successfully.')]")
    private WebElement successfullyReservationRideToast;


    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.driver.get(PAGE_URL);
        this.actions = new Actions(driver);

        PageFactory.initElements(driver, this);
    }

    public boolean isPageLoaded(String titleOfHomePageContainer){
        return new WebDriverWait(driver, Duration.ofSeconds(3))
            .until(ExpectedConditions.textToBePresentInElement(title, titleOfHomePageContainer));
    }

    public void clickOnLoginButton(){
        new WebDriverWait(driver, Duration.ofSeconds(2))
            .until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }

    public void clickOnProfileIconMenuButton(){
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(profileIconMenuButton));
        profileIconMenuButton.click();
    }

    public void clickOnMyProfileMenuOption(){
        new WebDriverWait(driver, Duration.ofSeconds(2))
            .until(ExpectedConditions.elementToBeClickable(myProfileMenuOption));
        myProfileMenuOption.click();
    }

    public boolean isFinishedRide(String finishedRideTitle){
        return new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.textToBePresentInElement(regularUserFinishedRideTitle, finishedRideTitle));
    }

    public boolean isVisibleAcceptRideToast(){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(acceptRideToast));

        return webElement != null;
    }

    public void clickOnAcceptRideToast(){
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(acceptRideToast));
        actions.moveToElement(acceptRideToast).click().perform();
    }

    public boolean isVisibleSuccessfullyReservationRideToast(){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(successfullyReservationRideToast));

        return webElement != null;
    }
}
