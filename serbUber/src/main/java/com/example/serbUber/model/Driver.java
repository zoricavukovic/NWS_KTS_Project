package com.example.serbUber.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Driver extends User {

    private boolean blocked;
    private boolean active;
    private double rate;
    private Location currentLocation;
    private List<Driving> drives = new LinkedList<>();
    private int workingMinutes;
    private Vehicle vehicle;
    private LocalDateTime lastActive;
    private LocalDateTime startShift;
    private LocalDateTime endShift;

    public Driver(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final Location address,
        final String profilePicture,
        final boolean blocked,
        final double rate,
        final Location currentLocation,
        final List<Driving> drives,
        final boolean active,
        final int workingMinutes,
        final LocalDateTime lastActive,
        final Vehicle vehicle,
        final LocalDateTime startShift,
        final LocalDateTime endShift
    ) {
        super(email, password, name, surname, phoneNumber, address, profilePicture);
        this.blocked = blocked;
        this.rate = rate;
        this.currentLocation = currentLocation;
        this.drives = drives;
        this.active = active;
        this.workingMinutes = workingMinutes;
        this.lastActive = lastActive;
        this.vehicle = vehicle;
        this.startShift = startShift;
        this.endShift = endShift;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isActive() {
        return active;
    }

    public double getRate() {
        return rate;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public List<Driving> getDrives() {
        return drives;
    }

    public int getWorkingMinutes() {
        return workingMinutes;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getLastActive() {
        return lastActive;
    }

    public LocalDateTime getStartShift() {
        return startShift;
    }

    public LocalDateTime getEndShift() {
        return endShift;
    }
}
