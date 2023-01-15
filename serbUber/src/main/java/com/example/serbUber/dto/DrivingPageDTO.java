package com.example.serbUber.dto;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;

import java.time.LocalDateTime;
import java.util.*;

public class DrivingPageDTO{

    private Long id;
    private int duration;
    private LocalDateTime started;
    private Route route;
    private DrivingStatus drivingStatus;
    private int pageSize;
    private int pageNumber;

    public DrivingPageDTO(Driving driving, int pageSize, int pageNumber) {
        this.id = driving.getId();
        this.duration = driving.getDuration();
        this.started = driving.getStarted();
        this.route = driving.getRoute();
        this.drivingStatus = driving.getDrivingStatus();
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public static List<DrivingPageDTO> fromDrivingsPage(final List<Driving> drivings, final int pageSize, final int pageNumber){
        List<DrivingPageDTO> drivingDTOs = new LinkedList<>();
        drivings.forEach(driving ->
                drivingDTOs.add(new DrivingPageDTO(driving, pageSize, pageNumber))
        );
        return drivingDTOs;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public DrivingStatus getDrivingStatus() {
        return drivingStatus;
    }

    public void setDrivingStatus(DrivingStatus drivingStatus) {
        this.drivingStatus = drivingStatus;
    }
}
