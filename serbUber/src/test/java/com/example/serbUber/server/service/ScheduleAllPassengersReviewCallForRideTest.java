package com.example.serbUber.server.service;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.PassengerNotHaveTokensException;
import com.example.serbUber.service.DrivingNotificationService;
import com.example.serbUber.service.ScheduleAllPassengersReviewCallForRide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.example.serbUber.server.service.helper.Constants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScheduleAllPassengersReviewCallForRideTest {

    @Mock
    private DrivingNotificationService drivingNotificationService;

    @InjectMocks
    private ScheduleAllPassengersReviewCallForRide allPassengersReviewCallForRide;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("T1-Should delete one outdated")
    public void shouldDeleteOneOutdatedWhenReviewCallForRide() throws PassengerNotHaveTokensException, EntityNotFoundException {

        when(drivingNotificationService.getAllNotReservation()).thenReturn(
                Arrays.asList(NOT_RESERVATION_DRIVING_NOTIFICATION_OUTDATED, NOT_RESERVATION_DRIVING_NOTIFICATION)
        );

        when(drivingNotificationService.checkIfDrivingNotificationIsOutdated(NOT_RESERVATION_DRIVING_NOTIFICATION_OUTDATED))
                .thenReturn(true);

        when(drivingNotificationService.checkIfDrivingNotificationIsOutdated(NOT_RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(false);

        when(drivingNotificationService.checkIfUsersReviewed(NOT_RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(false);

        doNothing().when(drivingNotificationService).deleteOutdatedNotification(NOT_RESERVATION_DRIVING_NOTIFICATION_OUTDATED);
        allPassengersReviewCallForRide.allPassengersReviewCallForRide();

        verify(drivingNotificationService, times(1)).deleteOutdatedNotification(NOT_RESERVATION_DRIVING_NOTIFICATION_OUTDATED);
    }

    @Test
    @DisplayName("T2-Should throw PassengerNotHaveTokensException")
    public void shouldThrowPassengerNotHaveTokensExceptionWhenReviewCallForRide() throws PassengerNotHaveTokensException, EntityNotFoundException {

        when(drivingNotificationService.getAllNotReservation()).thenReturn(
                List.of(NOT_RESERVATION_DRIVING_NOTIFICATION)
        );

        when(drivingNotificationService.checkIfDrivingNotificationIsOutdated(NOT_RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(false);

        when(drivingNotificationService.checkIfUsersReviewed(NOT_RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(true);

        when(drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(NOT_RESERVATION_DRIVING_NOTIFICATION))
                .thenThrow(new PassengerNotHaveTokensException());

        assertThrows(PassengerNotHaveTokensException.class, () ->
                allPassengersReviewCallForRide.allPassengersReviewCallForRide()
        );
        verify(drivingNotificationService, times(0)).delete(NOT_RESERVATION_DRIVING_NOTIFICATION);
    }

    @Test
    @DisplayName("T3-Should throw  EntityNotFoundException")
    public void shouldThrowEntityNotFoundExceptionWhenReviewCallForRide() throws PassengerNotHaveTokensException, EntityNotFoundException {

        when(drivingNotificationService.getAllNotReservation()).thenReturn(
                List.of(NOT_RESERVATION_DRIVING_NOTIFICATION)
        );

        when(drivingNotificationService.checkIfDrivingNotificationIsOutdated(NOT_RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(false);

        when(drivingNotificationService.checkIfUsersReviewed(NOT_RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(true);

        when(drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(NOT_RESERVATION_DRIVING_NOTIFICATION))
                .thenThrow(new EntityNotFoundException(EXIST_OBJECT_ID, EntityType.DRIVING_NOTIFICATION));

        assertThrows(EntityNotFoundException.class, () ->
                allPassengersReviewCallForRide.allPassengersReviewCallForRide()
        );
        verify(drivingNotificationService, times(0)).delete(NOT_RESERVATION_DRIVING_NOTIFICATION);
    }

    @Test
    @DisplayName("T4-Should call delete outdated notification and delete reviewed once")
    public void shouldCallDeleteOutdatedAndDeleteReviewedWhenReviewCallForRide() throws PassengerNotHaveTokensException, EntityNotFoundException {

        when(drivingNotificationService.getAllNotReservation()).thenReturn(
                List.of(NOT_RESERVATION_DRIVING_NOTIFICATION, NOT_RESERVATION_DRIVING_NOTIFICATION_OUTDATED)
        );

        when(drivingNotificationService.checkIfDrivingNotificationIsOutdated(NOT_RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(false);
        when(drivingNotificationService.checkIfDrivingNotificationIsOutdated(NOT_RESERVATION_DRIVING_NOTIFICATION_OUTDATED))
                .thenReturn(true);

        when(drivingNotificationService.checkIfUsersReviewed(NOT_RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(true);

        when(drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(NOT_RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(NOT_REJECTED_DRIVING);

        doNothing().when(drivingNotificationService).deleteOutdatedNotification(NOT_RESERVATION_DRIVING_NOTIFICATION_OUTDATED);
        doNothing().when(drivingNotificationService).delete(NOT_RESERVATION_DRIVING_NOTIFICATION);

        allPassengersReviewCallForRide.allPassengersReviewCallForRide();
        verify(drivingNotificationService, times(1)).delete(NOT_RESERVATION_DRIVING_NOTIFICATION);
        verify(drivingNotificationService, times(1)).deleteOutdatedNotification(NOT_RESERVATION_DRIVING_NOTIFICATION_OUTDATED);
    }

    @Test
    @DisplayName("T5-Should delete one outdated reservation when calls find driver for ride")
    public void shouldDeleteOneOutdatedRWhenFindDriverForRide() throws PassengerNotHaveTokensException, EntityNotFoundException {

        when(drivingNotificationService.getAllReservation()).thenReturn(
                Arrays.asList(RESERVATION_DRIVING_NOTIFICATION, RESERVATION_DRIVING_NOTIFICATION_OUTDATED)
        );
        when(drivingNotificationService.checkIfUsersReviewed(RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(false);
        when(drivingNotificationService.checkIfUsersReviewed(RESERVATION_DRIVING_NOTIFICATION_OUTDATED))
                .thenReturn(false);

        when(drivingNotificationService.checkIfDrivingNotificationIsOutdated(RESERVATION_DRIVING_NOTIFICATION_OUTDATED))
                .thenReturn(true);
        when(drivingNotificationService.checkIfDrivingNotificationIsOutdated(RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(false);

        doNothing().when(drivingNotificationService).deleteOutdatedNotification(RESERVATION_DRIVING_NOTIFICATION_OUTDATED);
        allPassengersReviewCallForRide.reservationShouldFindDriverForRide();

        verify(drivingNotificationService, times(1)).deleteOutdatedNotification(RESERVATION_DRIVING_NOTIFICATION_OUTDATED);
    }

    @Test
    @DisplayName("T6-Should throw passenger not have enough tokens exception when calls find driver for ride")
    public void shouldThrowPassengerNotHaveTokensExceptionWhenFindDriverForRide() throws PassengerNotHaveTokensException, EntityNotFoundException {

        when(drivingNotificationService.getAllReservation()).thenReturn(
                Arrays.asList(RESERVATION_DRIVING_NOTIFICATION)
        );
        when(drivingNotificationService.checkIfUsersReviewed(RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(true);
        when(drivingNotificationService.checkTimeOfStartingReservationRide(RESERVATION_DRIVING_NOTIFICATION.getStarted()))
                .thenReturn(true);

        when(drivingNotificationService. createDrivingIfFoundDriverAndSuccessfullyPaid(RESERVATION_DRIVING_NOTIFICATION))
                .thenThrow(new PassengerNotHaveTokensException());

        assertThrows(PassengerNotHaveTokensException.class, () ->
                allPassengersReviewCallForRide.reservationShouldFindDriverForRide()
        );
        verify(drivingNotificationService, times(1)).createDrivingIfFoundDriverAndSuccessfullyPaid(RESERVATION_DRIVING_NOTIFICATION);
    }

    @Test
    @DisplayName("T7-Should throw entity not found exception when calls find driver for ride")
    public void shouldThrowEntityNotFoundExceptionWhenFindDriverForRide() throws PassengerNotHaveTokensException, EntityNotFoundException {

        when(drivingNotificationService.getAllReservation()).thenReturn(
                Arrays.asList(RESERVATION_DRIVING_NOTIFICATION)
        );
        when(drivingNotificationService.checkIfUsersReviewed(RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(true);
        when(drivingNotificationService.checkTimeOfStartingReservationRide(RESERVATION_DRIVING_NOTIFICATION.getStarted()))
                .thenReturn(true);

        when(drivingNotificationService. createDrivingIfFoundDriverAndSuccessfullyPaid(RESERVATION_DRIVING_NOTIFICATION))
                .thenThrow(new EntityNotFoundException(EXIST_OBJECT_ID, EntityType.DRIVING_NOTIFICATION));

        assertThrows(EntityNotFoundException.class, () ->
                allPassengersReviewCallForRide.reservationShouldFindDriverForRide()
        );
        verify(drivingNotificationService, times(1)).createDrivingIfFoundDriverAndSuccessfullyPaid(RESERVATION_DRIVING_NOTIFICATION);
    }

    @Test
    @DisplayName("T8-Should call delete outdated reservation without driver and create driving if found driver both once when calls find driver for ride")
    public void shouldCallDeleteOutdatedAndCreateDrivingBothOnceRWhenFindDriverForRide() throws PassengerNotHaveTokensException, EntityNotFoundException {

        when(drivingNotificationService.getAllReservation()).thenReturn(
                Arrays.asList(RESERVATION_DRIVING_NOTIFICATION, RESERVATION_DRIVING_NOTIFICATION_OUTDATED)
        );
        when(drivingNotificationService.checkIfUsersReviewed(RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(true);
        when(drivingNotificationService.checkIfUsersReviewed(RESERVATION_DRIVING_NOTIFICATION_OUTDATED))
                .thenReturn(true);

        when(drivingNotificationService.checkTimeOfStartingReservationRide(RESERVATION_DRIVING_NOTIFICATION.getStarted()))
                .thenReturn(true);
        when(drivingNotificationService.checkTimeOfStartingReservationRide(RESERVATION_DRIVING_NOTIFICATION_OUTDATED.getStarted()))
                .thenReturn(false);
        when(drivingNotificationService.checkTimeOfStartingReservationIsSoonRide(RESERVATION_DRIVING_NOTIFICATION_OUTDATED.getStarted()))
                .thenReturn(true);

        when(drivingNotificationService. createDrivingIfFoundDriverAndSuccessfullyPaid(RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(NOT_REJECTED_DRIVING);
        doNothing().when(drivingNotificationService).deleteOutdatedReservationWithoutDriverNotification(RESERVATION_DRIVING_NOTIFICATION_OUTDATED);

        allPassengersReviewCallForRide.reservationShouldFindDriverForRide();

        verify(drivingNotificationService, times(1)).createDrivingIfFoundDriverAndSuccessfullyPaid(RESERVATION_DRIVING_NOTIFICATION);
        verify(drivingNotificationService, times(1)).deleteOutdatedReservationWithoutDriverNotification(RESERVATION_DRIVING_NOTIFICATION_OUTDATED);
    }

    @Test
    @DisplayName("T8-Should call should find driver when calls find driver for ride")
    public void shouldCallShouldFindDriverWhenFindDriverForRide() throws PassengerNotHaveTokensException, EntityNotFoundException {

        when(drivingNotificationService.getAllReservation()).thenReturn(
                Arrays.asList(RESERVATION_DRIVING_NOTIFICATION)
        );
        when(drivingNotificationService.checkIfUsersReviewed(RESERVATION_DRIVING_NOTIFICATION))
                .thenReturn(true);

        when(drivingNotificationService.checkTimeOfStartingReservationRide(RESERVATION_DRIVING_NOTIFICATION.getStarted()))
                .thenReturn(false);
        when(drivingNotificationService.checkTimeOfStartingReservationIsSoonRide(RESERVATION_DRIVING_NOTIFICATION.getStarted()))
                .thenReturn(false);

        doNothing().when(drivingNotificationService).shouldFindDriver(RESERVATION_DRIVING_NOTIFICATION);

        allPassengersReviewCallForRide.reservationShouldFindDriverForRide();

        verify(drivingNotificationService).shouldFindDriver(RESERVATION_DRIVING_NOTIFICATION);
    }

}
