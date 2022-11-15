package com.example.serbUber.model;

import com.example.serbUber.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Entity
@Inheritance(strategy=TABLE_PER_CLASS)
public abstract class Notification {
    @Id
    @SequenceGenerator(name = "generator", sequenceName = "notificationsIdGen", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    private Long id;

    @Column(name="message", nullable = false)
    private String message;

    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timeStamp;

    @OneToOne()
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @OneToOne()
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    public Notification(){

    }
    public Notification(
        final String message,
        final User sender,
        final User receiver
    ) {
        this.message = message;
        this.timeStamp = LocalDateTime.now();
        this.sender = sender;
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
