package com.example.serbUber.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import com.example.serbUber.model.Driving;
import org.springframework.transaction.annotation.Transactional;

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

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void remindPassengerForReservation(){
        List<Driving> allReservations = drivingService.getAllReservations();
        allReservations.forEach((reservation) -> {
            long minutesToStartDriving = ChronoUnit.MINUTES.between(LocalDateTime.now(), reservation.getStarted());
            if(isTimeToRemind(reservation, minutesToStartDriving)){
                setLastReminder(reservation, (int) minutesToStartDriving);
            }
        });
    }

    private boolean isTimeToRemind(Driving reservation, long minutesToStartDriving){

        return (minutesToStartDriving == 15 && reservation.getLastReminder() == null) ||
                (reservation.getLastReminder() != null && getMinutesFromLastReminderToNow(reservation.getLastReminder()) == 5);
    }

    private long getMinutesFromLastReminderToNow(LocalDateTime lastReminder) {

        return ChronoUnit.MINUTES.between(lastReminder, LocalDateTime.now());
    }

    private void setLastReminder(final Driving reservation, final int minutesToStartDriving){
        webSocketService.sendReservationReminder(reservation.getUsers(), minutesToStartDriving);
        reservation.setLastReminder(LocalDateTime.now());
        drivingService.save(reservation);
    }
}
