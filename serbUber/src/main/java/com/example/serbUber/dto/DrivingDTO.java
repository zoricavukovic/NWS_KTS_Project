package com.example.serbUber.dto;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.RegularUser;

import java.time.LocalDateTime;
import java.util.*;

public class DrivingDTO {

    private Long id;
    private boolean active;
    private int duration;
    private LocalDateTime started;
    private LocalDateTime payingLimit;
    private Route route;
    private DrivingStatus drivingStatus;
    private Long driverId;
    private Set<RegularUser> users;
    private Map<Long, Boolean> usersPaid = new HashMap<>();
    private double price;

    private boolean hasReviewForUser = false;

    public DrivingDTO(final Driving driving){
        this.id = driving.getId();
        this.active = driving.isActive();
        this.duration = driving.getDuration();
        this.started = driving.getStarted();
        this.payingLimit = driving.getPayingLimit();
        this.route = driving.getRoute();
        this.drivingStatus = driving.getDrivingStatus();
        this.driverId = driving.getDriverId();
        this.usersPaid = driving.getUsersPaid();
        this.users = setPictureForUsers(driving.getUsers());
        this.price = driving.getPrice();
        this.hasReviewForUser = false;
    }

    private Set<RegularUser> setPictureForUsers(Set<RegularUser> users){

        return users;
    }


    public static List<DrivingDTO> fromDrivings(final List<Driving> drivings){
        List<DrivingDTO> drivingDTOs = new LinkedList<>();
        drivings.forEach(driving ->
                drivingDTOs.add(new DrivingDTO(driving))
        );

        return drivingDTOs;
    }

    public boolean isActive() {
        return active;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public LocalDateTime getPayingLimit() {
        return payingLimit;
    }

    public Route getRoute() {
        return route;
    }

    public DrivingStatus getDrivingStatus() {
        return drivingStatus;
    }

    public Long getDriverId() {
        return driverId;
    }

    public Map<Long, Boolean> getUsersPaid() {
        return usersPaid;
    }

    public double getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public boolean isHasReviewForUser() {
        return hasReviewForUser;
    }

    public Set<RegularUser> getUsers() {
        return users;
    }

    public void setUsers(Set<RegularUser> users) {
        this.users = users;
    }
}
