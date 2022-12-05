package com.example.serbUber.request;

import com.example.serbUber.util.Constants;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

public class ReviewRequest {

    @NotNull(message = "Rate must exist")
    @Range(min = Constants.MIN_RATE, max = Constants.MAX_RATE, message = WRONG_RATE)
    private double vehicleRate;

    @NotNull
    @Range(min = Constants.MIN_RATE, max = Constants.MAX_RATE, message = WRONG_RATE)
    private double driverRate;

    @Size(max = Constants.MAX_LENGTH_OF_MESSAGE, message = WRONG_MESSAGE_LENGTH)
    private String message;

    @NotNull(message = NOT_NULL_MESSAGE)
    private Long userId;

    @NotNull(message = "Driving must be selected!")
    private Long drivingId;

    public ReviewRequest(
            final double vehicleRate,
            final double driverRate,
            final String message,
            final Long userId,
            final Long drivingId
    ) {
        this.vehicleRate = vehicleRate;
        this.driverRate = driverRate;
        this.message = message;
        this.drivingId = drivingId;
        this.userId = userId;
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

    public Long getDrivingId() {
        return drivingId;
    }

    public Long getUserId() {
        return userId;
    }
}
