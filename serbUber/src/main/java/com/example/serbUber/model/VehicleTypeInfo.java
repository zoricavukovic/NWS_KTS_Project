package com.example.serbUber.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicleTypeInfos")
public class VehicleTypeInfo {
    @Id
    private String id;

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

    public String getId() { return id; }

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
