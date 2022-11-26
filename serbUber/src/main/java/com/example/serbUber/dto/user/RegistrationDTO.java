package com.example.serbUber.dto.user;

public class RegistrationDTO {

    private final Long verifyId;
    private final String email;

    public RegistrationDTO(Long verifyId, String email) {
        this.verifyId = verifyId;
        this.email = email;
    }

    public Long getVerifyId() {
        return verifyId;
    }

    public String getEmail() {
        return email;
    }
}
