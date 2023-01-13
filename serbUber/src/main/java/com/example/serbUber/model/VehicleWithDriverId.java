package com.example.serbUber.model;

public class VehicleWithDriverId {
    private Vehicle vehicle;
    private Long driverId;

    public VehicleWithDriverId(Vehicle vehicle, Long driverId) {
        this.vehicle = vehicle;
        this.driverId = driverId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Long getDriverId() {
        return driverId;
    }
}
