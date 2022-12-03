package com.example.serbUber.dto;

import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.DrivingStatusNotification;

public class DrivingStatusNotificationDTO {

    private Long driverId;
    private double minutes;
    private DrivingStatus drivingStatus;
    private String reason;
    private Long id;

    public DrivingStatusNotificationDTO(Long driverId, double minutes, DrivingStatus drivingStatus, String reason, Long id) {
        this.driverId = driverId;
        this.minutes = minutes;
        this.drivingStatus = drivingStatus;
        this.reason = reason;
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public double getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public DrivingStatus getDrivingStatus() {
        return drivingStatus;
    }

    public void setDrivingStatus(DrivingStatus drivingStatus) {
        this.drivingStatus = drivingStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
