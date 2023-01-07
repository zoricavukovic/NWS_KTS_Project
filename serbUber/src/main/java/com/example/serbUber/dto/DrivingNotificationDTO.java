package com.example.serbUber.dto;

import com.example.serbUber.model.*;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;
import com.graphhopper.storage.index.LocationIndex;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.example.serbUber.model.DrivingNotification.getListOfUsers;

public class DrivingNotificationDTO {

    private Route route;
    private String senderEmail;
    private DrivingNotificationType drivingNotificationType;
    private LocalDateTime started;
    private int duration;
    private Vehicle vehicle;
    private Set<RegularUser> receivers;
    private double price;

    public DrivingNotificationDTO(final DrivingNotification drivingNotification) {
        this.route = drivingNotification.getRoute();
        this.price = drivingNotification.getPrice();
        this.senderEmail = drivingNotification.getSender().getEmail();
        this.vehicle = drivingNotification.getVehicle();
        this.receivers = getListOfUsers(drivingNotification.getReceiversReviewed());
    }

    public static List<DrivingNotificationDTO> fromDrivingNotifications(List<DrivingNotification> notifications) {
        List<DrivingNotificationDTO> notificationDTOs = new LinkedList<>();
        notifications.forEach(notification ->
                notificationDTOs.add(new DrivingNotificationDTO(notification)));
        return notificationDTOs;
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

    public DrivingNotificationType getDrivingNotificationType() {
        return drivingNotificationType;
    }

    public void setDrivingNotificationType(DrivingNotificationType drivingNotificationType) {
        this.drivingNotificationType = drivingNotificationType;
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

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Set<RegularUser> getReceivers() {
        return receivers;
    }

    public void setReceivers(Set<RegularUser> receivers) {
        this.receivers = receivers;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

}
