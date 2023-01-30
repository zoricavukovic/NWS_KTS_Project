package com.seleniumtest.seleniumtest.tests;

import com.seleniumtest.seleniumtest.helper.LoginHelper;
import com.seleniumtest.seleniumtest.pages.DriverActiveRidesPage;
import com.seleniumtest.seleniumtest.pages.HomePage;
import com.seleniumtest.seleniumtest.tests.bases.TwoBrowserTestBase;
import org.junit.jupiter.api.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static com.seleniumtest.seleniumtest.helper.Constants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Transactional
public class FinishingRideTest extends TwoBrowserTestBase {

    @Test
//    @Rollback(true)
    @DisplayName("T1-Success finished ride, driver doesn't have next ride")
    public void finishingRideNotHavingNextRideSuccessfulTest() {
        //driver login on chrome driver
        HomePage homePageChrome = LoginHelper.redirectToLoginPage(chromeDriver);

        //user with active driving login on edge driver
        HomePage homePageEdge = LoginHelper.redirectToLoginPage(edgeDriver);

        LoginHelper.loginWhenRedirectedOnLoginPage(chromeDriver, DRIVER_EMAIL_HAS_CURRENT_NOT_NEXT_RIDE, EXISTING_PASSWORD);
        LoginHelper.loginWhenRedirectedOnLoginPage(edgeDriver, EXISTING_REGULAR_USER_EMAIL, EXISTING_PASSWORD);

        //driver finished ride on chrome driver
        DriverActiveRidesPage driverActiveRidesPage = new DriverActiveRidesPage(chromeDriver);
        driverActiveRidesPage.clickOnFinishRideButton();
        assertTrue(driverActiveRidesPage.isDriverDoesntHaveActiveOrFutureRides(NO_ACTIVE_OR_FUTURE_MESSAGE));

        //now, user doesn't have active ride on edge driver
        assertTrue(homePageEdge.isFinishedRide(FINISHED_RIDE_TITLE));
    }

    @Test
//    @Rollback(true)
    @DisplayName("T2-Success finished ride, driver has next ride")
    public void finishingRideDriverHasNextRideSuccessfulTest() {
        //driver login on chrome driver
        HomePage homePageChrome = LoginHelper.redirectToLoginPage(chromeDriver);

        //user with active driving login on edge driver
        HomePage homePageEdge = LoginHelper.redirectToLoginPage(edgeDriver);

        LoginHelper.loginWhenRedirectedOnLoginPage(chromeDriver, DRIVER_EMAIL_HAS_CURRENT_AND_NEXT_RIDE, EXISTING_PASSWORD);
        LoginHelper.loginWhenRedirectedOnLoginPage(edgeDriver, EXISTING_REGULAR_USER_EMAIL_OF_DRIVER_THAT_HAS_NOW_AND_FUTURE_DRIVING, EXISTING_PASSWORD);

        //driver finished ride on chrome driver
        DriverActiveRidesPage driverActiveRidesPage = new DriverActiveRidesPage(chromeDriver);
        driverActiveRidesPage.clickOnFinishRideButton();
        assertTrue(driverActiveRidesPage.isStartButtonPresent(STARTING_RIDE_TILE));

        //now, user doesn't have active ride on edge driver
        assertTrue(homePageEdge.isFinishedRide(FINISHED_RIDE_TITLE));
    }
}
