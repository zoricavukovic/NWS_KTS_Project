package com.example.serbUber.dto.user;

import com.example.serbUber.model.user.Driver;

import java.util.LinkedList;
import java.util.List;

public class DriverPageDTO extends DriverDTO{
    private int pageSize;
    private int pageNumber;

    public DriverPageDTO(Driver driver, int pageSize, int pageNumber) {
        super(driver);
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public static List<DriverPageDTO> fromDriversPage(final List<Driver> drivers, final int pageSize, final int pageNumber){
        List<DriverPageDTO> driverDTOs = new LinkedList<>();
        drivers.forEach(driver ->
                driverDTOs.add(new DriverPageDTO(driver, pageSize, pageNumber))
        );
        return driverDTOs;
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
