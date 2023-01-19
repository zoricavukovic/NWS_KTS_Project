package com.example.serbUber.request.payment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

public class UpdatePayingInfoRequest {

    @NotNull(message = MISSING_NUM_OF_TOKENS)
    @Positive(message = MISSING_NUM_OF_TOKENS)
    private final double tokenPrice;

    @NotNull(message = MAX_NUM_OF_TOKENS)
    @Positive(message = MAX_NUM_OF_TOKENS)
    private final int maxNumOfTokensPerTransaction;

    public UpdatePayingInfoRequest(
            final double tokenPrice,
            final int maxNumOfTokensPerTransaction
    ) {
        this.tokenPrice = tokenPrice;
        this.maxNumOfTokensPerTransaction = maxNumOfTokensPerTransaction;
    }

    public double getTokenPrice() {
        return tokenPrice;
    }

    public int getMaxNumOfTokensPerTransaction() {
        return maxNumOfTokensPerTransaction;
    }
}
