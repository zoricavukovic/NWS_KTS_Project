package com.example.serbUber.server.helper;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.Role;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

public class Constants {

    public static final Long DRIVING_NOT_EXIST_ID = -1L;
    public static final Long DRIVING_EXIST_ID = 1L;
    public static final String DRIVING_REJECTION_REASON = "Driving is rejected..";
    public static final double DURATION = 3;
    public static final LocalDateTime STARTED = LocalDateTime.now();
    public static final LocalDateTime END = LocalDateTime.now().plusMinutes((long) DURATION);
    public static final double PRICE = 2;
    public static final String EXIST_PASSWORD = "sifra123@";

    //VEHICLE
    public static final Long EXIST_VEHICLE_ID = 1L;
    public static final double RATE = 0;


    //DRIVER
    public static final String EXIST_DRIVER_EMAIL = "mile@gmail.com";
    public static final String NAME = "Mile";
    public static final String SURNAME = "Milic";
    public static final String PHONE_NUMBER = "232132131";
    public static final String CITY = "Novi Sad";
    public static final String PROFILE_PICTURE = "default";
    public static final Role ROLE_DRIVER = new Role("ROLE_DRIVER");


    public static final VehicleTypeInfo VEHICLE_TYPE_INFO = new VehicleTypeInfo();
    public static final Vehicle EXIST_VEHICLE = new Vehicle(EXIST_VEHICLE_ID, false, false, VEHICLE_TYPE_INFO, RATE);
    public static final Driver EXIST_DRIVER = new Driver(EXIST_DRIVER_EMAIL, EXIST_PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, PROFILE_PICTURE, EXIST_VEHICLE, ROLE_DRIVER);
    public static final Driving NOT_REJECTED_DRIVING = new Driving(DRIVING_EXIST_ID, DURATION, STARTED, END, new Route(), DrivingStatus.PENDING, EXIST_DRIVER, PRICE);
    public static final DrivingStatusNotification REJECTED_DRIVING_STATUS_NOTIFICATION = new DrivingStatusNotification(DRIVING_REJECTION_REASON, DrivingStatus.REJECTED, NOT_REJECTED_DRIVING);


}
