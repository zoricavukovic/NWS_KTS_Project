package com.example.serbUber.controller;

import com.example.serbUber.service.user.DriverService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class SchedulerController {

    private final DriverService driverService;

    public SchedulerController(final DriverService driverService) {
        this.driverService = driverService;
    }

    @Scheduled(cron = "${scheduler.cron.every.minute}")
    public void resetActivityForDrivers() {
        driverService.resetActivityForDrivers();
    }
}
