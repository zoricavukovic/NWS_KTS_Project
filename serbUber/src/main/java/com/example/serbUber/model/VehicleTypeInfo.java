package com.example.serbUber.model;

import javax.persistence.*;

@Entity
@Table(name="vehicle_type_infos")
public class VehicleTypeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name="start_price", nullable = false)
    private double startPrice;

    @Column(name="num_of_seats", nullable = false)
    private int numOfSeats;

    public VehicleTypeInfo() {
    }

    public VehicleTypeInfo(
        final VehicleType vehicleType,
        final double startPrice,
        final int numOfSeats
    ) {
        this.vehicleType = vehicleType;
        this.startPrice = startPrice;
        this.numOfSeats = numOfSeats;
    }

    public Long getId() {
        return id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

    public void setNumOfSeats(int numOfSeats) {
        this.numOfSeats = numOfSeats;
    }
}
