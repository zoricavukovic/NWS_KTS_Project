package com.example.serbUber.request;

import com.example.serbUber.model.Route;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationRequest {

    @NotNull(message = "Reservation time must be selected.")
    @Future(message = "Reservation time must be in future.")
    private LocalDateTime timeStamp;

    @NotNull(message = "Route must be selected.")
    private RouteRequest route;

    @NotEmpty(message = "Users must be added to reservation.")
    private List<String> users;

    public ReservationRequest(
            final LocalDateTime timeStamp,
            final RouteRequest route,
            final List<String> users
    ) {
        this.timeStamp = timeStamp;
        this.route = route;
        this.users = users;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public RouteRequest getRoute() {
        return route;
    }

    public List<String> getUsers() {
        return users;
    }
}
