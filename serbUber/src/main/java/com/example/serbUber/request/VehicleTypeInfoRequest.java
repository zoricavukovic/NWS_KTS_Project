package com.example.serbUber.request;

import com.example.serbUber.model.VehicleType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class VehicleTypeInfoRequest {

    @NotNull(message = "Vehicle must exist!")
    private VehicleType vehicleType;

    @NotNull
    @Min(value = 1, message = "Start price must be greater than 1!")
    private double startPrice;

    @NotNull
    @Min(value = 1, message = "Number of seats must be greater than 1!")
    private int numOfSeats;

    public VehicleTypeInfoRequest(
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

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public void setNumOfSeats(int numOfSeats) {
        this.numOfSeats = numOfSeats;
    }
}
