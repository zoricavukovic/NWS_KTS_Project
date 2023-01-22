package com.example.serbUber.model;

public class VehicleWithDriverId {
    private Vehicle vehicle;
    private Long driverId;
    private boolean activeDriver;

    public VehicleWithDriverId(final Vehicle vehicle, final Long driverId, final boolean activeDriver) {
        this.vehicle = vehicle;
        this.driverId = driverId;
        this.activeDriver = activeDriver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Long getDriverId() {
        return driverId;
    }

    public boolean isActiveDriver() {
        return activeDriver;
    }
}
