package com.example.serbUber.request;

import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashMap;

public class DrivingRequest {

    private boolean active;
    @PositiveOrZero
    private int duration;
    @NotNull
    @FutureOrPresent
    private LocalDateTime started;
    @NotNull
    private LocalDateTime payingLimit;
    @NotNull
    private Route route;
    @NotNull
    private DrivingStatus drivingStatus;
    @NotNull @Email
    private String driverEmail;
    @NotEmpty
    private HashMap<String, Boolean> usersPaid = new HashMap<String, Boolean>();
    @Positive
    private double price;

    public DrivingRequest(
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getPayingLimit() {
        return payingLimit;
    }

    public void setPayingLimit(LocalDateTime payingLimit) {
        this.payingLimit = payingLimit;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public DrivingStatus getDrivingStatus() {
        return drivingStatus;
    }

    public void setDrivingStatus(DrivingStatus drivingStatus) {
        this.drivingStatus = drivingStatus;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public HashMap<String, Boolean> getUsersPaid() {
        return usersPaid;
    }

    public void setUsersPaid(HashMap<String, Boolean> usersPaid) {
        this.usersPaid = usersPaid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
