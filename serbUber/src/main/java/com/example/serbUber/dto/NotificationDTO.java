package com.example.serbUber.dto;

import com.example.serbUber.model.Notification;
import com.example.serbUber.model.User;

import java.util.LinkedList;
import java.util.List;

public class NotificationDTO {

    private String id;
    private String message;
    private User sender;
    private User receiver;
    private boolean report;

    public NotificationDTO(final Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.sender = notification.getSender();
        this.receiver = notification.getReceiver();
        this.report = notification.isReport();
    }

    public static List<NotificationDTO> fromNotifications(List<Notification> notifications) {
        List<NotificationDTO> dtos = new LinkedList<>();
        notifications.forEach(notification ->
                dtos.add(new NotificationDTO(notification))
        );

        return dtos;
    }

    public String getMessage() {
        return message;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public boolean isReport() {
        return report;
    }

    public String getId() {
        return id;
    }

}
