package com.example.serbUber.request;

import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashMap;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

public class DrivingRequest {

    @NotNull(message = "Active must be selected")
    private boolean active;

    @NotNull(message = "Duration must be selected.")
    @PositiveOrZero(message = "Duration must be grater or equal with 0.")
    private int duration;

    @NotNull(message = "Started date must be selected.")
    @FutureOrPresent(message = "Started time cannot be in past.")
    private LocalDateTime started;

    @NotNull(message = "Paying limit must be selected.")
    @FutureOrPresent(message = "Paying limit cannot be in past.")
    private LocalDateTime payingLimit;

    @NotNull(message = "Route must be selected.")
    @Valid
    private RouteRequest route;

    @NotNull(message = "Driving status must be selected.")
    private DrivingStatus drivingStatus;

    @Email(message = WRONG_EMAIL)
    @NotBlank(message = EMPTY_EMAIL)
    @Size(max = 60, message = TOO_LONG_EMAIL)
    private String driverEmail;

    @NotEmpty(message = "Users paid cannot be empty.")
    private HashMap<String, Boolean> usersPaid = new HashMap<String, Boolean>();

    @NotNull(message = "Price cannot be empty.")
    @Positive(message = "Price must be greater than 0.")
    private double price;

    public DrivingRequest(
            final boolean active,
            final int duration,
            final LocalDateTime started,
            final LocalDateTime payingLimit,
            final RouteRequest route,
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

    public RouteRequest getRoute() {
        return route;
    }

    public void setRoute(RouteRequest route) {
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
