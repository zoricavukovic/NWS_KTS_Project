package com.example.serbUber.model;

import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.RegularUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="drivings")
public class Driving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="active", nullable = false)
    private boolean active = false;

    @Column(name="duration", nullable = false)
    private double duration;

    @Column(name="started")
    private LocalDateTime started;

    @Column(name="end_date")
    private LocalDateTime end;

    @Column(name="last_reminder")
    private LocalDateTime lastReminder;

    @OneToOne()
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    @Column(name="driving_status", nullable = false)
    private DrivingStatus drivingStatus;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="driver_id")
    @JsonIgnore
    private Driver driver;

    @ManyToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "drivings")
    @JsonIgnore
    private Set<RegularUser> users;

    @Column(name="price", nullable = false)
    private double price;

    @Column(name="reservation", nullable = false)
    private boolean reservation = false;


    public Driving() {
    }

    public Driving(
        final double duration,
        final LocalDateTime started,
        final LocalDateTime end,
        final Route route,
        final DrivingStatus drivingStatus,
        final Driver driver,
        final double price
    ) {
        this.duration = duration;
        this.started = started;
        this.end = end;
        this.route = route;
        this.drivingStatus = drivingStatus;
        this.driver = driver;
        this.price = price;
    }

    public Driving(
            final Long id,
            final double duration,
            final LocalDateTime started,
            final LocalDateTime end,
            final Route route,
            final DrivingStatus drivingStatus,
            final Driver driver,
            final double price
    ) {
        this.id = id;
        this.duration = duration;
        this.started = started;
        this.end = end;
        this.route = route;
        this.drivingStatus = drivingStatus;
        this.driver = driver;
        this.price = price;
    }

    public Driving(
            final Long id,
            final double duration,
            final LocalDateTime started,
            final LocalDateTime end,
            final Route route,
            final DrivingStatus drivingStatus,
            final Driver driver,
            final double price,
            final LocalDateTime lastReminder
    ) {
        this.id = id;
        this.duration = duration;
        this.started = started;
        this.end = end;
        this.route = route;
        this.drivingStatus = drivingStatus;
        this.driver = driver;
        this.price = price;
        this.lastReminder = lastReminder;
        this.setUsers(new HashSet<>());
    }

    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public DrivingStatus getDrivingStatus() {
        return drivingStatus;
    }

    public void setDrivingStatus(DrivingStatus drivingStatus) {
        this.drivingStatus = drivingStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @JsonIgnore
    public Set<RegularUser> getUsers() {
        return users;
    }

    public void setUsers(Set<RegularUser> users) {
        this.users = users;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getLastReminder() {
        return lastReminder;
    }

    public void setLastReminder(LocalDateTime last_reminder) {
        this.lastReminder = last_reminder;
    }

    public boolean isReservation() {
        return reservation;
    }

    public void setReservation(boolean reservation) {
        this.reservation = reservation;
    }
}
