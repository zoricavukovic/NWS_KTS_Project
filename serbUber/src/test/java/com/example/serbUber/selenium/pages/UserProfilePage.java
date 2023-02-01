package com.example.serbUber.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UserProfilePage {
    private WebDriver driver;

    @FindBy(how = How.XPATH, using="//label[contains(@class, 'email-label')]")
    private WebElement userEmail;

    public UserProfilePage(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    public boolean isProfileOfSpecificUser(String email){
        return new WebDriverWait(driver, Duration.ofSeconds(2))
            .until(ExpectedConditions.textToBePresentInElement(userEmail, email));
    }
}
