package com.example.serbUber.dto.user;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.user.Driver;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class DriverDTO {


    private String email;
    private String name;
    private String surname;
    private String phoneNumber;
    private String city;
    private String profilePicture;
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

    public DriverDTO(final Driver driver) {
        this.email = driver.getEmail();
        this.name = driver.getName();
        this.surname = driver.getSurname();
        this.phoneNumber = driver.getPhoneNumber();
        this.city = driver.getCity();
        this.profilePicture = driver.getProfilePicture();
        this.blocked = driver.isBlocked();
        this.rate = driver.getRate();
        this.currentLocation = currentLocation;
        this.drives = drives;
        this.active = active;
        this.workingMinutes = workingMinutes;
        this.lastActive = lastActive;
        this.vehicle = vehicle;
        this.startShift = startShift;
        this.endShift = endShift;
    }

    public static List<DriverDTO> fromDrivers(final List<Driver> drivers){
        List<DriverDTO> driverDTOs = new LinkedList<>();
        drivers.forEach(driver ->
            driverDTOs.add(new DriverDTO(driver))
        );

        return driverDTOs;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
