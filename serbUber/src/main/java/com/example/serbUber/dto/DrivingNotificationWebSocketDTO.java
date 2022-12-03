package com.example.serbUber.dto;

import com.example.serbUber.model.DrivingNotificationType;

public class DrivingNotificationWebSocketDTO {
    private Long id;
    private String senderEmail;

    public DrivingNotificationWebSocketDTO(Long id, String senderEmail) {
        this.id = id;
        this.senderEmail = senderEmail;
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

}
