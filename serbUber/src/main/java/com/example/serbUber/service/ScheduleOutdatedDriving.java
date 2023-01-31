package com.example.serbUber.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class ScheduleOutdatedDriving {

    private final DrivingService drivingService;

    public ScheduleOutdatedDriving(final DrivingService drivingService){

        this.drivingService = drivingService;
    }

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void rejectOutdatedDrivings(){
        this.drivingService.rejectOutdatedDrivings();
    }
}
