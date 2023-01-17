package com.example.serbUber.exception;

import static com.example.serbUber.util.Constants.UNSUCCESSFUL_PAYMENT_MESSAGE;

public class PassengerNotHaveTokensException extends AppException {

    public PassengerNotHaveTokensException() {
        super(UNSUCCESSFUL_PAYMENT_MESSAGE);
    }
}
