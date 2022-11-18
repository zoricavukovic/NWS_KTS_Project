package com.example.serbUber.model;

import com.example.serbUber.model.user.User;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private User client;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private User admin;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private List<Message> messages = new LinkedList<>();

    @Column(name="resolved", nullable = false)
    private boolean resolved = false;

    public ChatRoom() {

    }

    public ChatRoom(
            final User client,
            final User admin,
            final boolean resolved
    ) {
        this.client = client;
        this.admin = admin;
        this.resolved = resolved;
    }

    public ChatRoom(
            final User client,
            final User admin,
            final List<Message> messages,
            final boolean resolved
    ) {
        this.client = client;
        this.admin = admin;
        this.messages = messages;
        this.resolved = resolved;
    }

    public Long getId() {
        return id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
