package com.example.serbUber.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;

public class BlockingRequest {

    @NotNull(message = MISSING_ID)
    private Long userId;

    @NotBlank(message = "Reason must be entered")
    private String reason;

    public BlockingRequest() {
    }

    public BlockingRequest(Long userId, String reason) {
        this.userId = userId;
        this.reason = reason;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
