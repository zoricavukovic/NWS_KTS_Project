package com.example.serbUber.model;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Driving {
    private boolean active;
    private int duration;
    private LocalDateTime started;
    private LocalDateTime payingLimit;
    private Route route;
    private DrivingStatus drivingStatus;
    private String driverEmail;
    private HashMap<String, Boolean> usersPaid = new HashMap<String, Boolean>();
    private double price;

    public Driving(
        final boolean active,
        final int duration,
        final LocalDateTime started,
        final LocalDateTime payingLimit,
        final Route route,
        final DrivingStatus drivingStatus,
        final String driverEmail,
        final HashMap<String, Boolean> usersPaid,
        final double price
    ) {
        this.active = active;
        this.duration = duration;
        this.started = started;
        this.payingLimit = payingLimit;
        this.route = route;
        this.drivingStatus = drivingStatus;
        this.driverEmail = driverEmail;
        this.usersPaid = usersPaid;
        this.price = price;
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
