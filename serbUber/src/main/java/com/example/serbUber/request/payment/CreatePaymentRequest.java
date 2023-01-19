package com.example.serbUber.request.payment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;
import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_NUM_OF_TOKENS;

public class CreatePaymentRequest {

    @NotNull(message = MISSING_ID)
    private final Long tokenBankId;

    @NotNull(message = MISSING_NUM_OF_TOKENS)
    @Positive(message = MISSING_NUM_OF_TOKENS)
    private final double numOfTokens;

    public CreatePaymentRequest(
            final Long tokenBankId,
            final double numOfTokens
    ) {
        this.tokenBankId = tokenBankId;
        this.numOfTokens = numOfTokens;
    }

    public Long getTokenBankId() {
        return tokenBankId;
    }

    public double getNumOfTokens() {
        return numOfTokens;
    }
}
