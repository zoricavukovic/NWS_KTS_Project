package com.example.serbUber.model;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.example.serbUber.util.Constants.MAX_NUM_VERIFY_TRIES;

@Entity
@Table(name="verifies")
public class Verify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="security_code", nullable = false)
    private int securityCode;

    @Column(name="used", nullable = false)
    private boolean used = false;

    @Column(name="num_of_tries", nullable = false)
    private int numOfTries = 0;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    public Verify() {}

    public Verify(
            final Long userId,
            final String email,
            final int securityCode,
            final boolean used,
            final int numOfTries,
            final LocalDateTime expirationTime
    ) {
        this.userId = userId;
        this.email = email;
        this.securityCode = securityCode;
        this.used = used;
        this.numOfTries = numOfTries;
        this.expirationTime = expirationTime;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getNumOfTries() {
        return numOfTries;
    }

    public void setNumOfTries(int numOfTries) {
        this.numOfTries = numOfTries;
    }

    public boolean isNotUsed() {
        return !this.used;
    }

    public boolean hasTries() {
        return this.numOfTries < MAX_NUM_VERIFY_TRIES;
    }

    public int incrementNumOfTries() {return this.numOfTries += 1;}

    public boolean checkSecurityCode(int securityCode){
        return this.securityCode == securityCode;
    }

    public boolean notExpired() {

        return this.expirationTime.isAfter(LocalDateTime.now());
    }

    public boolean canVerify(int securityCode) {
        return isNotUsed() && hasTries() && checkSecurityCode(securityCode) && notExpired();
    }

    public boolean wrongCodeButHasTries() {return hasTries() && isNotUsed() && notExpired(); }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}
