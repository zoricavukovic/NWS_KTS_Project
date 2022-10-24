package com.example.serbUber.dto;

import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.VehicleTypeInfo;

import java.util.LinkedList;
import java.util.List;

public class VehicleDTO {

    private boolean petFriendly;
    private boolean babySeat;
    private VehicleTypeInfo vehicleTypeInfo;
    private double rate;

    public VehicleDTO(final Vehicle vehicle) {
        this.petFriendly = vehicle.isPetFriendly();
        this.babySeat = vehicle.isBabySeat();
        this.vehicleTypeInfo = vehicle.getVehicleTypeInfo();
        this.rate = vehicle.getRate();
    }

    public static List<VehicleDTO> fromVehicles(List<Vehicle> vehicles) {
        List<VehicleDTO> vehicleDTOs = new LinkedList<>();
        vehicles.forEach(v ->
                vehicleDTOs.add(new VehicleDTO(v))
        );

        return vehicleDTOs;
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

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public void setBabySeat(boolean babySeat) {
        this.babySeat = babySeat;
    }

    public void setVehicleTypeInfo(VehicleTypeInfo vehicleTypeInfo) {
        this.vehicleTypeInfo = vehicleTypeInfo;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
