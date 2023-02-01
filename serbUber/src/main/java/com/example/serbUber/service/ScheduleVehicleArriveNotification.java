package com.example.serbUber.service;

import com.example.serbUber.dto.SimpleDrivingInfoDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.Driving;
import com.example.serbUber.service.user.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleVehicleArriveNotification {

    private DriverService driverService;
    private DrivingService drivingService;
    private WebSocketService webSocketService;

    @Autowired
    public ScheduleVehicleArriveNotification(
            final DriverService driverService,
            final DrivingService drivingService,
            final WebSocketService webSocketService){
        this.driverService = driverService;
        this.drivingService = drivingService;
        this.webSocketService = webSocketService;
    }

    @Scheduled(cron = "*/10 * * * * *")
    @Transactional
    public void createVehicleArriveNotification() {
        List<Driver> activeDrivers = driverService.getActiveDrivers();
        for (Driver driver : activeDrivers) {
            Driving onWayToDepartureDriving = drivingService.getTimeToDepartureDriving(driver.getId());
            Driving futureDriving = drivingService.driverHasFutureDriving(driver.getId());
            if(onWayToDepartureDriving != null && isVehicleArriveOnDeparture(driver.getVehicle().getCurrentStop(), onWayToDepartureDriving.getRoute().getLocations().last().getLocation())){
                onWayToDepartureDriving.setActive(false);
                drivingService.save(onWayToDepartureDriving);
                webSocketService.sendVehicleArriveNotification(new SimpleDrivingInfoDTO(futureDriving), onWayToDepartureDriving.getUsers(), driver.getEmail());
            }
        }
    }

    private boolean isVehicleArriveOnDeparture(Location currentDriverLocation, Location departureLocation){

        return currentDriverLocation.getLat() == departureLocation.getLat()
                && currentDriverLocation.getLon() == departureLocation.getLon();
    }
}
