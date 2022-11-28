package com.example.serbUber.model;

import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;

import javax.persistence.*;

@Entity
@Table(name="driving_notifications")
public class DrivingNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lon_started")
    private double lonStarted;
    @Column(name = "lat_started")
    private double latStarted;
    @Column(name = "lon_end")
    private double lonEnd;
    @Column(name = "lat_end")
    private double latEnd;

    @Column(name = "price")
    private double price;

    @Column(name = "read")
    private boolean read;

    @Column(name = "reason")
    private String reason;

    @Column(name = "driving_notification_type", nullable = false)
    private DrivingNotificationType drivingNotificationType;

    @OneToOne()
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @OneToOne()
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    public DrivingNotification(
        final double lonStarted,
        final double latStarted,
        final double lonEnd,
        final double latEnd,
        final double price,
        final User sender,
        final User receiver,
        final DrivingNotificationType type
    ) {
        this.lonStarted = lonStarted;
        this.latStarted = latStarted;
        this.lonEnd = lonEnd;
        this.latEnd = latEnd;
        this.price = price;
        this.sender = sender;
        this.receiver = receiver;
        this.read = false;
        this.drivingNotificationType = type;
    }

    public DrivingNotification(
        final double lonStarted,
        final double latStarted,
        final double lonEnd,
        final double latEnd,
        final double price,
        final User sender,
        final User receiver,
        final DrivingNotificationType type,
        final String reason
    ) {
        this.lonStarted = lonStarted;
        this.latStarted = latStarted;
        this.lonEnd = lonEnd;
        this.latEnd = latEnd;
        this.price = price;
        this.sender = sender;
        this.receiver = receiver;
        this.read = false;
        this.drivingNotificationType = type;
        this.reason = reason;
    }

    public DrivingNotification() {}

    public Long getId() {
        return id;
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

    public DrivingNotificationType getDrivingNotificationType() {
        return drivingNotificationType;
    }

    public void setDrivingNotificationType(DrivingNotificationType drivingNotificationType) {
        this.drivingNotificationType = drivingNotificationType;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

