package com.example.serbUber.exception;

import static com.example.serbUber.exception.ErrorMessagesConstants.DRIVER_ALREADY_HAS_STARTED_DRIVING_EXCEPTION;

public class DriverAlreadyHasStartedDrivingException extends AppException {
    public DriverAlreadyHasStartedDrivingException() {
        super(DRIVER_ALREADY_HAS_STARTED_DRIVING_EXCEPTION);
    }
}
