package com.example.serbUber.dto;

import com.example.serbUber.model.*;
import java.util.LinkedList;
import java.util.List;

public class VehicleCurrentLocationDTO {

    private Long id;
    private Location currentLocation;
    private boolean inDrive;

    public VehicleCurrentLocationDTO(final Vehicle vehicle, List<double[]> vehicleRoute) {
        this.id = vehicle.getId();
        this.inDrive = vehicle.isInDrive();
        if (vehicle.hasRoute()) {
            this.currentLocation = vehicle.getLocationForIndexInRoute(vehicleRoute.get(vehicle.getCurrentLocationIndex()));
        }
    }


    public static List<VehicleCurrentLocationDTO> fromVehiclesToVehicleCurrentLocationDTO(
        final List<Vehicle> vehicles,
        final List<List<double[]>> vehiclesRoutePath
    ) {

        List<VehicleCurrentLocationDTO> vehicleDTOs = new LinkedList<>();
        vehicles.forEach(vehicle ->
            vehicleDTOs.add(new VehicleCurrentLocationDTO(vehicle, vehiclesRoutePath.get(vehicles.indexOf(vehicle))))
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
