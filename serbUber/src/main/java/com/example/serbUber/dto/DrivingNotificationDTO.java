package com.example.serbUber.dto;

import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.user.RegularUser;

import java.util.Set;

public class DrivingNotificationDTO {

    private double lonStarted;
    private double latStarted;
    private double lonEnd;
    private double latEnd;
    private double price;
    private RegularUser sender;
    private Set<RegularUser> users;

    public DrivingNotificationDTO(final DrivingNotification drivingNotification){
        this.lonStarted = drivingNotification.getLonStarted();
        this.lonEnd = drivingNotification.getLonEnd();
        this.latStarted = drivingNotification.getLatStarted();
        this.latEnd = drivingNotification.getLatEnd();
        this.price = drivingNotification.getPrice();
        this.sender = drivingNotification.getSender();
        this.users = drivingNotification.getUsers();
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

    public RegularUser getSender() {
        return sender;
    }

    public void setSender(RegularUser sender) {
        this.sender = sender;
    }

    public Set<RegularUser> getUsers() {
        return users;
    }

    public void setUsers(Set<RegularUser> users) {
        this.users = users;
    }
}
