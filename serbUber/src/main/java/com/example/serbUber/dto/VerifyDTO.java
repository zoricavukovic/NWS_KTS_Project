package com.example.serbUber.dto;

import com.example.serbUber.model.Verify;

public class VerifyDTO {

    private Long id;
    private Long userId;
    private String email;
    private int securityCode;

    public VerifyDTO(Long id, Long userId, String email, int securityCode) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.securityCode = securityCode;
    }

    public VerifyDTO(Verify verify) {
        this.id = verify.getId();
        this.userId = verify.getUserId();
        this.email = verify.getEmail();
        this.securityCode = verify.getSecurityCode();
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public Long getId() {
        return id;
    }
}
