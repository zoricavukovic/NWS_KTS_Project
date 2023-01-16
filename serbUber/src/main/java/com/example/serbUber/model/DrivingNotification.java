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
    private int duration;

    @Column(name="baby_seat")
    private boolean babySeat;

    @Column(name="pet_friendly")
    private boolean petFriendly;

    @OneToOne
    @JoinColumn(name = "vehicle_type_info", referencedColumnName = "id")
    private VehicleTypeInfo vehicleTypeInfo;

    @ElementCollection
    private Map<RegularUser, Integer> receiversReviewed;

    public DrivingNotification(
            final Route route,
            final double price,
            final RegularUser sender,
            final LocalDateTime started,
            final int duration,
            final boolean babySeat,
            final boolean petFriendly,
            final VehicleTypeInfo vehicleTypeInfo,
            final Map<RegularUser, Integer> receiversReviewed
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
}

