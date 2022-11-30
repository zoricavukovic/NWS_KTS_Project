package com.example.serbUber.dto;

import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.DrivingNotificationType;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;

import java.time.LocalDateTime;
import java.util.Set;

public class DrivingNotificationDTO {

    private double lonStarted;
    private double latStarted;
    private double lonEnd;
    private double latEnd;
    private double price;
    private String senderEmail;
    private String receiverEmail;
    private boolean read;
    private DrivingNotificationType drivingNotificationType;
    private String reason;

    private LocalDateTime started;

    private int duration;

    public DrivingNotificationDTO(final DrivingNotification drivingNotification){
        this.lonStarted = drivingNotification.getLonStarted();
        this.lonEnd = drivingNotification.getLonEnd();
        this.latStarted = drivingNotification.getLatStarted();
        this.latEnd = drivingNotification.getLatEnd();
        this.price = drivingNotification.getPrice();
        this.senderEmail = drivingNotification.getSender().getEmail();
        this.receiverEmail = drivingNotification.getReceiver().getEmail();
        this.read = drivingNotification.isRead();
        this.drivingNotificationType = drivingNotification.getDrivingNotificationType();
        this.reason = drivingNotification.getReason();
    }

    public double getLonStarted() {
        return lonStarted;
    }

    public void setLonStarted(double lonStarted) {
        this.lonStarted = lonStarted;
    }

    public double getLatStarted() {
        return latStarted;
    }

    public void setLatStarted(double latStarted) {
        this.latStarted = latStarted;
    }

    public double getLonEnd() {
        return lonEnd;
    }

    public void setLonEnd(double lonEnd) {
        this.lonEnd = lonEnd;
    }

    public double getLatEnd() {
        return latEnd;
    }

    public void setLatEnd(double latEnd) {
        this.latEnd = latEnd;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public DrivingNotificationType getDrivingNotificationType() {
        return drivingNotificationType;
    }

    public void setDrivingNotificationType(DrivingNotificationType drivingNotificationType) {
        this.drivingNotificationType = drivingNotificationType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
