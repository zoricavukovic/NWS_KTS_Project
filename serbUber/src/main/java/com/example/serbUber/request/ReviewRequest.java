package com.example.serbUber.request;

import com.example.serbUber.util.Constants;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_MESSAGE_LENGTH;
import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_RATE;

public class ReviewRequest {

    @NotNull(message = "Rate must exist")
    @Range(min = Constants.MIN_RATE, max = Constants.MAX_RATE, message = WRONG_RATE)
    private double vehicleRate;

    @NotNull
    @Range(min = Constants.MIN_RATE, max = Constants.MAX_RATE, message = WRONG_RATE)
    private double driverRate;

    @NotBlank(message = WRONG_MESSAGE_LENGTH)
    @Size(min = Constants.MIN_LENGTH_OF_MESSAGE, max = Constants.MAX_LENGTH_OF_MESSAGE, message = WRONG_MESSAGE_LENGTH)
    private String message;

    @NotBlank(message = "Driving must be selected!")
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
