package com.example.serbUber.server.helper;

import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.Role;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.example.serbUber.server.helper.LocationHelper.*;
import static org.mockito.ArgumentMatchers.any;

public class Constants {

    public static final Long NOT_EXIST_OBJECT_ID = -1L;
    public static final Long EXIST_OBJECT_ID = 1L;
    public static final String DRIVING_REJECTION_REASON = "Driving is rejected..";
    public static final double DURATION = 3;
    public static final LocalDateTime STARTED = LocalDateTime.now();
    public static final LocalDateTime END = LocalDateTime.now().plusMinutes((long) DURATION);
    public static final double PRICE = 2;
    public static final String USER_EMAIL = "serbuber@gmail.com";
    public static final String EXIST_PASSWORD = "sifra123@";
    public static final int NOT_REVIEWED_LINKED_REQUEST = 2;
    public static final int ACCEPT_DRIVING = 0;

    //VEHICLE
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

    public static final VehicleTypeInfo VEHICLE_TYPE_INFO = new VehicleTypeInfo();
    public static final VehicleTypeInfo VEHICLE_TYPE_INFO_DEFINED = new VehicleTypeInfo(VehicleType.CAR, 2, 6);
    public static Vehicle EXIST_VEHICLE = new Vehicle(EXIST_VEHICLE_ID, false, false, VEHICLE_TYPE_INFO_DEFINED, RATE);
    public static Driver EXIST_DRIVER = new Driver(DRIVER_ID, EXIST_DRIVER_EMAIL, EXIST_PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, PROFILE_PICTURE, EXIST_VEHICLE, ROLE_DRIVER);
    public static Driving NOT_REJECTED_DRIVING = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, END, new Route(), DrivingStatus.PENDING, EXIST_DRIVER, PRICE);
    public static DrivingStatusNotification REJECTED_DRIVING_STATUS_NOTIFICATION = new DrivingStatusNotification(DRIVING_REJECTION_REASON, DrivingStatus.REJECTED, NOT_REJECTED_DRIVING);


    public static double DISTANCE = 5.0;
    public static double TIME_IN_MIN = 10.0;
    public static Location LOCATION = new Location("Novi Sad", "Lasla Gala", "2", "21000", 45.247460, 19.839030);
    public static DrivingLocationIndex DRIVING_LOCATION_INDEX = new DrivingLocationIndex(LOCATION, 1, 0);
    public static SortedSet<DrivingLocationIndex> SORTED_SET_DRIVING_LOCATION_INDEX = new TreeSet<>(new HashSet<DrivingLocationIndex>(Arrays.asList(DRIVING_LOCATION_INDEX)));
    public static Route ROUTE = new Route(SORTED_SET_DRIVING_LOCATION_INDEX, DISTANCE, DURATION);


}
