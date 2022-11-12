package com.example.serbUber.exception;

import static com.example.serbUber.exception.ErrorMessagesConstants.NO_AVAILABLE_ADMIN_EXC;

public class NoAvailableAdminException extends AppException {

    public NoAvailableAdminException(String message) {
        super(message);
    }

    public NoAvailableAdminException() {
        super(NO_AVAILABLE_ADMIN_EXC);
    }

}
