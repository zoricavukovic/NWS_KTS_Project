package com.example.serbUber.request;

import com.example.serbUber.util.Constants;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReviewRequest {

    @NotNull(message = "Rate must exist")
    @Range(min = Constants.MIN_RATE, max = Constants.MAX_RATE, message = "Rate must be value between 1 and 5!")
    private double vehicleRate;

    @NotNull
    @Range(min = Constants.MIN_RATE, max = Constants.MAX_RATE, message = "Rate must be value between 1 and 5!")
    private double driverRate;

    @NotNull(message = "Message must exist!")
    @Size(min = 20, max = 100, message = "Message must have between 20-100 characters!")
    private String message;

    @NotNull(message = "Driving must be selected!")
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
