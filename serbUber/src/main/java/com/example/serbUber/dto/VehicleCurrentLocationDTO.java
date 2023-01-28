package com.example.serbUber.dto;

import com.example.serbUber.model.*;
import java.util.LinkedList;
import java.util.List;

public class VehicleCurrentLocationDTO {

    private Long id;
    private Location currentLocation;
    private boolean inDrive;
    private VehicleType type;
    private long driverId;
    private boolean activeDriver;
    private int crossedWaypoints;
    private double timeToDestination;

    private int chosenRouteIdx;

    public VehicleCurrentLocationDTO() {

    }

    public VehicleCurrentLocationDTO(final VehicleWithDriverId vehicleWithDriverId) {
        this.id = vehicleWithDriverId.getVehicle().getId();
        this.type = vehicleWithDriverId.getVehicle().getVehicleTypeInfo().getVehicleType();
        this.crossedWaypoints = vehicleWithDriverId.getVehicle().getCrossedWaypoints();
        this.activeDriver = vehicleWithDriverId.isActiveDriver();
        this.driverId = vehicleWithDriverId.getDriverId();
        if (vehicleWithDriverId.getVehicle().hasRoute()){
            this.inDrive = true;
            this.currentLocation = vehicleWithDriverId.getVehicle().getCurrentStop();
        }
        else {
            this.inDrive = false;
        }
        this.currentLocation = vehicleWithDriverId.getVehicle().getCurrentStop();
        this.timeToDestination = 0;
        this.chosenRouteIdx = 0;
    }

    public VehicleCurrentLocationDTO(final VehicleWithDriverId vehicleWithDriverId, final int chosenRouteIdx) {
        this.id = vehicleWithDriverId.getVehicle().getId();
        this.type = vehicleWithDriverId.getVehicle().getVehicleTypeInfo().getVehicleType();
        this.crossedWaypoints = vehicleWithDriverId.getVehicle().getCrossedWaypoints();
        this.activeDriver = vehicleWithDriverId.isActiveDriver();
        this.driverId = vehicleWithDriverId.getDriverId();
        if (vehicleWithDriverId.getVehicle().hasRoute()){
            this.inDrive = true;
            this.currentLocation = vehicleWithDriverId.getVehicle().getCurrentStop();
        }
        else {
            this.inDrive = false;
        }
        this.currentLocation = vehicleWithDriverId.getVehicle().getCurrentStop();
        this.timeToDestination = 0;
        this.chosenRouteIdx = chosenRouteIdx;
    }



    public static List<VehicleCurrentLocationDTO> fromVehiclesToVehicleCurrentLocationDTO(
        final List<VehicleWithDriverId> vehicles
    ) {

        List<VehicleCurrentLocationDTO> vehicleDTOs = new LinkedList<>();
        vehicles.forEach(vehicle ->
            vehicleDTOs.add(new VehicleCurrentLocationDTO(vehicle))
        );

        return vehicleDTOs;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
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

    public boolean isActiveDriver() {
        return activeDriver;
    }

    public void setActiveDriver(boolean activeDriver) {
        this.activeDriver = activeDriver;
    }

    public int getCrossedWaypoints() {
        return crossedWaypoints;
    }

    public void setCrossedWaypoints(int crossedWaypoints) {
        this.crossedWaypoints = crossedWaypoints;
    }

    public double getTimeToDestination() {
        return timeToDestination;
    }

    public void setTimeToDestination(double timeToDestination) {
        this.timeToDestination = timeToDestination;
    }

    public int getChosenRouteIdx() {
        return chosenRouteIdx;
    }

    public void setChosenRouteIdx(int chosenRouteIdx) {
        this.chosenRouteIdx = chosenRouteIdx;
    }
}
