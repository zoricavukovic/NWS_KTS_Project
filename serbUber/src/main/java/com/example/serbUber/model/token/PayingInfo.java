package com.example.serbUber.model.token;

import javax.persistence.*;

@Entity
@Table(name="paying_info")
public class PayingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="currency", nullable = false)
    private String currency;

    @Column(name="token_price", nullable = false)
    private double tokenPrice;

    @Column(name="max_num_of_tokens_per_transaction", nullable = false)
    private int maxNumOfTokensPerTransaction;

    public PayingInfo() {
    }

    public PayingInfo(String currency, double tokenPrice, int maxNumOfTokensPerTransaction) {
        this.currency = currency;
        this.tokenPrice = tokenPrice;
        this.maxNumOfTokensPerTransaction = maxNumOfTokensPerTransaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getTokenPrice() {
        return tokenPrice;
    }

    public void setTokenPrice(double tokenPrice) {
        this.tokenPrice = tokenPrice;
    }

    public int getMaxNumOfTokensPerTransaction() {
        return maxNumOfTokensPerTransaction;
    }

    public void setMaxNumOfTokensPerTransaction(int maxNumOfTokensPerTransaction) {
        this.maxNumOfTokensPerTransaction = maxNumOfTokensPerTransaction;
    }
}
