package com.example.serbUber.dto.payment;

public class TotalInAppSpendingDTO {

    private final double totalMoneySpent;

    private final double totalTokenAmountSpent;

    private final double totalTokensInApp;

    public TotalInAppSpendingDTO(
            final double totalMoneySpent,
            final double totalTokenAmountSpent,
            final double totalTokensInApp
    ) {
        this.totalMoneySpent = totalMoneySpent;
        this.totalTokenAmountSpent = totalTokenAmountSpent;
        this.totalTokensInApp = totalTokensInApp;
    }

    public double getTotalMoneySpent() {
        return totalMoneySpent;
    }

    public double getTotalTokenAmountSpent() {
        return totalTokenAmountSpent;
    }

    public double getTotalTokensInApp() {
        return totalTokensInApp;
    }
}
