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

public class FacebookLoginPage {

    private WebDriver driver;

    @FindBy(how = How.ID, using="email")
    private WebElement emailInput;

    @FindBy(how = How.ID, using="pass")
    private WebElement passwordInput;

    @FindBy(how = How.NAME, using="login")
    private WebElement loginButton;

    @FindBy(how = How.ID, using = "facebook-img")
    private WebElement facebookButton;

    private Actions actions;

    public FacebookLoginPage(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
        this.actions = new Actions(driver);
    }

    public void clickOnSignInButton(){
        new WebDriverWait(driver, Duration.ofSeconds(3))
            .until(ExpectedConditions.elementToBeClickable(facebookButton));
        facebookButton.click();
    }

    public void setEmail(String email) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOf(emailInput));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void setPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickOnLoginButton(){
        new WebDriverWait(driver, Duration.ofSeconds(2))
            .until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }

    public boolean isFacebookPageLoaded(String facebookWelcomeMessage){
        WebElement visibleBanner = new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format("//span[contains(text(), '%s')]", facebookWelcomeMessage))
            ));

        return visibleBanner != null;
    }
}
