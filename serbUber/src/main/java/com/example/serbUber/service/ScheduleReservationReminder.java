package com.example.serbUber.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import com.example.serbUber.model.Driving;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class ScheduleReservationReminder {
    private DrivingService drivingService;
    private WebSocketService webSocketService;

    public ScheduleReservationReminder(final DrivingService drivingService, final WebSocketService webSocketService){
        this.drivingService = drivingService;
        this.webSocketService = webSocketService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void remindPassengerForReservation(){
        List<Driving> allReservations = drivingService.getAllReservations();
        for(Driving reservation : allReservations){
            long minutesToStartDriving = ChronoUnit.MINUTES.between(LocalDateTime.now(), reservation.getStarted());
            if(minutesToStartDriving == 15 && reservation.getLastReminder() == null){
                setLastReminder(reservation, (int) minutesToStartDriving);
            }
            else if(reservation.getLastReminder() != null){
                long minutesFromLastReminderToNow = ChronoUnit.MINUTES.between(reservation.getLastReminder(), LocalDateTime.now());
                if(minutesFromLastReminderToNow == 5){
                    setLastReminder(reservation, (int) minutesToStartDriving);
                }
            }
        }
    }

    private void setLastReminder(final Driving reservation, final int minutesToStartDriving){
        webSocketService.sendReservationReminder(reservation.getUsers(), minutesToStartDriving);
        reservation.setLastReminder(LocalDateTime.now());
        drivingService.save(reservation);
    }
}
