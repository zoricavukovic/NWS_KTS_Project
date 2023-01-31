package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.service.user.DriverService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class SchedulerDriver {
    private final DriverService driverService;
    private final DrivingService drivingService;
    private final WebSocketService webSocketService;

    public SchedulerDriver(
        final DriverService driverService,
        final DrivingService drivingService,
        final WebSocketService webSocketService
    ) {
        this.driverService = driverService;
        this.drivingService = drivingService;
        this.webSocketService = webSocketService;
    }

    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void createOnMyWayDrivingForDriver() {
        List<Driver> activeDrivers = driverService.getActiveDrivers();
        for (Driver driver : activeDrivers) {
            Driving nextDriving = drivingService.driverHasFutureDriving(driver.getId());
            if (!driver.isDrive() && nextDriving != null && driverService.isTimeToGoToDeparture(driver, nextDriving)) {
                DrivingDTO drivingDTO = drivingService.createDrivingToDeparture(driver, driver.getVehicle().getCurrentStop(), nextDriving.getRoute(), nextDriving.getUsers());
                webSocketService.startDrivingToDeparture(nextDriving.getUsers(), drivingDTO.getDuration());
            }
        }
    }
}

