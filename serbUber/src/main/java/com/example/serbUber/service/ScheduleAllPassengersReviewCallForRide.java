package com.example.serbUber.service;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.PassengerNotHaveTokensException;
import com.example.serbUber.model.DrivingNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ScheduleAllPassengersReviewCallForRide {

    private DrivingNotificationService drivingNotificationService;

    @Autowired
    public ScheduleAllPassengersReviewCallForRide(final DrivingNotificationService drivingNotificationService){
        this.drivingNotificationService = drivingNotificationService;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void allPassengersReviewCallForRide() throws EntityNotFoundException, PassengerNotHaveTokensException {
        List<DrivingNotification> allDrivingNotifications = drivingNotificationService.getAllNotReservation();
        for(DrivingNotification drivingNotification : allDrivingNotifications){
            if(drivingNotificationService.checkIfDrivingNotificationIsOutdated(drivingNotification)){
                drivingNotificationService.deleteOutdatedNotification(drivingNotification);
            }
            else if (drivingNotificationService.checkIfUsersReviewed(drivingNotification)){
                drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(drivingNotification);
                drivingNotificationService.delete(drivingNotification);
            }
        }
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void reservationShouldFindDriverForRide() throws EntityNotFoundException, PassengerNotHaveTokensException {
        List<DrivingNotification> allDrivingNotifications = drivingNotificationService.getAllReservation();
        for(DrivingNotification drivingNotification : allDrivingNotifications){
            if (drivingNotificationService.checkIfUsersReviewed(drivingNotification)){
              allUsersAreReviewed(drivingNotification);
            }
            else if(drivingNotificationService.checkIfDrivingNotificationIsOutdated(drivingNotification)){
                drivingNotificationService.deleteOutdatedNotification(drivingNotification);
            }
        }
    }

    private void allUsersAreReviewed(DrivingNotification drivingNotification) throws PassengerNotHaveTokensException, EntityNotFoundException {
        if (drivingNotificationService.checkTimeOfStartingReservationRide(drivingNotification.getStarted())){
            drivingNotificationService.createDrivingIfFoundDriverAndSuccessfullyPaid(drivingNotification);
        }else if (drivingNotificationService.checkTimeOfStartingReservationIsSoonRide(drivingNotification.getStarted())){
            drivingNotificationService.deleteOutdatedReservationWithoutDriverNotification(drivingNotification);
        }else {
            drivingNotificationService.shouldFindDriver(drivingNotification);
        }
    }
}
