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

import java.time.Duration;

public class GoogleLoginPage {

    private WebDriver driver;

    @FindBy(how = How.TAG_NAME, using="asl-google-signin-button")
    private WebElement signInButton;

    @FindBy(how = How.XPATH, using="//input[@type='email']")
    private WebElement emailInput;

    @FindBy(how = How.XPATH, using="//span[text()='Next']/parent::button")
    private WebElement nextButton;

    @FindBy(how = How.XPATH, using="//input[@type='password']")
    private WebElement passwordInput;

    private Actions actions;

    public GoogleLoginPage(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
        this.actions = new Actions(driver);
    }

    public void clickOnSignInButton(){
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.elementToBeClickable(signInButton));
        signInButton.click();
    }

    public void setEmail(String email) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOf(emailInput));
        emailInput.clear();
        emailInput.sendKeys(email);
        emailInput.sendKeys(Keys.RETURN);
    }

    public void setPassword(String password) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOf(passwordInput));
        passwordInput.clear();
        passwordInput.sendKeys(password);
        passwordInput.sendKeys(Keys.RETURN);
    }

    public void clickOnNextButton(){
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.elementToBeClickable(nextButton));
        actions.click(nextButton).perform();
    }
}
