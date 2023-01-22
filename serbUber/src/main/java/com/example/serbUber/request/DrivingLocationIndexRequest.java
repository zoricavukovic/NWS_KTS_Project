package com.example.serbUber.request;

import javax.validation.constraints.NotNull;

public class DrivingLocationIndexRequest {

    @NotNull(message = "Location must be selected")
    private LocationRequest location;

    private int index;

    public DrivingLocationIndexRequest(LocationRequest location, int index) {
        this.location = location;
        this.index = index;
    }

    public DrivingLocationIndexRequest(){}

    public LocationRequest getLocation() {
        return location;
    }

    public void setLocation(LocationRequest location) {
        this.location = location;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
