package com.example.serbUber.selenium.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;

    @FindBy(how = How.ID, using="submit-login")
    private WebElement loginButton;

    @FindBy(how = How.NAME, using="email")
    private WebElement emailInput;

    @FindBy(how = How.NAME, using="password")
    private WebElement passwordInput;

    @FindBy(how = How.XPATH, using="//h1")
    private WebElement title;

    @FindBy(how = How.XPATH, using = "//div[contains(@aria-label, 'Email or password is not correct!')]")
    private WebElement errorToastLabel;

    public LoginPage(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    public boolean isPageLoaded(String titleOfLoginPage){
        return new WebDriverWait(driver, Duration.ofSeconds(2))
            .until(ExpectedConditions.textToBePresentInElement(title, titleOfLoginPage));
    }

    public void setEmail(String email) {
        emailInput.clear();
        emailInput.sendKeys(email);
        emailInput.sendKeys(Keys.RETURN);
    }

    public void setPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
        passwordInput.sendKeys(Keys.RETURN);
    }

    public void clickOnLoginButton(){
        new WebDriverWait(driver, Duration.ofSeconds(2))
            .until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }

    public boolean isVisibleErrorToast(){
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(2))
            .until(ExpectedConditions.visibilityOf(errorToastLabel));

        return webElement != null;
    }
}
