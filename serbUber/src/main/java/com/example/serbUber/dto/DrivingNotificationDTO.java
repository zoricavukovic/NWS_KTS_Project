package com.example.serbUber.dto;

import com.example.serbUber.model.*;
import com.example.serbUber.model.user.RegularUser;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.example.serbUber.model.DrivingNotification.getListOfUsers;

public class DrivingNotificationDTO {

    private Route route;
    private DrivingNotificationType drivingNotificationType;
    private LocalDateTime started;
    private double duration;
    private VehicleTypeInfo vehicleTypeInfo;
    private Set<RegularUser> passengers;
    private double price;
    private LocalDateTime chosenDateTime;

    public DrivingNotificationDTO(final DrivingNotification drivingNotification) {
        this.route = drivingNotification.getRoute();
        this.price = drivingNotification.getPrice();
        this.vehicleTypeInfo = drivingNotification.getVehicleTypeInfo();
        this.started = drivingNotification.getStarted();
        this.duration = drivingNotification.getDuration();
        this.vehicleTypeInfo = drivingNotification.getVehicleTypeInfo();
        Set<RegularUser> passengers = getListOfUsers(drivingNotification.getReceiversReviewed());
        passengers.add(drivingNotification.getSender());
        this.passengers = passengers;
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

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public VehicleTypeInfo getVehicleTypeInfo() {
        return vehicleTypeInfo;
    }

    public void setVehicleTypeInfo(VehicleTypeInfo vehicleTypeInfo) {
        this.vehicleTypeInfo = vehicleTypeInfo;
    }

    public Set<RegularUser> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<RegularUser> passengers) {
        this.passengers = passengers;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public LocalDateTime getChosenDateTime() {
        return chosenDateTime;
    }

    public void setChosenDateTime(LocalDateTime chosenDateTime) {
        this.chosenDateTime = chosenDateTime;
    }
}
