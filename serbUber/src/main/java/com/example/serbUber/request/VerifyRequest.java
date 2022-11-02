package com.example.serbUber.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import static com.example.serbUber.util.Constants.POSITIVE_INT_NUM_REG;

public class VerifyRequest {

    @NotNull(message = "Verify id must be selected.")
    @Pattern(regexp = POSITIVE_INT_NUM_REG, message = "Id must be positive integer.")
    private Long verifyId;

    @Pattern(regexp = POSITIVE_INT_NUM_REG, message = "Security code is number greater than 0.")
    private int securityCode;

    @NotBlank(message = "User role must be selected.")
    private String userRole;

    public VerifyRequest(Long id, int securityCode, String userRole) {
        this.verifyId = id;
        this.securityCode = securityCode;
        this.userRole = userRole;
    }

    public Long getVerifyId() {
        return verifyId;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public String getUserRole() {
        return userRole;
    }
}
