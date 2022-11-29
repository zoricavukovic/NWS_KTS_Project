package com.example.serbUber.model.user;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.util.Constants;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.util.Constants.ROLE_DRIVER;

@Entity
@Table(name="drivers")
public class Driver extends User {

    @OneToOne()
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location currentLocation;

    @OneToMany()
    @JoinColumn(name = "driving_id", referencedColumnName = "id")
    private List<Driving> drivings = new LinkedList<>();

    @OneToOne()
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    @Column(name="last_active")
    private LocalDateTime lastActive = null;

    @Column(name="start_shift")
    private LocalDateTime startShift = null;

    @Column(name="end_shift")
    private LocalDateTime endShift = null;

    @Column(name="blocked")
    private boolean blocked = false;

    @Column(name="active")
    private boolean active = false;

    @Column(name="rate")
    private double rate = Constants.START_RATE;

    @Column(name="drive")
    private boolean drive = false;

    @Column(name="working_minutes")
    private int workingMinutes = Constants.START_WORKING_MINUTES;

    public Driver() {
        super();
    }

    public Driver(
            final Long id,
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture
    ) {
        super(id,email, password, name, surname, phoneNumber, city, profilePicture, new Role(ROLE_DRIVER));

    }

    public Driver(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture,
        final Vehicle vehicle,
        final Role role
    ) {
        super(email, password, name, surname, phoneNumber, city, profilePicture, role);
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

    public boolean isDrive() {
        return drive;
    }

    public void setDrive(boolean drive) {
        this.drive = drive;
    }
}
