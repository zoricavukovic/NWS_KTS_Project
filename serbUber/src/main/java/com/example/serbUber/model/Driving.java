package com.example.serbUber.model;

import com.example.serbUber.model.user.RegularUser;

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
    private boolean active;

    @Column(name="duration", nullable = false)
    private int duration;

    @Column(name="started")
    private LocalDateTime started;

    @Column(name="paying_limit")
    private LocalDateTime payingLimit;

    @OneToOne()
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    @Column(name="driving_status", nullable = false)
    private DrivingStatus drivingStatus;

    @Column(name="driver_email", nullable = false)
    private String driverEmail;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "drivings_users", joinColumns = @JoinColumn(name = "driving_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<RegularUser> users;

    @ElementCollection
    private Map<String, Boolean> usersPaid = new HashMap<String, Boolean>();

    @Column(name="price", nullable = false)
    private double price;


    public Driving() {
    }

    public Driving(
        final boolean active,
        final int duration,
        final LocalDateTime started,
        final LocalDateTime payingLimit,
        final Route route,
        final DrivingStatus drivingStatus,
        final String driverEmail,
        final HashMap<String, Boolean> usersPaid,
        final double price
    ) {
        this.active = active;
        this.duration = duration;
        this.started = started;
        this.payingLimit = payingLimit;
        this.route = route;
        this.drivingStatus = drivingStatus;
        this.driverEmail = driverEmail;
        this.usersPaid = usersPaid;
        this.price = price;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getPayingLimit() {
        return payingLimit;
    }

    public void setPayingLimit(LocalDateTime payingLimit) {
        this.payingLimit = payingLimit;
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

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public Set<RegularUser> getUsers() {
        return users;
    }

    public void setUsers(Set<RegularUser> users) {
        this.users = users;
    }

    public Map<String, Boolean> getUsersPaid() {
        return usersPaid;
    }

    public void setUsersPaid(Map<String, Boolean> usersPaid) {
        this.usersPaid = usersPaid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
