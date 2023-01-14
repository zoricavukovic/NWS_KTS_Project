package com.example.serbUber.exception;

public class ExcessiveNumOfPassengersException extends AppException {

    public ExcessiveNumOfPassengersException(String vehicleType) {
        super(String.format("Excessive number of passengers for %s", vehicleType));
    }
}
