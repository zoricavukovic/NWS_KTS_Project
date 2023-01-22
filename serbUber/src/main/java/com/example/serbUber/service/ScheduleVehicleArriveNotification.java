package com.example.serbUber.service;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.Driving;
import com.example.serbUber.service.user.DriverService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleVehicleArriveNotification {

    private final DriverService driverService;
    private final DrivingService drivingService;

    private final WebSocketService webSocketService;

    public ScheduleVehicleArriveNotification(
            final DriverService driverService,
            final DrivingService drivingService,
            final WebSocketService webSocketService){
        this.driverService = driverService;
        this.drivingService = drivingService;
        this.webSocketService = webSocketService;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void createVehicleArriveNotification() throws EntityNotFoundException {
        List<Driver> activeDrivers = driverService.getActiveDrivers();
        for (Driver driver : activeDrivers) {
            Driving onWayToDepartureDriving = drivingService.getTimeToDepartureDriving(driver.getId());
            if(onWayToDepartureDriving != null && isVehicleArriveOnDeparture(driver.getVehicle().getCurrentStop(), onWayToDepartureDriving.getRoute().getLocations().first().getLocation())){
                webSocketService.sendVehicleArriveNotification(onWayToDepartureDriving.getUsers());
            }
        }
    }

    private boolean isVehicleArriveOnDeparture(Location currentDriverLocation, Location departureLocation){

        return currentDriverLocation.getLat() == departureLocation.getLat()
                && currentDriverLocation.getLon() == departureLocation.getLon();
    }
}
