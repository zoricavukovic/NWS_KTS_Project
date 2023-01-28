package com.example.serbUber.dto;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SimpleDrivingInfoDTO {
    private double minutes;
    private Route route;
    private Long drivingId;
    private boolean active;
    private double cost;
    private Long vehicleId;

    private String drivingStatus;

    private LocalDateTime started;

    private String vehicleType;

    public SimpleDrivingInfoDTO() {

    }

    public SimpleDrivingInfoDTO(Driving driving) {
        this.drivingStatus = driving.getDrivingStatus().toString();
        this.drivingId = driving.getId();
        this.active = driving.isActive();
        if (driving.isActive() || driving.getEnd() == null){
            this.minutes = driving.getRoute().getTimeInMin();
        }else {
            this.minutes = ChronoUnit.MINUTES.between(driving.getEnd(), driving.getStarted());
        }
        this.route = driving.getRoute();
        this.cost = driving.getPrice();
        this.started = driving.getStarted();
        this.vehicleId = driving.getDriver().getVehicle().getId();
        this.vehicleType = driving.getDriver().getVehicle().getVehicleTypeInfo().getVehicleType().toString();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getMinutes() {
        return minutes;
    }

    public void setMinutes(double minutes) {
        this.minutes = minutes;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Long getDrivingId() {
        return drivingId;
    }

    public void setDrivingId(Long drivingId) {
        this.drivingId = drivingId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDrivingStatus() {
        return drivingStatus;
    }

    public void setDrivingStatus(String drivingStatus) {
        this.drivingStatus = drivingStatus;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
