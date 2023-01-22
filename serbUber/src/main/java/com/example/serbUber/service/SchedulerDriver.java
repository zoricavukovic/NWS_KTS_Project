package com.example.serbUber.service;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.service.user.DriverService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SchedulerDriver {
    private final DriverService driverService;
    private final DrivingService drivingService;

    public SchedulerDriver(final DriverService driverService, final DrivingService drivingService) {
        this.driverService = driverService;
        this.drivingService = drivingService;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void createOnMyWayDrivingForDriver() {
        List<Driver> activeDrivers = driverService.getActiveDrivers();
        for (Driver driver : activeDrivers) {
            Driving nextDriving = drivingService.driverHasFutureDriving(driver.getId());
            if (driver.isDrive() && nextDriving != null) {

                continue;
            } else if (nextDriving != null) {
                drivingService.createDrivingToDeparture(driver, driver.getVehicle().getCurrentStop(), nextDriving.getRoute(), nextDriving.getUsers());
            }
        }
    }
}

