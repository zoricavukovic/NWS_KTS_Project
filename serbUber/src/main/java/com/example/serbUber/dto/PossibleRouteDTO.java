package com.example.serbUber.dto;

import java.util.List;

public class PossibleRouteDTO {

    private double distance; //in metres
    private double timeInMin;
    private List<double[]> locations;

    public PossibleRouteDTO() {
    }

    public PossibleRouteDTO(final double distance, final List<double[]> locations, final double minutes) {
        this.distance = distance;
        this.timeInMin = minutes;
        this.locations = locations;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTimeInMin() {
        return timeInMin;
    }

    public void setTimeInMin(long timeInMin) {
        this.timeInMin = timeInMin;
    }

    public List<double[]> getPointList() {
        return locations;
    }

    public void setPointList(List<double[]> locations) {
        this.locations = locations;
    }

}
