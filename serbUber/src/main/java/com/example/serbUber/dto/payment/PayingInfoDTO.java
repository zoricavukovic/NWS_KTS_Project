package com.example.serbUber.dto.payment;

import com.example.serbUber.model.token.PayingInfo;

public class PayingInfoDTO {

    private final Long id;
    private final String currency;
    private final double tokenPrice;
    private final double maxNumOfTokensPerTransaction;

    public PayingInfoDTO(final PayingInfo payingInfo) {
        this.id = payingInfo.getId();
        this.currency = payingInfo.getCurrency();
        this.tokenPrice = payingInfo.getTokenPrice();
        this.maxNumOfTokensPerTransaction = payingInfo.getMaxNumOfTokensPerTransaction();
    }

    public Long getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public double getTokenPrice() {
        return tokenPrice;
    }

    public double getMaxNumOfTokensPerTransaction() {
        return maxNumOfTokensPerTransaction;
    }
}
