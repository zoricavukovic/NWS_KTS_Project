package com.example.serbUber.dto.payment;

import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.model.token.TokenBank;

import java.util.List;

import static com.example.serbUber.dto.payment.TokenTransactionDTO.fromTransactions;

public class TokenBankDTO {

    private final Long id;
    private final RegularUserDTO user;
    private final double numOfTokens;
    private final double totalTokenAmountSpent;
    private final double totalMoneyAmountSpent;
    private final List<TokenTransactionDTO> transactions;
    private final PayingInfoDTO payingInfo;

    public TokenBankDTO(final TokenBank tokenBank) {
        this.id = tokenBank.getId();
        this.user = new RegularUserDTO(tokenBank.getUser());
        this.numOfTokens = tokenBank.getNumOfTokens();
        this.totalTokenAmountSpent = tokenBank.getTotalTokenAmountSpent();
        this.totalMoneyAmountSpent = tokenBank.getTotalMoneyAmountSpent();
        this.transactions = fromTransactions(tokenBank.getTransactions());
        this.payingInfo = new PayingInfoDTO(tokenBank.getPayingInfo());
    }

    public Long getId() {
        return id;
    }

    public RegularUserDTO getUser() {
        return user;
    }

    public double getNumOfTokens() {
        return numOfTokens;
    }

    public double getTotalMoneyAmountSpent() {
        return totalMoneyAmountSpent;
    }

    public List<TokenTransactionDTO> getTransactions() {
        return transactions;
    }

    public PayingInfoDTO getPayingInfo() {
        return payingInfo;
    }

    public double getTotalTokenAmountSpent() {
        return totalTokenAmountSpent;
    }
}
