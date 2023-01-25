package com.example.serbUber.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class VehicleCurrentPositionRequest {

    @Valid
    LongLatRequest longLatRequest;

    @NotNull(message = "Crossed waypoints value cannot be empty.")
    int crossedWaypoint;

    @NotNull(message = "Chosen route index cannot be empty.")
    int chosenRouteIdx;

    public VehicleCurrentPositionRequest() {
    }

    public VehicleCurrentPositionRequest(
            final LongLatRequest longLatRequest,
            final int crossedWaypoint,
            final int chosenRouteIdx
    ) {
        this.longLatRequest = longLatRequest;
        this.crossedWaypoint = crossedWaypoint;
        this.chosenRouteIdx = chosenRouteIdx;
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

    public int getChosenRouteIdx() {
        return chosenRouteIdx;
    }

    public void setChosenRouteIdx(int chosenRouteIdx) {
        this.chosenRouteIdx = chosenRouteIdx;
    }
}
