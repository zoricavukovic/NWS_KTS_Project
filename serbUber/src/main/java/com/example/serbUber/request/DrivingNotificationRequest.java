package com.example.serbUber.request;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.POSITIVE_MESSAGE;
import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

public class DrivingNotificationRequest {

    private RouteRequest route;
    @Valid
//    @Positive(message = POSITIVE_MESSAGE)
    private double price;
    @Valid
    @Email(message = WRONG_EMAIL)
    private String senderEmail;
    private List<String> passengers;

    private LocalDateTime started;
//    @Positive(message = "Duration of driving must be positive")
    private int duration;
    private boolean babySeat;
    private boolean petFriendly;
    private String vehicleType;

    public DrivingNotificationRequest() {
    }

    public DrivingNotificationRequest(RouteRequest routeRequest, double price, String senderEmail, List<String> passengers, LocalDateTime started, int duration, boolean babySeat, boolean petFriendly, String vehicleType, double time, double distance) {
        this.route = routeRequest;
        this.price = price;
        this.senderEmail = senderEmail;
        this.passengers = passengers;
        this.started = started;
        this.duration = duration;
        this.babySeat = babySeat;
        this.petFriendly = petFriendly;
        this.vehicleType = vehicleType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public List<String> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isBabySeat() {
        return babySeat;
    }

    public void setBabySeat(boolean babySeat) {
        this.babySeat = babySeat;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public RouteRequest getRoute() {
        return route;
    }

    public void setRoute(RouteRequest route) {
        this.route = route;
    }
}
