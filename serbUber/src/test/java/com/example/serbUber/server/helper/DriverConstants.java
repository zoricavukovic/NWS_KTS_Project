package com.example.serbUber.server.helper;

import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.Role;

import static com.example.serbUber.server.helper.Constants.*;
import static com.example.serbUber.server.helper.LocationHelper.CITY;
import static com.example.serbUber.server.helper.VehicleTypeInfoConstants.VEHICLE_TYPE_INFO_SUV;

public class DriverConstants {

    public static final Long EXIST_VEHICLE_ID = 1L;
    public static final double RATE = 0;
    //DRIVER
    public static final String EXIST_DRIVER_EMAIL = "mile@gmail.com";
    public static final String NAME = "Mile";
    public static final String SURNAME = "Milic";
    public static final String PHONE_NUMBER = "232132131";
    public static final String PROFILE_PICTURE = "default";
    public static final Role ROLE_DRIVER = new Role("ROLE_DRIVER");
    public static final Long DRIVER_ID = 1L;

    public static final VehicleTypeInfo VEHICLE_TYPE_INFO_DEFINED = new VehicleTypeInfo(VehicleType.CAR, 2, 6);
    public static Vehicle EXIST_VEHICLE = new Vehicle(EXIST_VEHICLE_ID, false, false, VEHICLE_TYPE_INFO_DEFINED, RATE);
    public static Driver EXIST_DRIVER = new Driver(DRIVER_ID, EXIST_DRIVER_EMAIL, EXIST_PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, PROFILE_PICTURE, EXIST_VEHICLE, ROLE_DRIVER);
    public static Driving NOT_REJECTED_DRIVING = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, END, new Route(), DrivingStatus.PENDING, EXIST_DRIVER, PRICE);
    public static DrivingStatusNotification REJECTED_DRIVING_STATUS_NOTIFICATION = new DrivingStatusNotification(DRIVING_REJECTION_REASON, DrivingStatus.REJECTED, NOT_REJECTED_DRIVING);

    public static Long VEHICLE_ID_1 = 1L;
    public static Vehicle VEHICLE_1 = new Vehicle(VEHICLE_ID_1, false, false, VEHICLE_TYPE_INFO_SUV, RATE);
    public static Long DRIVER_ID_1 = 1L;
    public static String DRIVER_EMAIL_1 = "driver1@gmail.com";
    public static Driver DRIVER_1 = new Driver(DRIVER_ID_1, DRIVER_EMAIL_1, EXIST_PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, PROFILE_PICTURE, VEHICLE_1, ROLE_DRIVER);
    public static Long VEHICLE_ID_2 = 2L;
    public static Vehicle VEHICLE_2 = new Vehicle(VEHICLE_ID_2, false, false, VEHICLE_TYPE_INFO_SUV, RATE);
    public static Long DRIVER_ID_2 = 2L;
    public static String DRIVER_EMAIL_2 = "driver2@gmail.com";
    public static Driver DRIVER_2 = new Driver(DRIVER_ID_2, DRIVER_EMAIL_2, EXIST_PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, PROFILE_PICTURE, VEHICLE_2, ROLE_DRIVER);
    public static Long VEHICLE_ID_3 = 3L;
    public static Vehicle VEHICLE_3 = new Vehicle(VEHICLE_ID_3, false, false, VEHICLE_TYPE_INFO_SUV, RATE);
    public static Long DRIVER_ID_3 = 3L;
    public static String DRIVER_EMAIL_3 = "driver3@gmail.com";
    public static Driver DRIVER_3 = new Driver(DRIVER_ID_3, DRIVER_EMAIL_3, EXIST_PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, PROFILE_PICTURE, VEHICLE_3, ROLE_DRIVER);

    public static Driver createDriver(
            final Long id,
            final String email,
            final Vehicle vehicle,
            final Location location
    ){
        vehicle.setCurrentStop(location);
        return new Driver(id, email, EXIST_PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, PROFILE_PICTURE, vehicle, ROLE_DRIVER);
    }
}