package com.example.serbUber.request;

public class VerifyRequest {
    private String verifyId;
    private String userId;
    private int securityCode;
    private String userRole;

    public VerifyRequest(String id, String userId, int securityCode, String userRole) {
        this.verifyId = id;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }
}
