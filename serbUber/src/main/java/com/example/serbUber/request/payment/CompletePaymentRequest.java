package com.example.serbUber.request.payment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;
import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_NUM_OF_TOKENS;

public class CompletePaymentRequest {

    @NotBlank(message = "Payment id cannot be empty.")
    private final String paymentId;

    @NotBlank(message = "Payer id cannot be empty.")
    private final String payerId;

    @NotNull(message = MISSING_NUM_OF_TOKENS)
    @Positive(message = MISSING_NUM_OF_TOKENS)
    private final double numOfTokens;

    @NotNull(message = MISSING_ID)
    private final Long tokenBankId;

    public CompletePaymentRequest(
            final String paymentId,
            final String payerId,
            final double numOfTokens,
            final Long tokenBankId
    ) {
        this.paymentId = paymentId;
        this.payerId = payerId;
        this.numOfTokens = numOfTokens;
        this.tokenBankId = tokenBankId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPayerId() {
        return payerId;
    }

    public double getNumOfTokens() {
        return numOfTokens;
    }

    public Long getTokenBankId() {
        return tokenBankId;
    }
}
