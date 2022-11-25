package com.example.serbUber.model;

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

    @OneToOne()
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    private VehicleTypeInfo vehicleTypeInfo;

    @Column(name="rate", nullable = false)
    private double rate = Constants.START_RATE;

    @Column(name="location_index")
    private int currentLocationIndex;

    @OneToOne()
    @JoinColumn(name = "active_route_id", referencedColumnName = "id")
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
    }

    public Vehicle(
        final Long id,
        final boolean petFriendly,
        final boolean babySeat,
        final VehicleTypeInfo vehicleTypeInfo,
        final double rate,
        final int currentLocationIndex,
        final Route activeRoute
    ) {
        this.id = id;
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
        this.vehicleTypeInfo = vehicleTypeInfo;
        this.rate = rate;
        this.currentLocationIndex = currentLocationIndex;
        this.activeRoute = activeRoute;
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
