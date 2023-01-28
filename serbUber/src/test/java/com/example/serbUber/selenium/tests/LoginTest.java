package com.example.serbUber.selenium.tests;

import com.example.serbUber.selenium.helper.LoginHelper;
import com.example.serbUber.selenium.pages.*;
import com.example.serbUber.selenium.tests.bases.TestBase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.example.serbUber.selenium.helper.Constants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginTest extends TestBase {

    private HomePage homePage;

    @Test
    @DisplayName("T1-Success login with valid credentials")
    public void regularLoginSuccessfulTest() {
        homePage = LoginHelper.login(driver, EXISTING_REGULAR_USER_EMAIL, EXISTING_PASSWORD);
        homePage.clickOnProfileIconMenuButton();
        homePage.clickOnMyProfileMenuOption();

        UserProfilePage userProfilePage = new UserProfilePage(driver);
        assertTrue(userProfilePage.isProfileOfSpecificUser(EXISTING_REGULAR_USER_EMAIL));
    }

    @ParameterizedTest
    @DisplayName("T2-Login failed with wrong credentials(not existing)")
    @MethodSource(value = "sourceWrongCredentials")
    public void regularLoginShouldFailedWrongCredentialsTest(String email, String password) {
        LoginHelper.login(driver, email, password);

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isVisibleErrorToast(FAILED_LOGIN_MESSAGE));
    }

    List<Arguments> sourceWrongCredentials(){

        return Arrays.asList(arguments(NOT_EXISTING_USER_EMAIL, EXISTING_PASSWORD),
            arguments(EXISTING_REGULAR_USER_EMAIL, NOT_VALID_PASSWORD));
    }

    @ParameterizedTest
    @DisplayName("T3-Login failed, email/password are empty")
    @MethodSource(value = "sourceEmptyCredentials")
    public void regularLoginShouldFailedEmptyCredentialsTest(String email, String password, String message) {
        LoginHelper.login(driver, email, password);

        LoginPage loginPage = new LoginPage(driver);
        assertTrue((email.equals(EMPTY_FIELD) && password.equals(EMPTY_FIELD)) ? loginPage.isVisibleMatErrorForAllFields(EMAIL_IS_REQUIRED_MESSAGE, PASSWORD_IS_REQUIRED_MESSAGE)
            : loginPage.isVisibleMatError(message));
    }

    List<Arguments> sourceEmptyCredentials(){

        return Arrays.asList(arguments(EMPTY_FIELD, EMPTY_FIELD, EMAIL_IS_REQUIRED_MESSAGE),
            arguments(EXISTING_REGULAR_USER_EMAIL, EMPTY_FIELD, PASSWORD_IS_REQUIRED_MESSAGE),
            arguments(EMPTY_FIELD, EXISTING_PASSWORD, EMAIL_IS_REQUIRED_MESSAGE));
    }

    @Test
    @DisplayName("T4-Success login with Google")
    public void googleLoginSuccessfulTest() {
        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isPageLoaded(TITLE_OF_HOME_PAGE_CONTAINER));
        homePage.clickOnLoginButton();

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageLoaded(TITLE_OF_LOGIN_PAGE));
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
    @DisplayName("T5-Success login with Facebook")
    public void facebookLoginSuccessfulTest() {
        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isPageLoaded(TITLE_OF_HOME_PAGE_CONTAINER));
        homePage.clickOnLoginButton();

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageLoaded(TITLE_OF_LOGIN_PAGE));

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

        assertTrue(facebookLoginPage.isFacebookPageLoaded(FACEBOOK_TITLE));
        driver.switchTo().window(onePage);
        facebookLoginPage.clickOnSignInButton();    //mora ponovo za facebook

        homePage.clickOnProfileIconMenuButton();
        homePage.clickOnMyProfileMenuOption();

        UserProfilePage userProfilePage = new UserProfilePage(driver);
        assertTrue(userProfilePage.isProfileOfSpecificUser(EXISTING_EMAIL_FACEBOOK));
    }
}
