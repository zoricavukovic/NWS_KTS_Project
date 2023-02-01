package com.example.serbUber.selenium.pages;

import org.openqa.selenium.By;
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

    @FindBy(how = How.XPATH, using="//div[contains(@class, 'title')]")
    private WebElement regularUserFinishedRideTitle;

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

    public void clickOnMyProfileMenuOption(String label){
        WebElement myProfileMenuOption = new WebDriverWait(driver, Duration.ofSeconds(2))
            .until(ExpectedConditions.elementToBeClickable(By.xpath(String.format("//button/span[contains(text(), '%s')]", label))));
        myProfileMenuOption.click();
    }

    public boolean isFinishedRide(String finishedRideTitle){
        return new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.textToBePresentInElement(regularUserFinishedRideTitle, finishedRideTitle));
    }

    public boolean isVisibleAcceptRideToast(String acceptRideMessage){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@aria-label, '%s')]", acceptRideMessage))));

        return webElement != null;
    }

    public void clickOnAcceptRideToast(String acceptRideMessage){
        WebElement acceptRideToast = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@aria-label, '%s')]", acceptRideMessage))));
        actions.moveToElement(acceptRideToast).click().perform();
    }

    public boolean isVisibleSuccessfullyReservationRideToast(String successfulReservationMessage){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@aria-label, '%s')]", successfulReservationMessage))));

        return webElement != null;
    }
}
