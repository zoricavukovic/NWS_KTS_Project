package com.example.serbUber.dto;

import com.example.serbUber.model.Driving;

import java.util.LinkedList;
import java.util.List;

public class DrivingPageDTO extends DrivingDTO{

    private int pageSize;
    private int pageNumber;

    public DrivingPageDTO(Driving driving, int pageSize, int pageNumber) {
        super(driving);
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
}
