package com.example.serbUber.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    private double vehicleRate;
    private double driverRate;
    private String message;
    private Driving driving;


    public Review(){

    }
    public Review(
        final double vehicleRate,
        final double driverRate,
        final String message,
        final Driving driving
    ) {
        this.vehicleRate = vehicleRate;
        this.driverRate = driverRate;
        this.message = message;
        this.driving = driving;
    }

    public double getVehicleRate() {
        return vehicleRate;
    }

    public double getDriverRate() {
        return driverRate;
    }

    public String getMessage() {
        return message;
    }

    public Driving getDriving() {
        return driving;
    }

    public String getId() {
        return id;
    }
}
