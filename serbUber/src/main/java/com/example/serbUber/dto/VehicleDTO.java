package com.example.serbUber.dto;

import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.VehicleTypeInfo;

import java.util.LinkedList;
import java.util.List;

public class VehicleDTO {

    private Long id;
    private boolean petFriendly;
    private boolean babySeat;
    private VehicleTypeInfo vehicleTypeInfo;
    private double rate;
    private int currentLocationIndex;
    private Route activeRoute;

    public VehicleDTO(final Vehicle vehicle) {
        this.id = vehicle.getId();
        this.petFriendly = vehicle.isPetFriendly();
        this.babySeat = vehicle.isBabySeat();
        this.vehicleTypeInfo = vehicle.getVehicleTypeInfo();
        this.rate = vehicle.getRate();
    }

    public VehicleDTO(Vehicle vehicle, int currentLocationIndex, Route activeRoute) {
        this.id = vehicle.getId();
        this.petFriendly = vehicle.isPetFriendly();
        this.babySeat = vehicle.isBabySeat();
        this.vehicleTypeInfo = vehicle.getVehicleTypeInfo();
        this.rate = vehicle.getRate();
        this.currentLocationIndex = currentLocationIndex;
        this.activeRoute = activeRoute;
    }

    public static Vehicle toVehicle(VehicleDTO vehicleDTO) {

        return new Vehicle(
                vehicleDTO.getId(),
                vehicleDTO.isPetFriendly(),
                vehicleDTO.isBabySeat(),
                vehicleDTO.getVehicleTypeInfo(),
                vehicleDTO.getRate()
        );
    }

    public static List<VehicleDTO> fromVehicles(List<Vehicle> vehicles) {
        List<VehicleDTO> vehicleDTOs = new LinkedList<>();
        vehicles.forEach(vehicle ->
                vehicleDTOs.add(new VehicleDTO(vehicle))
        );

        return vehicleDTOs;
    }

    public static List<VehicleDTO> fromVehiclesWithAdditionalFields(List<Vehicle> vehicles) {
        List<VehicleDTO> vehicleDTOs = new LinkedList<>();
//        vehicles.forEach(vehicle ->
//            vehicleDTOs.add(new VehicleDTO(vehicle, vehicle.getCurrentLocationIndex(), vehicle.getActiveRoute()))
//        );

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

    public Long getId() {
        return id;
    }

    public int getCurrentLocationIndex() {
        return currentLocationIndex;
    }

    public void setCurrentLocationIndex(int currentLocationIndex) {
        this.currentLocationIndex = currentLocationIndex;
    }

    public Route getActiveRoute() {
        return activeRoute;
    }

    public void setActiveRoute(Route activeRoute) {
        this.activeRoute = activeRoute;
    }
}
