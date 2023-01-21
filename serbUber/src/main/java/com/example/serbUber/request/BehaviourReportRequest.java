package com.example.serbUber.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;

public class BehaviourReportRequest {

    @NotNull(message = MISSING_ID)
    @Positive(message = MISSING_ID)
    private Long senderId;

    @NotNull(message = MISSING_ID)
    @Positive(message = MISSING_ID)
    private Long receiverId;

    @NotBlank(message = "Report message cannot be empty!")
    private String message;

    public BehaviourReportRequest(
            final Long senderId,
            final Long receiverId,
            final String message
    ) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
