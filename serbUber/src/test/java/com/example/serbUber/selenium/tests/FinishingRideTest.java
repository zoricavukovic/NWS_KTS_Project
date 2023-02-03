package com.example.serbUber.selenium.tests;

import com.example.serbUber.selenium.pages.DriverActiveRidesPage;
import com.example.serbUber.selenium.pages.HomePage;
import com.example.serbUber.selenium.tests.bases.TwoBrowserTestBase;
import org.junit.jupiter.api.*;
import com.example.serbUber.selenium.helper.LoginHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static com.example.serbUber.selenium.helper.Constants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Transactional
public class FinishingRideTest extends TwoBrowserTestBase {

    @Test
    @DisplayName("T1-Success finished ride, driver doesn't have next ride")
    @Rollback(true)
    public void finishingRideNotHavingNextRideSuccessfulTest() {
        LoginHelper.redirectToLoginPage(chromeDriver);

        HomePage homePageEdge = LoginHelper.redirectToLoginPage(edgeDriver);

        LoginHelper.loginWhenRedirectedOnLoginPage(edgeDriver, EXISTING_REGULAR_USER_EMAIL, EXISTING_PASSWORD);
        LoginHelper.loginWhenRedirectedOnLoginPage(chromeDriver, DRIVER_EMAIL_HAS_CURRENT_NOT_NEXT_RIDE, EXISTING_PASSWORD);

        DriverActiveRidesPage driverActiveRidesPage = new DriverActiveRidesPage(chromeDriver);
        driverActiveRidesPage.clickOnFinishRideButton();
        assertTrue(driverActiveRidesPage.isDriverDoesntHaveActiveOrFutureRides(NO_ACTIVE_OR_FUTURE_MESSAGE));

        assertTrue(homePageEdge.isFinishedRide(FINISHED_RIDE_TITLE));
    }

    @Test
    @DisplayName("T2-Success finished ride, driver has next ride")
    @Rollback(value = true)
    public void finishingRideDriverHasNextRideSuccessfulTest() {
        LoginHelper.redirectToLoginPage(chromeDriver);

        HomePage homePageEdge = LoginHelper.redirectToLoginPage(edgeDriver);

        LoginHelper.loginWhenRedirectedOnLoginPage(edgeDriver, EXISTING_REGULAR_USER_EMAIL_OF_DRIVER_THAT_HAS_NOW_AND_FUTURE_DRIVING, EXISTING_PASSWORD);
        LoginHelper.loginWhenRedirectedOnLoginPage(chromeDriver, DRIVER_EMAIL_HAS_CURRENT_AND_NEXT_RIDE, EXISTING_PASSWORD);

        DriverActiveRidesPage driverActiveRidesPage = new DriverActiveRidesPage(chromeDriver);
        driverActiveRidesPage.clickOnFinishRideButton();
        assertTrue(driverActiveRidesPage.isStartButtonPresent(STARTING_RIDE_TILE));

        assertTrue(homePageEdge.isFinishedRide(FINISHED_RIDE_TITLE));
    }
}
