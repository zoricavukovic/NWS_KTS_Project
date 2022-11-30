package com.example.serbUber.request.user;

import javax.validation.constraints.NotNull;

public class DriverActivityStatusRequest {

    @NotNull(message = "Id cannot be empty value.")
    private final Long id;

    @NotNull(message = "Activity cannot be empty value.")
    private final boolean active;

    public DriverActivityStatusRequest(Long id, boolean active) {
        this.id = id;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }
}
