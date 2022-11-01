package com.example.serbUber.model.user;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.util.Constants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Document(collection = "drivers")
public class Driver extends User {

    private Location currentLocation;
    private List<Driving> drivings = new LinkedList<>();
    private Vehicle vehicle;
    private LocalDateTime lastActive = null;
    private LocalDateTime startShift = null;
    private LocalDateTime endShift = null;
    private boolean blocked = false;
    private boolean active = false;
    private boolean verified = false;
    private double rate = Constants.START_RATE;
    private int workingMinutes = Constants.START_WORKING_MINUTES;

    public Driver() {
        super();
    }

    public Driver(
            final String id,
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture
    ) {
        super(id,email, password, name, surname, phoneNumber, city, profilePicture, new Role("ROLE_DRIVER"));

    }


    public Driver(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture,
        final Vehicle vehicle
    ) {
        super(email, password, name, surname, phoneNumber, city, profilePicture, new Role("ROLE_DRIVER"));
        this.vehicle = vehicle;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public List<Driving> getDrivings() {
        return drivings;
    }

    public void setDrivings(List<Driving> drivings) {
        this.drivings = drivings;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getLastActive() {
        return lastActive;
    }

    public void setLastActive(LocalDateTime lastActive) {
        this.lastActive = lastActive;
    }

    public LocalDateTime getStartShift() {
        return startShift;
    }

    public void setStartShift(LocalDateTime startShift) {
        this.startShift = startShift;
    }

    public LocalDateTime getEndShift() {
        return endShift;
    }

    public void setEndShift(LocalDateTime endShift) {
        this.endShift = endShift;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getWorkingMinutes() {
        return workingMinutes;
    }

    public void setWorkingMinutes(int workingMinutes) {
        this.workingMinutes = workingMinutes;
    }
}
