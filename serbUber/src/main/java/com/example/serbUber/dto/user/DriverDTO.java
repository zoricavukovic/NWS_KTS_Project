package com.example.serbUber.dto.user;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.user.Driver;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class DriverDTO extends UserDTO {
    private Long id;
    private boolean blocked;
    private boolean active;
    private double rate;
    private List<Driving> drives = new LinkedList<>();
    private int workingMinutes;
    private Vehicle vehicle;
    private LocalDateTime startShift;
    private LocalDateTime endShift;

    public DriverDTO(final Driver driver) {
        super(driver.getId(),
            driver.getEmail(),
            driver.getName(),
            driver.getSurname(),
            driver.getPhoneNumber(),
            driver.getCity(),
            driver.getProfilePicture(),
            driver.getRole(),
            driver.getPassword(),
            driver.isOnline()
        );
        this.blocked = driver.isBlocked();
        this.rate = driver.getRate();
        this.drives = driver.getDrivings();
        this.active = driver.isActive();
        this.id = driver.getId();
        this.workingMinutes = driver.getWorkingMinutes();
        this.vehicle = driver.getVehicle();
        this.startShift = driver.getStartShift();
        this.endShift = driver.getEndShift();
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

    public List<Driving> getDrives() {
        return drives;
    }

    public int getWorkingMinutes() {
        return workingMinutes;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getStartShift() {
        return startShift;
    }

    public LocalDateTime getEndShift() {
        return endShift;
    }


    public Long getId() {
        return id;
    }

    public static List<DriverDTO> fromDrivers(final List<Driver> drivers){
        List<DriverDTO> driverDTOs = new LinkedList<>();
        drivers.forEach(driver ->
            driverDTOs.add(new DriverDTO(driver))
        );

        return driverDTOs;
    }


}
