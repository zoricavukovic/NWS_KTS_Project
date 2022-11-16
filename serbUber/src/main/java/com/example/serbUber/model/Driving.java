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

    @Column(name="driver_id", nullable = false)
    private Long driverId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "drivings_users", joinColumns = @JoinColumn(name = "driving_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<RegularUser> users;

    @ElementCollection
    private Map<Long, Boolean> usersPaid = new HashMap<>();

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
        final Long driverId,
        final HashMap<Long, Boolean> usersPaid,
        final double price
    ) {
        this.active = active;
        this.duration = duration;
        this.started = started;
        this.payingLimit = payingLimit;
        this.route = route;
        this.drivingStatus = drivingStatus;
        this.driverId = driverId;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public void setUsersPaid(Map<Long, Boolean> usersPaid) {
        this.usersPaid = usersPaid;
    }
    public Map<Long, Boolean> getUsersPaid() {
        return usersPaid;
    }

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

}
