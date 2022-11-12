package com.example.serbUber.model;

import com.example.serbUber.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="message", nullable = false)
    private String message;

    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timeStamp;

    @Column(name="admin_response", nullable = false)
    private boolean adminResponse = false;

    public Message() {

    }

    public Message(String message, boolean adminResponse) {
        this.message = message;
        this.adminResponse = adminResponse;
        this.timeStamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public boolean isAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(boolean adminResponse) {
        this.adminResponse = adminResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
