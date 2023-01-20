package com.example.serbUber.dto.bell;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.model.BellNotification;
import com.example.serbUber.model.Driving;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class BellNotificationDTO {

    private final Long id;
    private final String message;
    private final LocalDateTime timeStamp;
    private final boolean seen;
    private final boolean shouldRedirect;
    private final String redirectId;
    private final Long userId;

    public BellNotificationDTO(
            final Long id,
            final String message,
            final LocalDateTime timeStamp,
            final boolean shouldRedirect,
            final boolean seen,
            final String redirectId,
            final Long userId
    ) {
        this.id = id;
        this.message = message;
        this.timeStamp = timeStamp;
        this.shouldRedirect = shouldRedirect;
        this.seen = seen;
        this.redirectId = redirectId;
        this.userId = userId;
    }

    public BellNotificationDTO(final BellNotification bellNotification) {
        this.id = bellNotification.getId();
        this.message = bellNotification.getMessage();
        this.timeStamp = bellNotification.getTimeStamp();
        this.shouldRedirect = bellNotification.isShouldRedirect();
        this.seen = bellNotification.isSeen();
        this.redirectId = bellNotification.getRedirectId();
        this.userId = bellNotification.getUserId();
    }

    public static List<BellNotificationDTO> fromBellNotifications(final List<BellNotification> notifications){
        List<BellNotificationDTO> bellNotificationDTOS = new LinkedList<>();
        notifications.forEach(notification ->
                bellNotificationDTOS.add(new BellNotificationDTO(notification))
        );

        return bellNotificationDTOS;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public boolean isShouldRedirect() {
        return shouldRedirect;
    }

    public String getRedirectId() {
        return redirectId;
    }

    public Long getUserId() {
        return userId;
    }
}
