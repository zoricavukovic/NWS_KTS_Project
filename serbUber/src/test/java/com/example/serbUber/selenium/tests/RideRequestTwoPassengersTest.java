package com.example.serbUber.selenium.tests;

import com.example.serbUber.selenium.helper.LoginHelper;
import com.example.serbUber.selenium.pages.*;
import com.example.serbUber.selenium.tests.bases.TwoBrowserTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.serbUber.selenium.helper.Constants.*;
import static com.example.serbUber.selenium.helper.Constants.DRIVER_NAME_RIDE_TWO_LOCATIONS_ONE_PASSENGER;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RideRequestTwoPassengersTest extends TwoBrowserTestBase {

    @Test
    @DisplayName(value = "T1-Ride request failed, linked passenger rejected ride")
    public void rideRequestFailedLinkedPassengerRejectedRide() {
        HomePage homePageSenderPassenger = LoginHelper.redirectToLoginPage(chromeDriver);
        HomePage homePageLinkedPassenger = LoginHelper.redirectToLoginPage(edgeDriver);

        LoginHelper.loginWhenRedirectedOnLoginPage(chromeDriver, USER_FREE_TO_CREATE_RIDE, EXISTING_PASSWORD);
        LoginHelper.loginWhenRedirectedOnLoginPage(edgeDriver, USER_WITH_NO_TOKENS_EMAIL, EXISTING_PASSWORD);

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
        selectRideDetailsPage.clickOnPassengersExpansionPanel();
        selectRideDetailsPage.addLinkedPassenger(USER_WITH_NO_TOKENS_EMAIL);
        selectRideDetailsPage.clickOnFirstPassengerOption();
        selectRideDetailsPage.clickOnRequestRideButton();

        assertTrue(homePageLinkedPassenger.isVisibleAcceptRideToast());
        homePageLinkedPassenger.clickOnAcceptRideToast();
        DrivingNotificationPage drivingNotificationPageLinkedPassenger = new DrivingNotificationPage(edgeDriver);
        drivingNotificationPageLinkedPassenger.clickOnRejectRideButton();
        Assertions.assertTrue(drivingNotificationPageLinkedPassenger.isVisibleRideIsRejectedToast());
    }
    @Test
    @DisplayName("T2-Ride created successfully with two passengers and three locations chosen suv")
    public void rideCreatedSuccessfullyWithTwoPassengersAndThreeLocationsChosenSuvTest() {
        HomePage homePageSenderPassenger = LoginHelper.login(chromeDriver, USER_SENDER, EXISTING_PASSWORD);
        HomePage homePageLinkedPassenger = LoginHelper.login(edgeDriver, LINKED_USER, EXISTING_PASSWORD);

        RideRequestPage rideRequestPage = new RideRequestPage(chromeDriver);
        List<String> locations = new ArrayList<>();
        locations.add("Bulevar Cara Lazara 10");
        locations.add("Bulevar Evrope 20");
        locations.add("Futoski put 103");
        rideRequestPage.enterLocations(locations);
        rideRequestPage.clickOnViewPossibleRoutesButton();
        rideRequestPage.scrollRouteDiv();

        rideRequestPage.clickOnRequestNowButton();

        SelectPassengersAndVehicleRideRequestPage rideDetailsPage = new SelectPassengersAndVehicleRideRequestPage(chromeDriver);

        rideDetailsPage.clickOnVanVehicleType();
        rideDetailsPage.clickOnPassengersExpansionPanel();
        rideDetailsPage.addLinkedPassenger(LINKED_USER);
        rideDetailsPage.clickOnFirstPassengerOption();
        rideDetailsPage.clickOnRequestRideButton();

        assertTrue(homePageLinkedPassenger.isVisibleAcceptRideToast());
        homePageLinkedPassenger.clickOnAcceptRideToast();

        DrivingNotificationPage drivingNotificationPageLinkedPassenger = new DrivingNotificationPage(edgeDriver);
        drivingNotificationPageLinkedPassenger.clickOnAcceptRideButton();

        DrivingDetailsPage drivingDetailsPageSender = new DrivingDetailsPage(chromeDriver);
        drivingDetailsPageSender.isDrivingDetailsPage(DRIVING_DETAILS_TITLE);
        drivingDetailsPageSender.isCorrectDriver(DRIVER_NAME_RIDE_TWO_LOCATIONS_ONE_PASSENGER);

        DrivingDetailsPage drivingDetailsPageLinkedPassenger = new DrivingDetailsPage(edgeDriver);
        drivingDetailsPageLinkedPassenger.isDrivingDetailsPage(DRIVING_DETAILS_TITLE);
        drivingDetailsPageLinkedPassenger.isCorrectDriver(DRIVER_NAME_RIDE_TWO_LOCATIONS_ONE_PASSENGER);
    }

}
