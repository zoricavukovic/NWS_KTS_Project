package com.example.serbUber.model;

import javax.persistence.*;

import static com.example.serbUber.util.Constants.MAX_NUM_VERIFY_TRIES;

@Entity
@Table(name="verifies")
public class Verify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="security_code", nullable = false)
    private int securityCode;

    @Column(name="used", nullable = false)
    private boolean used = false;

    @Column(name="num_of_tries", nullable = false)
    private int numOfTries = 0;

    public Verify() {}

    public Verify(Long userId, String email, int securityCode, boolean used, int numOfTries) {
        this.userId = userId;
        this.email = email;
        this.securityCode = securityCode;
        this.used = used;
        this.numOfTries = numOfTries;
    }

    public Verify(Long id, Long userId, String email, int securityCode, boolean used, int numOfTries) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.securityCode = securityCode;
        this.used = used;
        this.numOfTries = numOfTries;
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

    public boolean canVerify(int securityCode) {
        return isNotUsed() && hasTries() && checkSecurityCode(securityCode);
    }

    public boolean wrongCodeButHasTries() {return hasTries() && isNotUsed(); }
}
