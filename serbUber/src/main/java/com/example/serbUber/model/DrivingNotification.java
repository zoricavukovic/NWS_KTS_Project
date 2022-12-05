package com.example.serbUber.model;

import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="driving_notifications")
public class DrivingNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    @Column(name = "price")
    private double price;

    @OneToOne()
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private RegularUser sender;

    @Column(name="started")
    private LocalDateTime started;

    @Column(name="duration")
    private int duration;

    @Column(name="baby_seat")
    private boolean babySeat;

    @Column(name="pet_friendly")
    private boolean petFriendly;

    @OneToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "passenger_driving_notifications", joinColumns = @JoinColumn(name = "notification_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<RegularUser> receivers;

    @Column(name="answered_passengers")
    private int answeredPassengers = 0;

    public DrivingNotification(
            final Route route,
            final double price,
            final RegularUser sender,
            final LocalDateTime started,
            final int duration,
            final boolean babySeat,
            final boolean petFriendly,
            final Vehicle vehicle,
            final Set<RegularUser> receivers
            ) {
        this.route = route;
        this.price = price;
        this.sender = sender;
        this.started = started;
        this.duration = duration;
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
        this.vehicle = vehicle;
        this.receivers = receivers;
    }

    public DrivingNotification() {}

    public Long getId() {
        return id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public RegularUser getSender() {
        return sender;
    }

    public void setSender(RegularUser sender) {
        this.sender = sender;
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

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isBabySeat() {
        return babySeat;
    }

    public void setBabySeat(boolean babySeat) {
        this.babySeat = babySeat;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Set<RegularUser> getReceivers() {
        return receivers;
    }

    public void setReceivers(Set<RegularUser> receivers) {
        this.receivers = receivers;
    }

    public int getAnsweredPassengers() {
        return answeredPassengers;
    }

    public void setAnsweredPassengers(int answeredPassengers) {
        this.answeredPassengers = answeredPassengers;
    }
}

