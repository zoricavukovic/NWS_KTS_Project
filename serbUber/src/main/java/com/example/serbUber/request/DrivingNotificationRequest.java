package com.example.serbUber.request;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;
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
    private boolean babySeat;
    private boolean petFriendly;
    private String vehicleType;
    private Timestamp chosenDateTime;
    private boolean reservation = false;

    public DrivingNotificationRequest() {
    }

    public DrivingNotificationRequest(
        final RouteRequest routeRequest,
        final double price,
        final String senderEmail,
        final List<String> passengers,
        final boolean babySeat,
        final boolean petFriendly,
        final String vehicleType,
        final Timestamp chosenDateTime
    ) {
        this.route = routeRequest;
        this.price = price;
        this.senderEmail = senderEmail;
        this.passengers = passengers;
        this.babySeat = babySeat;
        this.petFriendly = petFriendly;
        this.vehicleType = vehicleType;
        this.chosenDateTime = chosenDateTime;
    }

    public DrivingNotificationRequest(
        final RouteRequest routeRequest,
        final double price,
        final String senderEmail,
        final List<String> passengers,
        final boolean babySeat,
        final boolean petFriendly,
        final String vehicleType,
        final Timestamp chosenDateTime,
        final boolean isReservation
    ) {
        this.route = routeRequest;
        this.price = price;
        this.senderEmail = senderEmail;
        this.passengers = passengers;
        this.babySeat = babySeat;
        this.petFriendly = petFriendly;
        this.vehicleType = vehicleType;
        this.chosenDateTime = chosenDateTime;
        this.reservation = isReservation;
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

    public Timestamp getChosenDateTime() {
        return chosenDateTime;
    }

    public void setChosenDateTime(Timestamp chosenDateTime) {
        this.chosenDateTime = chosenDateTime;
    }

    public boolean isReservation() {
        return reservation;
    }

    public void setReservation(boolean reservation) {
        this.reservation = reservation;
    }

}
