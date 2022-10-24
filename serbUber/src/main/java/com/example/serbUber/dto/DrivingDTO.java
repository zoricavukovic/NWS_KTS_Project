package com.example.serbUber.dto;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DrivingDTO {

    private boolean active;
    private int duration;
    private LocalDateTime started;
    private LocalDateTime payingLimit;
    private Route route;
    private DrivingStatus drivingStatus;
    private String driverEmail;
    private HashMap<String, Boolean> usersPaid = new HashMap<String, Boolean>();
    private double price;

    public DrivingDTO(final Driving driving){
        this.active = driving.isActive();
        this.duration = driving.getDuration();
        this.started = driving.getStarted();
        this.payingLimit = driving.getPayingLimit();
        this.route = driving.getRoute();
        this.drivingStatus = driving.getDrivingStatus();
        this.driverEmail = driving.getDriverEmail();
        this.usersPaid = driving.getUsersPaid();
        this.price = driving.getPrice();
    }

    public static List<DrivingDTO> fromDrivings(final List<Driving> drivings){
        List<DrivingDTO> drivingDTOs = new LinkedList<>();
        drivings.forEach(driving ->
                drivingDTOs.add(new DrivingDTO(driving))
        );

        return drivingDTOs;
    }

    public boolean isActive() {
        return active;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public LocalDateTime getPayingLimit() {
        return payingLimit;
    }

    public Route getRoute() {
        return route;
    }

    public DrivingStatus getDrivingStatus() {
        return drivingStatus;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public HashMap<String, Boolean> getUsersPaid() {
        return usersPaid;
    }

    public double getPrice() {
        return price;
    }
}
