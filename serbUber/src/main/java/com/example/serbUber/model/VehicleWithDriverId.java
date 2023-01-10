package com.example.serbUber.model;

public class VehicleWithDriverId {
    private Vehicle vehicle;
    private long driverId;

    public VehicleWithDriverId(Vehicle vehicle, long driverId) {
        this.vehicle = vehicle;
        this.driverId = driverId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public long getDriverId() {
        return driverId;
    }
}
