package com.example.serbUber.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_SECURITY_CODE;

public class VerifyRequest {

    @NotNull(message = WRONG_SECURITY_CODE)
    @Positive(message = WRONG_SECURITY_CODE)
    private Long verifyId;

    @NotNull(message = WRONG_SECURITY_CODE)
    @Positive(message = WRONG_SECURITY_CODE)
    private int securityCode;

    public VerifyRequest(Long verifyId, int securityCode) {
        this.verifyId = verifyId;
        this.securityCode = securityCode;
    }

    public Long getVerifyId() {
        return verifyId;
    }

    public int getSecurityCode() {
        return securityCode;
    }

}
