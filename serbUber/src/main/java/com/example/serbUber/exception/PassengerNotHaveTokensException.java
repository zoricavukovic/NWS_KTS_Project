package com.example.serbUber.exception;

import static com.example.serbUber.util.Constants.UNSUCCESSFUL_PAYMENT_MESSAGE;

public class PassengerNotHaveTokensException extends RuntimeException {
    private final String message;

    public PassengerNotHaveTokensException() {
        super();
        this.message = UNSUCCESSFUL_PAYMENT_MESSAGE;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
