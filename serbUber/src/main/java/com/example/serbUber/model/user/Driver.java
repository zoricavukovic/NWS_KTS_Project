package com.example.serbUber.model.user;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Vehicle;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Document(collection = "drivers")
public class Driver extends User {

    private final double START_RATE = 0.0;
    private final int START_WORKING_MINUTES = 0;

    private Location currentLocation;
    private List<Driving> drives = new LinkedList<>();
    private Vehicle vehicle;
    private LocalDateTime lastActive = null;
    private LocalDateTime startShift = null;
    private LocalDateTime endShift = null;
    private boolean blocked = false;
    private boolean active = false;
    private double rate = START_RATE;
    private int workingMinutes = START_WORKING_MINUTES;

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
        super(email, password, name, surname, phoneNumber, address, profilePicture, new Role("ROLE_DRIVER"));
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

    public Driver(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final Location address,
        final String profilePicture,
        final Vehicle vehicle
    ) {
        super(email, password, name, surname, phoneNumber, address, profilePicture, new Role("ROLE_DRIVER"));
        this.vehicle = vehicle;
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
