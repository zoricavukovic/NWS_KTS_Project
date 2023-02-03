package com.example.serbUber.selenium.tests;

import com.example.serbUber.selenium.helper.LoginHelper;
import com.example.serbUber.selenium.pages.*;
import com.example.serbUber.selenium.tests.bases.OneBrowserTestBase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.serbUber.selenium.helper.Constants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Transactional
public class RideRequestOnePassengerTest extends OneBrowserTestBase {

    @Test
    @DisplayName("T1-Ride request failed, start and end point are not selected")
    public void rideCreationFailedStartAndEndPointNotSelectedTest() {
        LoginHelper.login(chromeDriver, USER_FREE_TO_CREATE_RIDE, EXISTING_PASSWORD);

        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE);
        rideRequestPage.clickOnViewPossibleRoutesButton();
        assertTrue(rideRequestPage.isVisibleEnterLocationsErrorToast(ENTER_LOCATION_ERROR_TOAST_MESSAGE));
    }

    @Test
    @DisplayName("T2-Ride request failed, user don't have enough tokens")
    public void rideCreationFailedUserHaveNotEnoughTokens() {
        LoginHelper.login(chromeDriver, USER_WITH_NO_TOKENS_EMAIL, EXISTING_PASSWORD);

        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE);
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT);
        locations.add(END_POINT);

        for(int i=0; i < locations.size(); i++){
            rideRequestPage.sendDataToLocationInput(i, locations.get(i));
            rideRequestPage.selectFirstLocationOption(WANTED_LOCATION_INDEX);
        }

        assertTrue(rideRequestPage.allLocationsAreSelected());
        rideRequestPage.clickOnViewPossibleRoutesButton();
        rideRequestPage.clickOnRequestNowButton();

        SelectPassengersAndVehicleRideRequestPage selectRideDetailsPage = new SelectPassengersAndVehicleRideRequestPage(chromeDriver);
        selectRideDetailsPage.clickOnVanVehicleType();
        selectRideDetailsPage.clickOnRequestRideButton();
        assertTrue(selectRideDetailsPage.isVisibleNoEnoughTokensToast(NOT_ENOUGH_TOKENS_TOAST_MESSAGE));
    }

    @Test
    @DisplayName("T3-Ride request failed, there is no available driver")
    public void rideCreationFailedThereIsNoAvailableDriver() {
        LoginHelper.login(chromeDriver, USER_FREE_TO_CREATE_RIDE, EXISTING_PASSWORD);

        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE);
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT);
        locations.add(END_POINT);

        for(int i=0; i < locations.size(); i++){
            rideRequestPage.sendDataToLocationInput(i, locations.get(i));
            rideRequestPage.selectFirstLocationOption(WANTED_LOCATION_INDEX);
        }

        assertTrue(rideRequestPage.allLocationsAreSelected());
        rideRequestPage.clickOnViewPossibleRoutesButton();
        rideRequestPage.clickOnRequestNowButton();

        SelectPassengersAndVehicleRideRequestPage selectRideDetailsPage = new SelectPassengersAndVehicleRideRequestPage(chromeDriver);
        selectRideDetailsPage.clickOnBabySeatCheckBox();
        selectRideDetailsPage.clickOnSuvVehicleType();
        selectRideDetailsPage.clickOnRequestRideButton();
        assertTrue(selectRideDetailsPage.isVisibleNoDriverFound(NOT_DRIVER_FOUND_TOAST_MESSAGE));
    }

    @Test
    @DisplayName("T4-Ride request failed, there is more passengers than number of seats")
    public void rideCreationFailedMorePassengersThanNumberOfSeatsDriver() {
        LoginHelper.login(chromeDriver, USER_FREE_TO_CREATE_RIDE, EXISTING_PASSWORD);

        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE);
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT);
        locations.add(END_POINT);

        for(int i=0; i < locations.size(); i++){
            rideRequestPage.sendDataToLocationInput(i, locations.get(i));
            rideRequestPage.selectFirstLocationOption(WANTED_LOCATION_INDEX);
        }

        assertTrue(rideRequestPage.allLocationsAreSelected());
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
        assertTrue(selectRideDetailsPage.isVisibleInvalidNumberOfPassengersToast(INVALID_NUMBER_OF_PASSENGERS_TOAST_MESSAGE));
    }



    @ParameterizedTest
    @DisplayName("T5-Ride reservation failed, chosen time is out of limits")
    @ValueSource(ints = {0,5,6})
    public void rideReservationFailedTimeOutOfLimits(int hours) throws InterruptedException {
        LoginHelper.login(chromeDriver, USER_FREE_TO_CREATE_RIDE, EXISTING_PASSWORD);
        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE);
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT);
        locations.add(END_POINT);

        for(int i=0; i < locations.size(); i++){
            rideRequestPage.sendDataToLocationInput(i, locations.get(i));
            rideRequestPage.selectFirstLocationOption(WANTED_LOCATION_INDEX);
        }

        assertTrue(rideRequestPage.allLocationsAreSelected());
        rideRequestPage.clickOnViewPossibleRoutesButton();
        rideRequestPage.scrollRouteDiv();
        rideRequestPage.clickOnRequestLaterButton();

        SelectPassengersAndVehicleRideRequestPage selectRideDetailsPage = new SelectPassengersAndVehicleRideRequestPage(chromeDriver);
        selectRideDetailsPage.clickOnSuvVehicleType();
        if (hours > 0) {
            assertTrue(selectRideDetailsPage.increaseTime(hours));
        } else {
            assertTrue(selectRideDetailsPage.setTimeForInvalidReservation(INCREASE_MINUTES));
        }

        selectRideDetailsPage.clickOnRequestRideButton();
        Assertions.assertTrue(selectRideDetailsPage.isVisibleInvalidChosenTime(INVALID_CHOSEN_TIME_TOAST_MESSAGE));
    }

    @Test
    @DisplayName("T6-Ride created successfully with one passenger and two locations chosen van")
    public void rideCreatedSuccessfullyWithOnePassengerAndTwoLocationsChosenVanTest() {
        LoginHelper.login(chromeDriver, USER_WITH_TEN_TOKENS_ONE_PASSENGER, EXISTING_PASSWORD);
        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        assertTrue(rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE));
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT_TWO_ROUTES);
        locations.add(END_POINT_TWO_ROUTES);

        for(int i=0; i < locations.size(); i++){
            rideRequestPage.sendDataToLocationInput(i, locations.get(i));
            rideRequestPage.selectFirstLocationOption(WANTED_LOCATION_INDEX);
        }

        assertTrue(rideRequestPage.allLocationsAreSelected());
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
    }

    @ParameterizedTest
    @DisplayName("T7-Reservation ride created successfully with one passenger and two locations chosen car")
    @ValueSource(ints = {1,4})
    public void reservationRideCreatedSuccessfully(int addHours) throws InterruptedException {
        HomePage homePage = LoginHelper.login(chromeDriver, USER_WITH_NO_TOKENS_EMAIL, EXISTING_PASSWORD);
        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        assertTrue(rideRequestPage.isRequestFormPresent(REQUEST_RIDE_TITLE));
        List<String> locations = new ArrayList<>();
        locations.add(START_POINT);
        locations.add(END_POINT);

        for(int i=0; i < locations.size(); i++){
            rideRequestPage.sendDataToLocationInput(i, locations.get(i));
            rideRequestPage.selectFirstLocationOption(WANTED_LOCATION_INDEX);
        }

        assertTrue(rideRequestPage.allLocationsAreSelected());
        rideRequestPage.clickOnViewPossibleRoutesButton();
        rideRequestPage.scrollRouteDiv();
        rideRequestPage.clickOnRequestLaterButton();

        SelectPassengersAndVehicleRideRequestPage rideDetailsPage = new SelectPassengersAndVehicleRideRequestPage(chromeDriver);
        assertTrue(rideDetailsPage.isFilterVehicleDiv());
        rideDetailsPage.clickOnCarVehicleType();
        assertTrue(rideDetailsPage.increaseTime(addHours));
        rideDetailsPage.clickOnRequestRideButton();

        assertTrue(homePage.isVisibleSuccessfullyReservationRideToast(RIDE_RESERVATION_CREATED_MESSAGE));
    }

}
