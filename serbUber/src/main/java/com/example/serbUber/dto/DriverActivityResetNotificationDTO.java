package com.example.serbUber.dto;

import com.example.serbUber.model.user.Driver;

public class DriverActivityResetNotificationDTO {

    private final Long id;

    private final String email;

    private final boolean active;

    public DriverActivityResetNotificationDTO(
            final Long id,
            final String email,
            final boolean active
    ) {
        this.id = id;
        this.email = email;
        this.active = active;
    }

    public DriverActivityResetNotificationDTO(final Driver driver) {
        this.id = driver.getId();
        this.email = driver.getEmail();
        this.active = driver.isActive();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return active;
    }
}
