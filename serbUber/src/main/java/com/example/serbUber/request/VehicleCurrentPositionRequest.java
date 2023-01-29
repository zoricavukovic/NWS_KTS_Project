package com.example.serbUber.request;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class VehicleCurrentPositionRequest {

    @Valid
    LongLatRequest longLatRequest;

    @Min(message = "Crossed waypoints value cannot be lower than -1.", value = -1)
    int crossedWaypoints;

    @Min(message = "Chosen route index cannot be lower than -1.", value = -1)
    int chosenRouteIdx;

    public VehicleCurrentPositionRequest() {
    }

    public VehicleCurrentPositionRequest(
            final LongLatRequest longLatRequest,
            final int crossedWaypoints,
            final int chosenRouteIdx
    ) {
        this.longLatRequest = longLatRequest;
        this.crossedWaypoints = crossedWaypoints;
        this.chosenRouteIdx = chosenRouteIdx;
    }

    public LongLatRequest getLongLatRequest() {
        return longLatRequest;
    }

    public void setLongLatRequest(LongLatRequest longLatRequest) {
        this.longLatRequest = longLatRequest;
    }

    public int getCrossedWaypoints() {
        return crossedWaypoints;
    }

    public void setCrossedWaypoints(int crossedWaypoints) {
        this.crossedWaypoints = crossedWaypoints;
    }

    public int getChosenRouteIdx() {
        return chosenRouteIdx;
    }

    public void setChosenRouteIdx(int chosenRouteIdx) {
        this.chosenRouteIdx = chosenRouteIdx;
    }
}
