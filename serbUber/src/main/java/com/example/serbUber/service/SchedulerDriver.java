package com.example.serbUber.service;


import com.example.serbUber.model.Driving;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.service.user.DriverService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class SchedulerDriver {
    private DriverService driverService;
    private DrivingService drivingService;

    public SchedulerDriver(final DriverService driverService, final DrivingService drivingService){
        this.driverService = driverService;
        this.drivingService = drivingService;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void createOnMyWayDrivingForDriver(){
        List<Driver> activeDrivers = driverService.getActiveDrivers();
        for (Driver driver: activeDrivers){
            Driving nextDriving = drivingService.driverHasFutureDriving(driver.getId());
            if (driver.isDrive() && nextDriving != null){

                continue;
            } else if (nextDriving != null){
                drivingService.createDrivingToDeparture(driver, driver.getVehicle().getCurrentStop(), nextDriving.getRoute());
            }
        }
//        List<Driving> allReservations = drivingService.getAllReservations();
//        for(Driving reservation : allReservations){
//            long minutesToStartDriving = ChronoUnit.MINUTES.between(LocalDateTime.now(), reservation.getStarted());
//            if(minutesToStartDriving == 15 && reservation.getLastReminder() == null){
//                setLastReminder(reservation, (int) minutesToStartDriving);
//            }
//            else if(reservation.getLastReminder() != null){
//                long minutesFromLastReminderToNow = ChronoUnit.MINUTES.between(reservation.getLastReminder(), LocalDateTime.now());
//                if(minutesFromLastReminderToNow == 5){
//                    setLastReminder(reservation, (int) minutesToStartDriving);
//                }
//            }
//        }
    }
}

