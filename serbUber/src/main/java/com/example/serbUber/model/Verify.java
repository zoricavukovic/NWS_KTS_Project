package com.example.serbUber.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.example.serbUber.util.Constants.MAX_NUM_VERIFY_TRIES;

@Document(collection = "verifies")
public class Verify {
    @Id
    private String id;

    private String userId;
    private String email;
    private int securityCode;
    private boolean used = false;
    private int numOfTries = 0;

    public Verify() {

    }

    public Verify(String userId, String email, int securityCode, boolean used, int numOfTries) {
        this.userId = userId;
        this.email = email;
        this.securityCode = securityCode;
        this.used = used;
        this.numOfTries = numOfTries;
    }

    public Verify(String id, String userId, String email, int securityCode, boolean used, int numOfTries) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.securityCode = securityCode;
        this.used = used;
        this.numOfTries = numOfTries;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public boolean isUsed() {
        return used;
    }

    public int getNumOfTries() {
        return numOfTries;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    public void setUsed(boolean used) {
        this.used = used;
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
