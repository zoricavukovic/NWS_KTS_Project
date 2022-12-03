package com.example.serbUber.model;

import com.example.serbUber.model.user.RegularUser;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.annotations.FetchMode.SELECT;

@Entity
@Table(name="drivings")
public class Driving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="active", nullable = false)
    private boolean active = false;

    @Column(name="duration", nullable = false)
    private int duration;

    @Column(name="started")
    private LocalDateTime started;

    @Column(name="end_date")
    private LocalDateTime end;

    @Column(name="paying_limit")
    private LocalDateTime payingLimit;

    @OneToOne()
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    @Column(name="driving_status", nullable = false)
    private DrivingStatus drivingStatus;

    @Column(name="driver_id", nullable = false)
    private Long driverId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "drivings_users", joinColumns = @JoinColumn(name = "driving_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<RegularUser> users;

    @ElementCollection
    private Map<Long, Boolean> usersPaid = new HashMap<>();

    @Column(name="price", nullable = false)
    private double price;


    public Driving() {
    }

    public Driving(int duration, LocalDateTime started, LocalDateTime end, LocalDateTime payingLimit, Route route, DrivingStatus drivingStatus, Long driverId, Set<RegularUser> users, Map<Long, Boolean> usersPaid, double price) {
        this.duration = duration;
        this.started = started;
        this.end = end;
        this.payingLimit = payingLimit;
        this.route = route;
        this.drivingStatus = drivingStatus;
        this.driverId = driverId;
        this.users = users;
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

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
