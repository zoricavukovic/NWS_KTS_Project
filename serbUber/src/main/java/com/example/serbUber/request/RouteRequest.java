package com.example.serbUber.request;

import com.example.serbUber.model.Location;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_KM_NUM;

public class RouteRequest {
    @NotNull(message = "Location must be selected.")
    @Valid
    private LocationRequest startPoint;

    @NotEmpty(message = "Destinations must be selected.")
    @Valid
    private List<LocationRequest> destinations = new LinkedList<>();

    @NotNull(message = WRONG_KM_NUM)
    @Positive(message = WRONG_KM_NUM)
    private double kilometers;

    public RouteRequest(
            final LocationRequest startPoint,
            final List<LocationRequest> destinations,
            final double kilometers
    ) {
        this.startPoint = startPoint;
        this.destinations = destinations;
        this.kilometers = kilometers;
    }

    public LocationRequest getStartPoint() {
        return startPoint;
    }

    public List<LocationRequest> getDestinations() {
        return destinations;
    }

    public double getKilometers() {
        return kilometers;
    }
}
