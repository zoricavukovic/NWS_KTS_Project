package com.example.serbUber.model;

import com.example.serbUber.model.user.RegularUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="time_stamp", nullable = false)
    private LocalDateTime timeStamp;

    @OneToOne()
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    @ManyToMany(mappedBy = "reservations", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RegularUser> users = new LinkedList<>();

    public Reservation(){
    }
    public Reservation(
        final LocalDateTime timeStamp,
        final Route route,
        final List<RegularUser> users
    ) {
        this.timeStamp = timeStamp;
        this.route = route;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<RegularUser> getUsers() {
        return users;
    }

    public void setUsers(List<RegularUser> users) {
        this.users = users;
    }
}
