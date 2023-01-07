package com.example.serbUber.exception;

import static com.example.serbUber.exception.ErrorMessagesConstants.DRIVING_SHOULD_NOT_START_YET;

public class DrivingShouldNotStartYetException extends AppException {
    public DrivingShouldNotStartYetException() {
        super(DRIVING_SHOULD_NOT_START_YET);
    }
}
