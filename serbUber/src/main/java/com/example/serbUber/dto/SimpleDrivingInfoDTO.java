package com.example.serbUber.dto;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Location;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SimpleDrivingInfoDTO {
    private double minutes;
    private Location startLocation;
    private Location endLocation;
    private Long drivingId;
    private boolean active;
    private double cost;
    private Long vehicleId;

    private String drivingStatus;

    private LocalDateTime started;

    public SimpleDrivingInfoDTO(Driving driving) {
        this.drivingStatus = driving.getDrivingStatus().toString();
        this.drivingId = driving.getId();
        this.active = driving.isActive();
        if (driving.isActive() || driving.getEnd() == null){
            this.minutes = driving.getRoute().getTimeInMin();
        }else {
            this.minutes = ChronoUnit.MINUTES.between(driving.getEnd(), driving.getStarted());
        }
        this.startLocation = driving.getRoute().getLocations().first().getLocation();
        this.endLocation = driving.getRoute().getLocations().last().getLocation();
        this.cost = driving.getPrice();
        this.started = driving.getStarted();
        this.vehicleId = driving.getDriver().getVehicle().getId();
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

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
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
}
