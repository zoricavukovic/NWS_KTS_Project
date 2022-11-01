package com.example.serbUber.request;

import com.example.serbUber.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

public class NotificationRequest {

    @NotBlank(message = WRONG_MESSAGE_LENGTH)
    @Size(min = Constants.MIN_LENGTH_OF_MESSAGE, max = Constants.MAX_LENGTH_OF_MESSAGE, message = WRONG_MESSAGE_LENGTH)
    private String message;

    @Email(message = WRONG_EMAIL)
    @NotBlank(message = EMPTY_EMAIL)
    @Size(max = 60, message = TOO_LONG_EMAIL)
    private String sender;

    @Email(message = WRONG_EMAIL)
    @NotBlank(message = EMPTY_EMAIL)
    @Size(max = 60, message = TOO_LONG_EMAIL)
    private String receiver;

    @NotNull(message = "Report option must be selected!")
    private boolean report;

    public NotificationRequest(
            final String message,
            final String sender,
            final String receiver,
            final boolean report
    ) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.report = report;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }
}
