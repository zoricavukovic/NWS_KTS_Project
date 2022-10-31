package com.example.serbUber.request;

import com.example.serbUber.util.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NotificationRequest {

    @NotNull(message = "Message must exist")
    @Size(min = Constants.minLengthOfMessage, max = Constants.maxLengthOfMessage, message = "Message must have between 20-100 characters!")
    private String message;

    @NotNull(message = "Message must exist")
    @Email(message = "Email is in wrong format")
    private String sender;

    @NotNull(message = "Message must exist")
    @Email(message = "Email is in wrong format")
    private String receiver;

    @NotNull(message = "Report option must be selected")
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
