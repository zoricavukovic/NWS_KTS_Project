package com.example.serbUber.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class VehicleCurrentPositionRequest {

    @Valid
    LongLatRequest longLatRequest;

    @NotNull(message = "Crossed waypoints value cannot be empty.")
    int crossedWaypoint;

    public VehicleCurrentPositionRequest() {
    }

    public VehicleCurrentPositionRequest(LongLatRequest longLatRequest, int crossedWaypoint) {
        this.longLatRequest = longLatRequest;
        this.crossedWaypoint = crossedWaypoint;
    }

    public LongLatRequest getLongLatRequest() {
        return longLatRequest;
    }

    public void setLongLatRequest(LongLatRequest longLatRequest) {
        this.longLatRequest = longLatRequest;
    }

    public int getCrossedWaypoint() {
        return crossedWaypoint;
    }

    public void setCrossedWaypoint(int crossedWaypoint) {
        this.crossedWaypoint = crossedWaypoint;
    }
}
