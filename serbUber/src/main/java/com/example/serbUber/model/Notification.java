package com.example.serbUber.model;

public class Notification {

    private String message;
    private User sender;
    private User receiver;
    private boolean report;

    public Notification(
        final String message,
        final User sender,
        final User receiver,
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

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public boolean isReport() {
        return report;
    }
}
