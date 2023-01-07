package com.example.serbUber.request;

import com.example.serbUber.model.Location;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

public class RouteRequest {

    @NotNull(message = NOT_NULL_MESSAGE)
    @Positive(message = POSITIVE_MESSAGE)
    private double timeInMin;

    @NotEmpty(message = "Destinations must be selected.")
    @Valid
    private List<DrivingLocationIndexRequest> locations = new LinkedList<>();

    @NotNull(message = WRONG_KM_NUM)
    @Positive(message = WRONG_KM_NUM)
    private double distance;

    @NotEmpty(message = "Route path must be selected.")
    private List<Integer> routePathIndex;

    public RouteRequest(

            final double timeInMin,
            final double distance,
            final List<DrivingLocationIndexRequest> locations,
            final List<Integer> routePathIndex
    ) {
        this.timeInMin = timeInMin;
        this.distance = distance;
        this.locations = locations;
        this.routePathIndex = routePathIndex;
    }

    public double getTimeInMin() {
        return timeInMin;
    }

    public void setTimeInMin(double timeInMin) {
        this.timeInMin = timeInMin;
    }

    public List<DrivingLocationIndexRequest> getLocations() {
        return locations;
    }

    public void setLocations(List<DrivingLocationIndexRequest> locations) {
        this.locations = locations;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<Integer> getRoutePathIndex() {
        return routePathIndex;
    }

    public void setRoutePathIndex(List<Integer> routePathIndex) {
        this.routePathIndex = routePathIndex;
    }
}
