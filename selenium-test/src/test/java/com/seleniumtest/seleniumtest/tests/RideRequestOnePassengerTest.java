package com.seleniumtest.seleniumtest.tests;

import com.seleniumtest.seleniumtest.helper.LoginHelper;
import com.seleniumtest.seleniumtest.pages.DrivingDetailsPage;
import com.seleniumtest.seleniumtest.pages.HomePage;
import com.seleniumtest.seleniumtest.pages.RideRequestPage;
import com.seleniumtest.seleniumtest.pages.SelectPassengersAndVehicleRideRequestPage;
import com.seleniumtest.seleniumtest.tests.bases.OneBrowserTestBase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.seleniumtest.seleniumtest.helper.Constants.*;
import static com.seleniumtest.seleniumtest.helper.Util.getHour;
import static com.seleniumtest.seleniumtest.helper.Util.getMinutes;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
public class RideRequestOnePassengerTest extends OneBrowserTestBase {

    @Test
    @DisplayName("T1-Ride request failed, start and end point are not selected")
    public void rideCreationFailedStartAndEndPointNotSelectedTest() {
        HomePage homePage = LoginHelper.login(chromeDriver, USER_FREE_TO_CREATE_RIDE, EXISTING_PASSWORD);

        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE);
        rideRequestPage.clickOnViewPossibleRoutesButton();
        assertTrue(rideRequestPage.isVisibleEnterLocationsErrorToast());
    }

    @Test
    @DisplayName("T2-Ride request failed, user don't have enough tokens")
    public void rideCreationFailedUserHaveNotEnoughTokens() {
        HomePage homePage = LoginHelper.login(chromeDriver, USER_WITH_NO_TOKENS_EMAIL, EXISTING_PASSWORD);

        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE);
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT);
        locations.add(END_POINT);
        rideRequestPage.enterLocations(locations);
        rideRequestPage.clickOnViewPossibleRoutesButton();
        rideRequestPage.clickOnRequestNowButton();

        SelectPassengersAndVehicleRideRequestPage selectRideDetailsPage = new SelectPassengersAndVehicleRideRequestPage(chromeDriver);
        selectRideDetailsPage.clickOnVanVehicleType();
        selectRideDetailsPage.clickOnRequestRideButton();
        assertTrue(selectRideDetailsPage.isVisibleNoEnoughTokensToast());
    }

    @Test
    @DisplayName("T3-Ride request failed, there is no available driver")
    public void rideCreationFailedThereIsNoAvailableDriver() {
        HomePage homePage = LoginHelper.login(chromeDriver, USER_FREE_TO_CREATE_RIDE, EXISTING_PASSWORD);

        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE);
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT);
        locations.add(END_POINT);
        rideRequestPage.enterLocations(locations);
        rideRequestPage.clickOnViewPossibleRoutesButton();
        rideRequestPage.clickOnRequestNowButton();

        SelectPassengersAndVehicleRideRequestPage selectRideDetailsPage = new SelectPassengersAndVehicleRideRequestPage(chromeDriver);
        selectRideDetailsPage.clickOnSuvVehicleType();
        selectRideDetailsPage.clickOnRequestRideButton();
        assertTrue(selectRideDetailsPage.isVisibleNoDriverFound());
    }

    @Test
    @DisplayName("T4-Ride request failed, there is more passengers than number of seats")
    public void rideCreationFailedMorePassengersThanNumberOfSeatsDriver() {
        HomePage homePage = LoginHelper.login(chromeDriver, USER_FREE_TO_CREATE_RIDE, EXISTING_PASSWORD);

        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE);
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT);
        locations.add(END_POINT);
        rideRequestPage.enterLocations(locations);
        rideRequestPage.clickOnViewPossibleRoutesButton();
        rideRequestPage.clickOnRequestNowButton();

        SelectPassengersAndVehicleRideRequestPage selectRideDetailsPage = new SelectPassengersAndVehicleRideRequestPage(chromeDriver);
        selectRideDetailsPage.clickOnSuvVehicleType();
        selectRideDetailsPage.clickOnPassengersExpansionPanel();
        selectRideDetailsPage.clickOnAddPassengerInput();
        for(int i=0; i<MAX_NUMBER_OF_SEATS_FOR_SUV; i++){
            selectRideDetailsPage.clickOnAddPassengerInput();
            selectRideDetailsPage.clickOnFirstPassengerOption();
        }
        assertTrue(selectRideDetailsPage.isVisibleInvalidNumberOfPassengersToast());
    }



    @ParameterizedTest
    @DisplayName("T5-Ride reservation failed, chosen time is out of limits")
    @MethodSource(value = "getNotValidTimeForReservation")
    public void rideReservationFailedTimeOutOfLimits(String hour, String minutes) {
        HomePage homePage = LoginHelper.login(chromeDriver, USER_FREE_TO_CREATE_RIDE, EXISTING_PASSWORD);
        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE);
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT);
        locations.add(END_POINT);
        rideRequestPage.enterLocations(locations);
        rideRequestPage.clickOnViewPossibleRoutesButton();

        SelectPassengersAndVehicleRideRequestPage selectRideDetailsPage = new SelectPassengersAndVehicleRideRequestPage(chromeDriver);
        selectRideDetailsPage.clickOnSuvVehicleType();
        selectRideDetailsPage.selectReservationTime(hour, minutes);

        selectRideDetailsPage.clickOnRequestRideButton();
        assertTrue(selectRideDetailsPage.isVisibleInvalidChosenTime());
    }

    @Test
    @DisplayName("T6-Ride created successfully with one passenger and two locations chosen van")
    public void rideCreatedSuccessfullyWithOnePassengerAndTwoLocationsChosenVanTest() {
        HomePage homePage = LoginHelper.login(chromeDriver, USER_WITH_TEN_TOKENS_ONE_PASSENGER, EXISTING_PASSWORD);
        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        assertTrue(rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE));
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT_TWO_ROUTES);
        locations.add(END_POINT_TWO_ROUTES);
        rideRequestPage.enterLocations(locations);
        rideRequestPage.clickOnViewPossibleRoutesButton();
        rideRequestPage.scrollRouteDiv();
        rideRequestPage.clickOnSecondRouteButton();
        rideRequestPage.clickOnSecondRouteDiv();
        rideRequestPage.clickOnRequestNowButton();

        SelectPassengersAndVehicleRideRequestPage rideDetailsPage = new SelectPassengersAndVehicleRideRequestPage(chromeDriver);
        assertTrue(rideDetailsPage.isFilterVehicleDiv());
        rideDetailsPage.clickOnBabySeatCheckBox();
        rideDetailsPage.clickOnVanVehicleType();
        rideDetailsPage.clickOnRequestRideButton();

        DrivingDetailsPage drivingDetailsPage = new DrivingDetailsPage(chromeDriver);
        assertTrue(drivingDetailsPage.isDrivingDetailsPage(DRIVING_DETAILS_TITLE));
        assertTrue(drivingDetailsPage.isCorrectDriver(DRIVER_NAME_RIDE_TWO_LOCATIONS_ONE_PASSENGER));
    }

    @ParameterizedTest
    @DisplayName("T7-Reservation ride created successfully with one passenger and two locations chosen car")
    @MethodSource(value = "getValidTimeForReservation")
    public void reservationRideCreatedSuccessfully(String hour, String minutes){
        HomePage homePage = LoginHelper.login(chromeDriver, USER_WITH_NO_TOKENS_EMAIL, EXISTING_PASSWORD);
        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        assertTrue(rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE));
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT);
        locations.add(END_POINT);
        rideRequestPage.enterLocations(locations);
        rideRequestPage.clickOnViewPossibleRoutesButton();
        rideRequestPage.scrollRouteDiv();
        rideRequestPage.clickOnRequestLaterButton();

        SelectPassengersAndVehicleRideRequestPage rideDetailsPage = new SelectPassengersAndVehicleRideRequestPage(chromeDriver);
        assertTrue(rideDetailsPage.isFilterVehicleDiv());
        rideDetailsPage.clickOnCarVehicleType();
        rideDetailsPage.selectReservationTime(hour, minutes);
        rideDetailsPage.clickOnRequestRideButton();

        assertTrue(homePage.isVisibleSuccessfullyReservationRideToast());
    }


    private List<Arguments> getNotValidTimeForReservation(){

        return Arrays.asList(arguments(getHour(6, 0), getMinutes(6, 0)),
                arguments(getHour(5, 2), getMinutes(5,2)),
                arguments(getHour(0, 28), getMinutes(0, 28)));
    }


    private List<Arguments> getValidTimeForReservation(){

        return Arrays.asList(
                arguments(getHour(1,0), getMinutes(30, 0)),
                arguments(getHour(1,0), getMinutes(0,0)),
                arguments(getHour(4,0), getMinutes(30,0))
        );

    }


}
