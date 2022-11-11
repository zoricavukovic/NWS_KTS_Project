package com.example.serbUber.dto;

import java.util.List;

public class PossibleRouteDTO {

    private double distance;
    private double timeInMin;
    private List<double[]> pointList;

    public PossibleRouteDTO() {
    }

    public PossibleRouteDTO(final double distance, final double timeInMs, final List<double[]> pointList) {
        this.distance = distance;
        this.timeInMin = getTimeInMinFromMs(timeInMs);
        this.pointList = pointList;
    }

    private double getTimeInMinFromMs(final double timeInMs) {
        return timeInMs == 0 ? 0.0 :  Math.ceil((timeInMs/1000)/60L + 0.5);
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
        return pointList;
    }

    public void setPointList(List<double[]> pointList) {
        this.pointList = pointList;
    }

}