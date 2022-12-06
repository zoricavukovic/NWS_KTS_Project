package com.example.serbUber.dto.payment;

import com.example.serbUber.model.token.TokenTransaction;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TokenTransactionDTO {

    private final Long id;
    private final LocalDateTime timeStamp;
    private final double numOfBoughtTokens;
    private final double totalPrice;

    public TokenTransactionDTO(final TokenTransaction tokenTransaction) {
        this.id = tokenTransaction.getId();
        this.timeStamp = tokenTransaction.getTimeStamp();
        this.numOfBoughtTokens = tokenTransaction.getNumOfBoughtTokens();
        this.totalPrice = tokenTransaction.getTotalPrice();
    }

    public static List<TokenTransactionDTO> fromTransactions(final List<TokenTransaction> transactions){
        List<TokenTransactionDTO> transactionDTOS = new LinkedList<>();
        transactions.forEach(transaction ->
                transactionDTOS.add(new TokenTransactionDTO(transaction))
        );

        return transactionDTOS;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public double getNumOfBoughtTokens() {
        return numOfBoughtTokens;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
