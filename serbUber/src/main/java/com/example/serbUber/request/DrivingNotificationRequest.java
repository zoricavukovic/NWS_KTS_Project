package com.example.serbUber.request;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.POSITIVE_MESSAGE;
import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

public class DrivingNotificationRequest {
    private double lonStarted;
    private double latStarted;
    private double lonEnd;
    private double latEnd;
    @Valid
    @Positive(message = POSITIVE_MESSAGE)
    private double price;

    @Valid
    @Email(message = WRONG_EMAIL)
    private String senderEmail;
    private List<String> passengers;

    public DrivingNotificationRequest() {
    }

    public DrivingNotificationRequest(double lonStarted, double latStarted, double lonEnd, double latEnd, double price, String senderEmail, List<String> passengers) {
        this.lonStarted = lonStarted;
        this.latStarted = latStarted;
        this.lonEnd = lonEnd;
        this.latEnd = latEnd;
        this.price = price;
        this.senderEmail = senderEmail;
        this.passengers = passengers;
    }

    public double getLonStarted() {
        return lonStarted;
    }

    public void setLonStarted(double lonStarted) {
        this.lonStarted = lonStarted;
    }

    public double getLatStarted() {
        return latStarted;
    }

    public void setLatStarted(double latStarted) {
        this.latStarted = latStarted;
    }

    public double getLonEnd() {
        return lonEnd;
    }

    public void setLonEnd(double lonEnd) {
        this.lonEnd = lonEnd;
    }

    public double getLatEnd() {
        return latEnd;
    }

    public void setLatEnd(double latEnd) {
        this.latEnd = latEnd;
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
}