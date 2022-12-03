package com.example.serbUber.model;

import com.example.serbUber.model.user.User;

import javax.persistence.*;

@Entity
@Table(name="reports")
public class Report extends Notification {

    @Column(name="admin_email")
    private String adminEmail;

    @Column(name="answered", nullable = false)
    private boolean answered = false;

    public Report() {
        super();
    }

    public Report(String message, User sender, User receiver, String adminEmail, boolean answered) {
        super(message, sender, receiver);
        this.adminEmail = adminEmail;
        this.answered = answered;
    }

    public Report(String message, User sender, User receiver, String adminEmail) {
        super(message, sender, receiver);
        this.adminEmail = adminEmail;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}
