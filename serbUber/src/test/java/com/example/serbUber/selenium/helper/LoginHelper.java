package com.example.serbUber.selenium.helper;

import com.example.serbUber.selenium.pages.HomePage;
import com.example.serbUber.selenium.pages.LoginPage;
import org.openqa.selenium.WebDriver;

import static com.example.serbUber.selenium.helper.Constants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginHelper {

    public static HomePage login(WebDriver driver, String email, String password) {
        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isPageLoaded(TITLE_OF_HOME_PAGE_CONTAINER));
        homePage.clickOnLoginButton();

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageLoaded(TITLE_OF_LOGIN_PAGE));
        loginPage.setEmail(email);
        loginPage.setPassword(password);
        loginPage.clickOnLoginButton();

        return homePage;
    }

    public static HomePage redirectToLoginPage(WebDriver driver) {
        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isPageLoaded(TITLE_OF_HOME_PAGE_CONTAINER));
        homePage.clickOnLoginButton();

        return homePage;
    }

    public static LoginPage loginWhenRedirectedOnLoginPage(WebDriver driver, String email, String password) {
        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageLoaded(TITLE_OF_LOGIN_PAGE));
        loginPage.setEmail(email);
        loginPage.setPassword(password);
        loginPage.clickOnLoginButton();

        return loginPage;
    }

}
