package com.example.serbUber.request;

import com.example.serbUber.model.Route;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationRequest {
    @NotNull
    @Future
    private LocalDateTime timeStamp;
    @NotNull
    private Route route;
    @NotEmpty
    private List<String> users;

    public ReservationRequest(
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
