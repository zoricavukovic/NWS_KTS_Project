package com.example.serbUber.model;

public class VehicleTypeInfo {
    private VehicleType vehicleType;
    private double startPrice;
    private int numOfSeats;

    public VehicleTypeInfo(
        final VehicleType vehicleType,
        final double startPrice,
        final int numOfSeats
    ) {
        this.vehicleType = vehicleType;
        this.startPrice = startPrice;
        this.numOfSeats = numOfSeats;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }
}
