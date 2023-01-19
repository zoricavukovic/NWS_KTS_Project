package com.example.serbUber.service;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.PassengerNotHaveTokensException;
import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.user.RegularUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.example.serbUber.util.Constants.*;

@Controller
public class ScheduleAllPassengersReviewCallForRide {

    private final DrivingNotificationService drivingNotificationService;

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
                drivingNotificationService.findDriverNow(drivingNotification);
                drivingNotificationService.delete(drivingNotification);
            }

        }

    }

    @Scheduled(cron = "*/30 * * * * *")
    public void reservationShouldFindDriverForRide() throws EntityNotFoundException, PassengerNotHaveTokensException {
        List<DrivingNotification> allDrivingNotifications = drivingNotificationService.getAllReservation();
        for(DrivingNotification drivingNotification : allDrivingNotifications){
            if (drivingNotificationService.checkIfUsersReviewed(drivingNotification)){
                if (drivingNotificationService.checkTimeOfStartingReservationRide(drivingNotification.getStarted())){
                    drivingNotificationService.findDriverNow(drivingNotification);
                }else if (drivingNotificationService.checkTimeOfStartingReservationIsSoonRide(drivingNotification.getStarted())){
                    drivingNotificationService.deleteOutdatedReservationWithoutDriverNotification(drivingNotification);
                }
            } else {
                if(drivingNotificationService.checkIfDrivingNotificationIsOutdated(drivingNotification)){
                    drivingNotificationService.deleteOutdatedNotification(drivingNotification);
                }
            }
        }

    }
}
