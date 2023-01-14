package com.example.serbUber.dto;

import com.example.serbUber.model.VehicleWithDriverId;

import java.util.LinkedList;
import java.util.List;

public class VehicleCurrentLocationForLocustDTO {

    private Long vehicleId;
    private List<LngLatLiteralDTO> waypoints = new LinkedList<>();
    private List<Integer> chosenRouteIdx = new LinkedList<>();
    private boolean inDrive;
    private Long driverId;
    private int currentIndexOfLocation;
    private int crossedWaypoints = 0;

    public VehicleCurrentLocationForLocustDTO(final VehicleWithDriverId vehicleWithDriverId) {
        this.vehicleId = vehicleWithDriverId.getVehicle().getId();
        this.driverId = vehicleWithDriverId.getDriverId();
        vehicleWithDriverId.getVehicle().getActiveRoute().getLocations().forEach(drivingLocationIndex -> {
                this.waypoints.add(new LngLatLiteralDTO(drivingLocationIndex.getLocation().getLat(), drivingLocationIndex.getLocation().getLat()));
                this.chosenRouteIdx.add(drivingLocationIndex.getRouteIndex());
            }
        );
       this.currentIndexOfLocation = vehicleWithDriverId.getVehicle().getCurrentLocationIndex();
    }

    public static List<VehicleCurrentLocationForLocustDTO> fromVehiclesToVehicleCurrentLocationForLocustDTO(
        final List<VehicleWithDriverId> vehicles
    ) {
        List<VehicleCurrentLocationForLocustDTO> vehicleDTOs = new LinkedList<>();
        vehicles.forEach(vehicle ->
            vehicleDTOs.add(new VehicleCurrentLocationForLocustDTO(vehicle))
        );

        return vehicleDTOs;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public boolean isInDrive() {
        return inDrive;
    }

    public void setInDrive(boolean inDrive) {
        this.inDrive = inDrive;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public List<LngLatLiteralDTO> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<LngLatLiteralDTO> waypoints) {
        this.waypoints = waypoints;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public int getCurrentIndexOfLocation() {
        return currentIndexOfLocation;
    }

    public void setCurrentIndexOfLocation(int currentIndexOfLocation) {
        this.currentIndexOfLocation = currentIndexOfLocation;
    }

    public List<Integer> getChosenRouteIdx() {
        return chosenRouteIdx;
    }

    public void setChosenRouteIdx(List<Integer> chosenRouteIdx) {
        this.chosenRouteIdx = chosenRouteIdx;
    }

    public int getCrossedWaypoints() {
        return crossedWaypoints;
    }

    public void setCrossedWaypoints(int crossedWaypoints) {
        this.crossedWaypoints = crossedWaypoints;
    }
}
