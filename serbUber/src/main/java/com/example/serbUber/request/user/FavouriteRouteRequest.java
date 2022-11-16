package com.example.serbUber.request.user;

import com.example.serbUber.model.Route;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

public class FavouriteRouteRequest {

    @NotNull(message=NOT_NULL_MESSAGE)
    private Long userId;

    @NotNull(message=NOT_NULL_MESSAGE)
    private Long routeId;

    public FavouriteRouteRequest(final Long userId, final Long routeId) {
        this.userId = userId;
        this.routeId = routeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
}
