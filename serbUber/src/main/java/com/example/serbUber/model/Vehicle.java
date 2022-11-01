package com.example.serbUber.model;

import com.example.serbUber.util.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicle")
public class Vehicle {
    @Id
    private String id;

    private boolean petFriendly;
    private boolean babySeat;
    private VehicleTypeInfo vehicleTypeInfo;
    private double rate = Constants.START_RATE;

    public Vehicle() {

    }

    public Vehicle(
            final String id,
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleTypeInfo vehicleTypeInfo,
            final double rate) {
        this.id = id;
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
        this.vehicleTypeInfo = vehicleTypeInfo;
        this.rate = rate;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public boolean isBabySeat() {
        return babySeat;
    }

    public void setBabySeat(boolean babySeat) {
        this.babySeat = babySeat;
    }

    public VehicleTypeInfo getVehicleTypeInfo() {
        return vehicleTypeInfo;
    }

    public void setVehicleTypeInfo(VehicleTypeInfo vehicleTypeInfo) {
        this.vehicleTypeInfo = vehicleTypeInfo;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
