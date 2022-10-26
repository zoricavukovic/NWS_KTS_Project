package com.example.serbUber.model;

import com.example.serbUber.model.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

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

    public String getId() {
        return id;
    }
}
