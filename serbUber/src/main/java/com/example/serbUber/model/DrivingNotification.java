package com.example.serbUber.model;

import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="driving_notifications")
public class DrivingNotification{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="lon_started")
    private double lonStarted;
    @Column(name="lat_started")
    private double latStarted;
    @Column(name="lon_end")
    private double lonEnd;
    @Column(name="lat_end")
    private double latEnd;

    @Column(name="price")
    private double price;

    @OneToOne()
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private RegularUser sender;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_driving_notifications", joinColumns = @JoinColumn(name = "notification_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<RegularUser> users;

    @Column(name="started")
    private LocalDateTime started;

    @Column(name="duration")
    private int duration;

    @Column(name="answered_passengers")
    private int answeredPassengers;

    public DrivingNotification(final double lonStarted,
                               final double latStarted,
                               final double lonEnd,
                               final double latEnd,
                               final double price,
                               final RegularUser sender,
                               final Set<RegularUser> users,
                               final LocalDateTime started,
                               final int duration,
                               final int answeredPassengers) {
        this.lonStarted = lonStarted;
        this.latStarted = latStarted;
        this.lonEnd = lonEnd;
        this.latEnd = latEnd;
        this.price = price;
        this.sender = sender;
        this.users = users;
        this.started = started;
        this.duration = duration;
        this.answeredPassengers = answeredPassengers;
    }

    public DrivingNotification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLonStarted() {
        return lonStarted;
    }

    public void setLonStarted(double lonStarted) {
        this.lonStarted = lonStarted;
    }

    public double getLatStarted() {
        return latStarted;
    }

    public void setLatStarted(double latStarted) {
        this.latStarted = latStarted;
    }

    public double getLonEnd() {
        return lonEnd;
    }

    public void setLonEnd(double lonEnd) {
        this.lonEnd = lonEnd;
    }

    public double getLatEnd() {
        return latEnd;
    }

    public void setLatEnd(double latEnd) {
        this.latEnd = latEnd;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public RegularUser getSender() {
        return sender;
    }

    public void setSender(RegularUser sender) {
        this.sender = sender;
    }

    public Set<RegularUser> getUsers() {
        return users;
    }

    public void setUsers(Set<RegularUser> users) {
        this.users = users;
    }

    public int getAnsweredPassengers() {
        return answeredPassengers;
    }

    public void setAnsweredPassengers(int answeredPassengers) {
        this.answeredPassengers = answeredPassengers;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
