package com.example.serbUber.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;

public class BehaviourDriverReportRequest {

    @NotNull(message = MISSING_ID)
    @Positive(message = MISSING_ID)
    private Long senderId;

    @NotNull(message = MISSING_ID)
    @Positive(message = MISSING_ID)
    private Long drivingId;

    @NotBlank(message = "Report message cannot be empty!")
    private String message;

    public BehaviourDriverReportRequest(
            final Long senderId,
            final Long drivingId,
            final String message
    ) {
        this.senderId = senderId;
        this.drivingId = drivingId;
        this.message = message;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getDrivingId() {
        return drivingId;
    }

    public void setDrivingId(Long drivingId) {
        this.drivingId = drivingId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
