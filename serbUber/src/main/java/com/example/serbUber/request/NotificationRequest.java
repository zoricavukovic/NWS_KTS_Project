package com.example.serbUber.request;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

public abstract class NotificationRequest {

    private String message;
    private LocalDateTime timestamp;
    @Valid
    @Email(message = WRONG_EMAIL)
    private String senderEmail;
    @Valid
    @Email(message = WRONG_EMAIL)
    private String receiverEmail;

    public NotificationRequest(String message, LocalDateTime timestamp, String senderEmail, String receiverEmail) {
        this.message = message;
        this.timestamp = timestamp;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
    }

    public NotificationRequest() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }
}
