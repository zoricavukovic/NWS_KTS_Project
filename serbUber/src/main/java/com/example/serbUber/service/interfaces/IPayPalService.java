package com.example.serbUber.service.interfaces;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.PayPalPaymentException;
import com.paypal.api.payments.Payment;

import java.util.Map;

public interface IPayPalService {

    Map<String, String> createPayment(
            Long tokeBankId,
            int numOfTokens
    ) throws EntityNotFoundException, PayPalPaymentException;

    boolean completePayment(
            final String paymentId,
            final String payerId,
            final int numOfTokens,
            final Long tokenBankId
    ) throws PayPalPaymentException, EntityNotFoundException;
}
