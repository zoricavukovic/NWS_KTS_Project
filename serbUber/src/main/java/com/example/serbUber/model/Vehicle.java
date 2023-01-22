package com.example.serbUber.model;

import com.example.serbUber.model.user.Driver;
import com.example.serbUber.util.Constants;

import javax.persistence.*;

@Entity
@Table(name="vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="pet_friendly", nullable = false)
    private boolean petFriendly;

    @Column(name="baby_seat", nullable = false)
    private boolean babySeat;

    @ManyToOne()
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    private VehicleTypeInfo vehicleTypeInfo;

    @Column(name="rate", nullable = false)
    private double rate = Constants.START_RATE;

    @Column(name="current_location_index")
    private int currentLocationIndex = -1;

    @Column(name="crossed_waypoints")
    private int crossedWaypoints = 0;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name="current_stop_location_id")
    private Location currentStop = null;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="active_route_id")
    private Route activeRoute;

    public Vehicle() {
    }

    public Vehicle(
            final Long id,
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleTypeInfo vehicleTypeInfo,
            final double rate
    ) {
        this.id = id;
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
        this.vehicleTypeInfo = vehicleTypeInfo;
        this.rate = rate;
        this.crossedWaypoints = 0;
    }

    public Vehicle(
        final Long id,
        final boolean petFriendly,
        final boolean babySeat,
        final VehicleTypeInfo vehicleTypeInfo,
        final double rate,
        final int currentLocationIndex
    ) {
        this.id = id;
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
        this.vehicleTypeInfo = vehicleTypeInfo;
        this.rate = rate;
        this.currentLocationIndex = currentLocationIndex;
        this.crossedWaypoints = 0;
    }

    public Vehicle(
        final boolean petFriendly,
        final boolean babySeat,
        final VehicleTypeInfo vehicleTypeInfo,
        final double rate,
        final Location location
    ) {
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
        this.vehicleTypeInfo = vehicleTypeInfo;
        this.rate = rate;
        this.currentLocationIndex = 0;
        this.currentStop = location;
        this.crossedWaypoints = 0;
    }

    public int getCrossedWaypoints() {
        return crossedWaypoints;
    }

    public void setCrossedWaypoints(int crossedWaypoints) {
        this.crossedWaypoints = crossedWaypoints;
    }

    public boolean hasRoute(){
        return this.activeRoute != null;
    }

    public Location getLocationForIndexInRoute(double[] lngLat){
        if (this.activeRoute != null) {

            return new Location(lngLat[0], lngLat[1]);
        }
        return null;
    }

    public Route getActiveRoute() {
        return activeRoute;
    }

    public void setActiveRoute(Route activeRoute) {
        this.activeRoute = activeRoute;
    }

    public int getCurrentLocationIndex() {
        return currentLocationIndex;
    }

    public void setCurrentLocationIndex(int currentLocationIndex) {
        this.currentLocationIndex = currentLocationIndex;
    }

    public Long getId() {
        return id;
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

    public Location getCurrentStop() {
        return currentStop;
    }

    public void setCurrentStop(Location currentStop) {
        this.currentStop = currentStop;
    }
}
