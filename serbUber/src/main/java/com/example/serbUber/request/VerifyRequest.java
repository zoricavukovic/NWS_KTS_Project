package com.example.serbUber.request;

public class VerifyRequest {
    private String verifyId;
    private int securityCode;
    private String userRole;

    public VerifyRequest(String id, int securityCode, String userRole) {
        this.verifyId = id;
        this.securityCode = securityCode;
        this.userRole = userRole;
    }

    public String getVerifyId() {
        return verifyId;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public String getUserRole() {
        return userRole;
    }
}
