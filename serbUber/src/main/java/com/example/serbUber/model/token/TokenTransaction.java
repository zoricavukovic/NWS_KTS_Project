package com.example.serbUber.model.token;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="token_transactions")
public class TokenTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="time_stamp", nullable = false)
    private LocalDateTime timeStamp;

    @Column(name="num_of_bought_tokens", nullable = false)
    private int numOfBoughtTokens;

    @Column(name="total_price", nullable = false)
    private double totalPrice;

    public TokenTransaction() {
    }

    public TokenTransaction(
            final LocalDateTime timeStamp,
            final int numOfBoughtTokens,
            final double totalPrice
    ) {
        this.timeStamp = timeStamp;
        this.numOfBoughtTokens = numOfBoughtTokens;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getNumOfBoughtTokens() {
        return numOfBoughtTokens;
    }

    public void setNumOfBoughtTokens(int numOfBoughtTokens) {
        this.numOfBoughtTokens = numOfBoughtTokens;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
