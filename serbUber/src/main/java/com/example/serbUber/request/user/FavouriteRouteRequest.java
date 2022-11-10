package com.example.serbUber.request.user;

import com.example.serbUber.model.Route;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static com.example.serbUber.exception.ErrorMessagesConstants.EMPTY_EMAIL;
import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

public class FavouriteRouteRequest {

    @Email(message=WRONG_EMAIL)
    @NotBlank(message = EMPTY_EMAIL)
    private String userEmail;

    private Long routeId;

    public FavouriteRouteRequest(final String userEmail, final Long routeId) {
        this.userEmail = userEmail;
        this.routeId = routeId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
}
