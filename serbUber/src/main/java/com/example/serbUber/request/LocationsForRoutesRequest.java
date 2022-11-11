package com.example.serbUber.request;

import com.example.serbUber.util.MaxSizeConstraint;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class LocationsForRoutesRequest {

    @NotEmpty(message = "Input locations list cannot be empty.")
    @MaxSizeConstraint
    private List<@Valid LongLatRequest> locationsForRouteRequest;

    public LocationsForRoutesRequest() {
    }

    public LocationsForRoutesRequest(final List<@Valid LongLatRequest> locationsForRouteRequest) {
        this.locationsForRouteRequest = locationsForRouteRequest;
    }

    public List<LongLatRequest> getLocationsForRouteRequest() {
        return locationsForRouteRequest;
    }

    public void setLocationsForRouteRequest(List<LongLatRequest> locationsForRouteRequest) {
        this.locationsForRouteRequest = locationsForRouteRequest;
    }
}
