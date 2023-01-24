package com.example.serbUber.selenium.tests;

import com.example.serbUber.selenium.pages.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.example.serbUber.selenium.helper.Constants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginTest extends TestBase {

    @Test
    @DisplayName("T1-Success login with valid credentials")
    public void regularLoginSuccessfulTest() {
        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isPageLoaded());
        homePage.clickOnLoginButton();

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageLoaded());
        loginPage.setEmail(EXISTING_REGULAR_USER_EMAIL);
        loginPage.setPassword(EXISTING_PASSWORD);
        loginPage.clickOnLoginButton();

        homePage.clickOnProfileIconMenuButton();
        homePage.clickOnMyProfileMenuOption();

        UserProfilePage userProfilePage = new UserProfilePage(driver);
        assertTrue(userProfilePage.isProfileOfSpecificUser(EXISTING_REGULAR_USER_EMAIL));

    }

    @ParameterizedTest
    @DisplayName("T2-Login failed with wrong credentials(not existing)")
    @MethodSource(value = "sourceWrongCredentials")
    public void regularLoginShouldFailedInvalidEmailTest(String email, String password) {
        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isPageLoaded());
        homePage.clickOnLoginButton();

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageLoaded());
        loginPage.setEmail(email);
        loginPage.setPassword(password);
        loginPage.clickOnLoginButton();

        assertTrue(loginPage.isVisibleErrorToast());
    }

    List<Arguments> sourceWrongCredentials(){

        return Arrays.asList(arguments(NOT_EXISTING_USER_EMAIL, EXISTING_PASSWORD),
            arguments(EXISTING_REGULAR_USER_EMAIL, NOT_VALID_PASSWORD));
    }

    @Test
    @DisplayName("T3-Success login with Google")
    public void googleLoginSuccessfulTest() {
        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isPageLoaded());
        homePage.clickOnLoginButton();

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageLoaded());
        GoogleLoginPage googleLoginPage = new GoogleLoginPage(driver);
        googleLoginPage.clickOnSignInButton();

        Set<String> windows = driver.getWindowHandles();
        Iterator<String> iterator = windows.iterator();
        String onePage = iterator.next();
        String secondPage = iterator.next();

        driver.switchTo().window(secondPage);

        googleLoginPage.setEmail(EXISTING_EMAIL_GOOGLE);
        googleLoginPage.clickOnNextButton();
        googleLoginPage.setPassword(EXISTING_PASSWORD_GOOGLE_AND_FACEBOOK);
        googleLoginPage.clickOnNextButton();

        driver.switchTo().window(onePage);
        homePage.clickOnProfileIconMenuButton();
        homePage.clickOnMyProfileMenuOption();

        UserProfilePage userProfilePage = new UserProfilePage(driver);
        assertTrue(userProfilePage.isProfileOfSpecificUser(EXISTING_EMAIL_GOOGLE));
    }

    @Test
    @DisplayName("T4-Success login with Facebook")
    public void facebookLoginSuccessfulTest() throws InterruptedException {
        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isPageLoaded());
        homePage.clickOnLoginButton();

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageLoaded());

        FacebookLoginPage facebookLoginPage = new FacebookLoginPage(driver);
        facebookLoginPage.clickOnSignInButton();

        Set<String> windows = driver.getWindowHandles();
        Iterator<String> iterator = windows.iterator();
        String onePage = iterator.next();
        String secondPage = iterator.next();

        driver.switchTo().window(secondPage);

        facebookLoginPage.setEmail(EXISTING_EMAIL_FACEBOOK);
        facebookLoginPage.setPassword(EXISTING_PASSWORD_GOOGLE_AND_FACEBOOK);
        facebookLoginPage.clickOnLoginButton();

        facebookLoginPage.isFacebookPageLoaded();
        driver.switchTo().window(onePage);
        facebookLoginPage.clickOnSignInButton();    //mora ponovo za facebook

        homePage.clickOnProfileIconMenuButton();
        homePage.clickOnMyProfileMenuOption();

        UserProfilePage userProfilePage = new UserProfilePage(driver);
        assertTrue(userProfilePage.isProfileOfSpecificUser(EXISTING_EMAIL_FACEBOOK));
    }


}
