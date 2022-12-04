package com.example.serbUber.model.token;

import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="token_banks")
public class TokenBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private RegularUser user;

    @Column(name="num_of_tokens", nullable = false)
    private int numOfTokens;

    @Column(name="total_token_amount_spent", nullable = false)
    private double totalTokenAmountSpent;

    @Column(name="total_money_amount_spent", nullable = false)
    private double totalMoneyAmountSpent;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "token_bank_id", referencedColumnName = "id")
    private List<TokenTransaction> transactions = new LinkedList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paying_info_id", referencedColumnName = "id")
    private PayingInfo payingInfo;

    public TokenBank() {
    }

    public TokenBank(
            final RegularUser user,
            final int numOfTokens,
            final double totalTokenAmountSpent,
            final double totalMoneyAmountSpent,
            final List<TokenTransaction> transactions,
            final PayingInfo payingInfo
    ) {
        this.user = user;
        this.numOfTokens = numOfTokens;
        this.totalTokenAmountSpent = totalTokenAmountSpent;
        this.totalMoneyAmountSpent = totalMoneyAmountSpent;
        this.transactions = transactions;
        this.payingInfo = payingInfo;
    }

    public TokenBank(
            final RegularUser user,
            final int numOfTokens,
            final double totalTokenAmountSpent,
            final double totalMoneyAmountSpent,
            final PayingInfo payingInfo
    ) {
        this.user = user;
        this.numOfTokens = numOfTokens;
        this.totalTokenAmountSpent = totalTokenAmountSpent;
        this.totalMoneyAmountSpent = totalMoneyAmountSpent;
        this.transactions = new LinkedList<>();
        this.payingInfo = payingInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegularUser getUser() {
        return user;
    }

    public void setUser(RegularUser user) {
        this.user = user;
    }

    public int getNumOfTokens() {
        return numOfTokens;
    }

    public void setNumOfTokens(int numOfTokens) {
        this.numOfTokens = numOfTokens;
    }

    public double getTotalTokenAmountSpent() {
        return totalTokenAmountSpent;
    }

    public void setTotalTokenAmountSpent(double totalTokenAmountSpent) {
        this.totalTokenAmountSpent = totalTokenAmountSpent;
    }

    public double getTotalMoneyAmountSpent() {
        return totalMoneyAmountSpent;
    }

    public void setTotalMoneyAmountSpent(double totalMoneyAmountSpent) {
        this.totalMoneyAmountSpent = totalMoneyAmountSpent;
    }

    public List<TokenTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TokenTransaction> transactions) {
        this.transactions = transactions;
    }

    public PayingInfo getPayingInfo() {
        return payingInfo;
    }

    public void setPayingInfo(PayingInfo payingInfo) {
        this.payingInfo = payingInfo;
    }
}
