package com.example.serbUber.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="bell_notifications")
public class BellNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="message", nullable = false)
    private String message;

    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timeStamp;

    @Column(name = "seen", nullable = false)
    private boolean seen = false;

    @Column(name = "should_redirect", nullable = false)
    private boolean shouldRedirect = false;

    @Column(name = "redirect_id")
    private String redirectId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public BellNotification() {

    }

    public BellNotification(
            final String message,
            final LocalDateTime timeStamp,
            final boolean seen,
            final boolean shouldRedirect,
            final String redirectId,
            final Long userId
    ) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.seen = seen;
        this.shouldRedirect = shouldRedirect;
        this.redirectId = redirectId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isShouldRedirect() {
        return shouldRedirect;
    }

    public void setShouldRedirect(boolean shouldRedirect) {
        this.shouldRedirect = shouldRedirect;
    }

    public String getRedirectId() {
        return redirectId;
    }

    public void setRedirectId(String redirectId) {
        this.redirectId = redirectId;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
