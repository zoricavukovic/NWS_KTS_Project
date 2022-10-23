package com.example.serbUber.model;

public class Review {
    private double vehicleRate;
    private double driverRate;
    private String message;
    private Driving driving;

    public Review(
        final double vehicleRate,
        final double driverRate,
        final String message,
        final Driving driving
    ) {
        this.vehicleRate = vehicleRate;
        this.driverRate = driverRate;
        this.message = message;
        this.driving = driving;
    }

    public double getVehicleRate() {
        return vehicleRate;
    }

    public double getDriverRate() {
        return driverRate;
    }

    public String getMessage() {
        return message;
    }

    public Driving getDriving() {
        return driving;
    }
}
