package com.example.serbUber.model;

import com.example.serbUber.model.user.RegularUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
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
    private double duration;

    @Column(name="baby_seat")
    private boolean babySeat;

    @Column(name="pet_friendly")
    private boolean petFriendly;

    @OneToOne
    @JoinColumn(name = "vehicle_type_info", referencedColumnName = "id")
    private VehicleTypeInfo vehicleTypeInfo;

    @ElementCollection
    private Map<RegularUser, Integer> receiversReviewed;

    @Column(name="is_reservation")
    private boolean reservation = false;

    @Column(name="notified")
    private boolean notified = false;

    @Column(name="created_reservation")
    private LocalDateTime createdReservation;

    public DrivingNotification(
            final Route route,
            final double price,
            final RegularUser sender,
            final LocalDateTime started,
            final double duration,
            final boolean babySeat,
            final boolean petFriendly,
            final VehicleTypeInfo vehicleTypeInfo,
            final Map<RegularUser, Integer> receiversReviewed,
            final boolean reservation
    ) {
        this.route = route;
        this.price = price;
        this.sender = sender;
        this.started = started;
        this.duration = duration;
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
        this.vehicleTypeInfo = vehicleTypeInfo;
        this.receiversReviewed = receiversReviewed;
        this.reservation = reservation;
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

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
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

    public VehicleTypeInfo getVehicleTypeInfo() {
        return vehicleTypeInfo;
    }

    public void setVehicleTypeInfo(VehicleTypeInfo vehicleTypeInfo) {
        this.vehicleTypeInfo = vehicleTypeInfo;
    }

    public Map<RegularUser, Integer> getReceiversReviewed() {
        return receiversReviewed;
    }

    public void setReceiversReviewed(Map<RegularUser, Integer> receiversReviewed) {
        this.receiversReviewed = receiversReviewed;
    }

    public static Set<RegularUser> getListOfUsers(Map<RegularUser, Integer> receiversReviewed){
        Set<RegularUser> users = new HashSet<>();
        receiversReviewed.forEach((key, value)-> users.add(key));

        return users;
    }

    public boolean isReservation() {
        return reservation;
    }

    public void setReservation(boolean reservation) {
        this.reservation = reservation;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public LocalDateTime getCreatedReservation() {
        return createdReservation;
    }

    public void setCreatedReservation(LocalDateTime createdReservation) {
        this.createdReservation = createdReservation;
    }
}

