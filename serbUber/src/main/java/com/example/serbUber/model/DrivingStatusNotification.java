package com.example.serbUber.model;

import javax.persistence.*;

@Entity
@Table(name="driving-status-notifications")
public class DrivingStatusNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="reason")
    private String reason;

    @Column(name="driving_status")
    private DrivingStatus drivingStatus;

    @OneToOne()
    @JoinColumn(name = "driving_id", referencedColumnName = "id")
    private Driving driving;

    @JoinColumn(name="read")
    private boolean read = false;


    public DrivingStatusNotification() {
    }

    public DrivingStatusNotification(String reason, DrivingStatus drivingStatus, Driving driving) {
        this.reason = reason;
        this.drivingStatus = drivingStatus;
        this.driving = driving;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DrivingStatus getDrivingStatus() {
        return drivingStatus;
    }

    public void setDrivingStatus(DrivingStatus drivingStatus) {
        this.drivingStatus = drivingStatus;
    }

    public Driving getDriving() {
        return driving;
    }

    public void setDriving(Driving driving) {
        this.driving = driving;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
