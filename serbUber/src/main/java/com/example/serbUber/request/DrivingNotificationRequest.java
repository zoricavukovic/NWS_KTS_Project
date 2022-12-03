package com.example.serbUber.request;


import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.POSITIVE_MESSAGE;
import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

public class DrivingNotificationRequest {

    private List<DrivingLocationIndexRequest> locations;
    @Valid
    @Positive(message = POSITIVE_MESSAGE)
    private double price;
    @Valid
    @Email(message = WRONG_EMAIL)
    private String senderEmail;
    private List<String> passengers;
    @FutureOrPresent(message = "Started time cannot be in past.")
    private LocalDateTime started;
    @Positive(message = "Duration of driving must be positive")
    private int duration;
    private boolean babySeat;
    private boolean petFriendly;
    private String vehicleType;
    @Positive(message = "Time of driving must be positive")
    private double time;
    @Positive(message = "Number of kilometers must be positive")
    private double distance;

    public DrivingNotificationRequest() {
    }

    public DrivingNotificationRequest(List<DrivingLocationIndexRequest> locations, double price, String senderEmail, List<String> passengers, LocalDateTime started, int duration, boolean babySeat, boolean petFriendly, String vehicleType, double time, double distance) {
        this.locations = locations;
        this.price = price;
        this.senderEmail = senderEmail;
        this.passengers = passengers;
        this.started = started;
        this.duration = duration;
        this.babySeat = babySeat;
        this.petFriendly = petFriendly;
        this.vehicleType = vehicleType;
        this.distance = distance;
        this.time = time;
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

    public List<DrivingLocationIndexRequest> getLocations() {
        return locations;
    }

    public void setLocations(List<DrivingLocationIndexRequest> locations) {
        this.locations = locations;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
