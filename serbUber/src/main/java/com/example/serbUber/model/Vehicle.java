package com.example.serbUber.model;

public class Vehicle {
    private boolean petFriendly;
    private boolean babySeat;
    private VehicleTypeInfo vehicleTypeInfo;
    private double rate;

    public Vehicle(
        final boolean petFriendly,
        final boolean babySeat,
        final VehicleTypeInfo vehicleTypeInfo,
        final double rate
    ) {
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
        this.vehicleTypeInfo = vehicleTypeInfo;
        this.rate = rate;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public boolean isBabySeat() {
        return babySeat;
    }

    public VehicleTypeInfo getVehicleTypeInfo() {
        return vehicleTypeInfo;
    }

    public double getRate() {
        return rate;
    }
}
