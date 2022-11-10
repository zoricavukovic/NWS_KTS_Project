package com.example.serbUber.exception;

import static com.example.serbUber.exception.ErrorMessagesConstants.UPDATE_ERROR_MESSAGE;

public class EntityUpdateException extends AppException {

    public EntityUpdateException() {
        super(UPDATE_ERROR_MESSAGE);
    }

    public EntityUpdateException(String message) {super(message);}
}