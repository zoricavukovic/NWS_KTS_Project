package com.example.serbUber.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    private LocalDateTime timeStamp;
    private Route route;
    private List<String> users;

    public Reservation(
        final LocalDateTime timeStamp,
        final Route route,
        final List<String> users
    ) {
        this.timeStamp = timeStamp;
        this.route = route;
        this.users = users;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Route getRoute() {
        return route;
    }

    public List<String> getUsers() {
        return users;
    }
}
