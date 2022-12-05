package com.example.serbUber.exception;

import static com.example.serbUber.exception.ErrorMessagesConstants.PAYPAL_PAYMENT_EXCEPTION;

public class PayPalPaymentException extends AppException {

    public PayPalPaymentException() {
        super(PAYPAL_PAYMENT_EXCEPTION);
    }

    public PayPalPaymentException(String message) {
        super(message);
    }

}
