package com.example.serbUber.dto;

import com.example.serbUber.model.DrivingNotificationType;

public class DrivingNotificationWebSocketDTO {
    private Long id;
    private String senderEmail;

    private DrivingNotificationType drivingNotificationType;

    public DrivingNotificationWebSocketDTO(Long id, String senderEmail, DrivingNotificationType drivingNotificationType) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.drivingNotificationType = drivingNotificationType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
