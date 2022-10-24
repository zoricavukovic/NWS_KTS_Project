package com.example.serbUber.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReviewRequest {

    @NotNull
    @Min(value = 1, message = "Rate must be at least 1 star or greater!")
    private double vehicleRate;

    @NotNull
    @Min(value = 1, message = "Rate must be at least 1 star or greater!")
    private double driverRate;

    @NotNull(message = "Message cannot ve null!")
    @Size(min = 20, max = 100, message = "Message must have between 20-100 characters!")
    private String message;

    @NotNull
    private String driving;

    public ReviewRequest(
            final double vehicleRate,
            final double driverRate,
            final String message,
            final String driving
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

    public String getDriving() {
        return driving;
    }

}
