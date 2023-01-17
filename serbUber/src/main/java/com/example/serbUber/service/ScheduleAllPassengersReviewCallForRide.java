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

@Controller
public class ScheduleAllPassengersReviewCallForRide {

    private final DrivingNotificationService drivingNotificationService;

    public ScheduleAllPassengersReviewCallForRide(final DrivingNotificationService drivingNotificationService){
        this.drivingNotificationService = drivingNotificationService;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void allPassengersReviewCallForRide() throws EntityNotFoundException, PassengerNotHaveTokensException {
        List<DrivingNotification> allDrivingNotifications = drivingNotificationService.getAll();
        for(DrivingNotification drivingNotification : allDrivingNotifications){
            if(drivingNotification.getStarted().plusMinutes(10).isBefore(LocalDateTime.now())){
                drivingNotificationService.deleteOutdatedNotification(drivingNotification);
            }
            else {
                Map<RegularUser, Integer> receiversReviewed = drivingNotification.getReceiversReviewed();
                boolean allPassengersReviewed = true;
                for(Map.Entry<RegularUser, Integer> receiverReview : receiversReviewed.entrySet()){
                    if(receiverReview.getValue() == 1){
                        drivingNotificationService.sendPassengersNotAcceptDrivingNotification(receiversReviewed.keySet(), receiverReview.getKey().getEmail(), drivingNotification.getSender().getEmail());
                        drivingNotificationService.delete(drivingNotification);
                        allPassengersReviewed = false;
                        break;
                    }
                    else if(receiverReview.getValue() == 2){
                        allPassengersReviewed = false;
                    }
                }

                if(allPassengersReviewed){
                    //pozivamo metodu da se trazi vozac...
                    drivingNotificationService.shouldFindDriver(drivingNotification);
                    drivingNotificationService.delete(drivingNotification);
                }
            }

        }

    }
}
