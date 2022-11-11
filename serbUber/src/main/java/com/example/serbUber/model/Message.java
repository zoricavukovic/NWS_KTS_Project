package com.example.serbUber.model;

import com.example.serbUber.model.user.User;

import javax.persistence.*;


@Entity
@Table(name="messages")
public class Message extends Notification {

    @Column(name="admin_response", nullable = false)
    private boolean adminResponse = false;

    public Message() {
        super();
    }

    public Message(String message, User sender, User receiver, boolean adminResponse) {
        super(message, sender, receiver);
        this.adminResponse = adminResponse;
    }

    public boolean isAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(boolean adminResponse) {
        this.adminResponse = adminResponse;
    }
}
