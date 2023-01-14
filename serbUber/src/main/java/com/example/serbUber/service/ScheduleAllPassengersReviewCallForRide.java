package com.example.serbUber.service;

import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.user.RegularUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ScheduleAllPassengersReviewCallForRide {

    private final DrivingNotificationService drivingNotificationService;

    public ScheduleAllPassengersReviewCallForRide(final DrivingNotificationService drivingNotificationService){
        this.drivingNotificationService = drivingNotificationService;
    }

    public void allPassengersReviewCallForRide(){
        List<DrivingNotification> allDrivingNotifications = drivingNotificationService.getAll();
        for(DrivingNotification drivingNotification : allDrivingNotifications){
            if(drivingNotification.getStarted().plusMinutes(10).isBefore(LocalDateTime.now())){
                drivingNotificationService.delete(drivingNotification);
            }
            else {
                Map<RegularUser, Integer> receiversReviewed = drivingNotification.getReceiversReviewed();
                boolean allPassengersReviewed = true;
                for(Map.Entry<RegularUser, Integer> receiverReview : receiversReviewed.entrySet()){
                    if(receiverReview.getValue() == 0){
                        drivingNotificationService.sendPassengersNotAcceptDrivingNotification(receiversReviewed.keySet(), receiverReview.getKey().getEmail(), drivingNotification.getSender().getEmail());
                    }
                    else if(receiverReview.getValue() == -1){
                        allPassengersReviewed = false;
                    }
                }

                if(allPassengersReviewed){
                    //pozivamo metodu da se trazi vozac...
                }
            }

        }

    }
}
