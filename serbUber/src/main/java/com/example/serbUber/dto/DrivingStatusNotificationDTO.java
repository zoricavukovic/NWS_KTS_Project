package com.example.serbUber.dto;

import com.example.serbUber.model.DrivingStatus;

public class DrivingStatusNotificationDTO {

    private Long driverId;
    private double minutes;
    private DrivingStatus drivingStatus;
    private String reason;
    private Long drivingId;

    private Long notificationId;

    public DrivingStatusNotificationDTO(Long driverId, double minutes, DrivingStatus drivingStatus, String reason, Long drivingId, Long notificationId) {
        this.driverId = driverId;
        this.minutes = minutes;
        this.drivingStatus = drivingStatus;
        this.reason = reason;
        this.drivingId = drivingId;
        this.notificationId = notificationId;
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

    public Long getDrivingId() {
        return drivingId;
    }

    public void setDrivingId(Long drivingId) {
        this.drivingId = drivingId;
    }

    public void setMinutes(double minutes) {
        this.minutes = minutes;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }
}
