package com.example.serbUber.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;

@Document(collection = "drivings")
public class Driving {
    @Id
    private String id;
    private boolean active;
    private int duration;
    private LocalDateTime started;
    private LocalDateTime payingLimit;
    private Route route;
    private DrivingStatus drivingStatus;
    private String driverEmail;
    private HashMap<String, Boolean> usersPaid = new HashMap<String, Boolean>();
    private double price;

    public Driving() {

    }

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
