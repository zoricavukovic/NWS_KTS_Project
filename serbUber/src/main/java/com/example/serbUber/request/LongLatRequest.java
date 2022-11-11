package com.example.serbUber.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LongLatRequest {
    @NotNull(message = "Longitude cannot be empty.")
    @Positive(message = "Longitude must be greater than 0.")
    private double lon;

    @NotNull(message = "Latitude cannot be empty.")
    @Positive(message = "Latitude must be greater than 0.")
    private double lat;

    public LongLatRequest() {
    }

    public LongLatRequest(final double lon, final double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
