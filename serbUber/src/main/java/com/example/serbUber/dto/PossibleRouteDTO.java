package com.example.serbUber.dto;

import java.util.List;

public class PossibleRouteDTO {

    private double distance; //in metres
    private double timeInMin;
    private List<double[]> locations;

    public PossibleRouteDTO() {
    }

    public PossibleRouteDTO(final double distance, final List<double[]> locations) {
        this.distance = distance;
        this.timeInMin = getTimeFromDistance(distance);
        this.locations = locations;
    }

    private double getTimeFromDistance(final double distance) {
        return distance == 0 ? 0.0 :  Math.ceil((distance/50000L)*60 + 0.5);
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
