package com.example.serbUber.exception;

import static com.example.serbUber.exception.ErrorMessagesConstants.ACTIVITY_STATUS_CANNOT_BE_CHANGED_MESSAGE;

public class ActivityStatusCannotBeChangedException extends AppException {

    public ActivityStatusCannotBeChangedException() {
        super(ACTIVITY_STATUS_CANNOT_BE_CHANGED_MESSAGE);
    }

    public ActivityStatusCannotBeChangedException(String message) {
        super(message);
    }
}
