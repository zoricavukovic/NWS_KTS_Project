package com.example.serbUber.dto;

import com.example.serbUber.model.*;
import java.util.LinkedList;
import java.util.List;

public class VehicleCurrentLocationDTO {

    private Long id;
    private Location currentLocation;
    private boolean inDrive;

    public VehicleCurrentLocationDTO(final Vehicle vehicle) {
        this.id = vehicle.getId();
        this.inDrive = vehicle.isInDrive();
        if (vehicle.hasRoute()) {
            this.currentLocation = vehicle.getLocationForIndexInRoute();
        }
    }


    public static List<VehicleCurrentLocationDTO> fromVehiclesToVehicleCurrentLocationDTO(List<Vehicle> vehicles) {
        List<VehicleCurrentLocationDTO> vehicleDTOs = new LinkedList<>();
        vehicles.forEach(vehicle ->
            vehicleDTOs.add(new VehicleCurrentLocationDTO(vehicle))
        );

        return vehicleDTOs;
    }

    public Long getId() {
        return id;
    }

    public boolean isInDrive() {
        return inDrive;
    }

    public void setInDrive(boolean inDrive) {
        this.inDrive = inDrive;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
