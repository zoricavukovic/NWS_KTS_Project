package com.example.serbUber.server.service.helper;

import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.request.DrivingNotificationRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.serbUber.server.controller.helper.ControllerConstants.NOT_EXIST_USER_EMAIL;
import static com.example.serbUber.server.controller.helper.VehicleConstants.VEHICLE_TYPE_SUV;
import static org.mockito.ArgumentMatchers.any;

public class Constants {

    public static final Long NOT_EXIST_OBJECT_ID = -1L;
    public static final Long EXIST_OBJECT_ID = 1L;
    public static final String DRIVING_REJECTION_REASON = "Driving is rejected..";
    public static final double DURATION = 10;
    public static final LocalDateTime STARTED = LocalDateTime.now();
    public static final LocalDateTime END = LocalDateTime.now().plusMinutes((long) DURATION);
    public static final double PRICE = 2;
    public static final String USER_EMAIL = "serbuber@gmail.com";
    public static final String EXIST_PASSWORD = "sifra123@";
    public static final int NOT_REVIEWED_LINKED_REQUEST = 2;
    public static final int ACCEPT_DRIVING = 0;
    public static final double DISTANCE = 5.0;
    public static final double TIME_IN_MIN = 4.0;

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
    public static final Long DRIVER_ID = 1L;

    public static final VehicleTypeInfo VEHICLE_TYPE_INFO = new VehicleTypeInfo();
    public static final VehicleTypeInfo VEHICLE_TYPE_INFO_DEFINED = new VehicleTypeInfo(VehicleType.CAR, 2, 6);
    public static final Vehicle EXIST_VEHICLE = new Vehicle(EXIST_VEHICLE_ID, false, false, VEHICLE_TYPE_INFO_DEFINED, RATE);
    public static final Driver EXIST_DRIVER = new Driver(DRIVER_ID, EXIST_DRIVER_EMAIL, EXIST_PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, PROFILE_PICTURE, EXIST_VEHICLE, ROLE_DRIVER);
    public static final Driving NOT_REJECTED_DRIVING = new Driving(EXIST_OBJECT_ID, DURATION, STARTED, END, new Route(), DrivingStatus.PENDING, EXIST_DRIVER, PRICE);
    public static final DrivingStatusNotification REJECTED_DRIVING_STATUS_NOTIFICATION = new DrivingStatusNotification(DRIVING_REJECTION_REASON, DrivingStatus.REJECTED, NOT_REJECTED_DRIVING);

    public static final double LON = 45.247460;
    public static final double LAT = 19.839030;
    public static final double NEW_LON = 44.0;
    public static final double NEW_LAT = 20.0;
    public static final Location LOCATION = new Location("Novi Sad", "Lasla Gala", "2", "21000", LON, LAT);
    public static final DrivingLocationIndex DRIVING_LOCATION_INDEX = new DrivingLocationIndex(LOCATION, 1, 0);
    public static final SortedSet<DrivingLocationIndex> SORTED_SET_DRIVING_LOCATION_INDEX = new TreeSet<>(new HashSet<DrivingLocationIndex>(Arrays.asList(DRIVING_LOCATION_INDEX)));
    public static final Route ROUTE = new Route(SORTED_SET_DRIVING_LOCATION_INDEX, DISTANCE, TIME_IN_MIN);

    public static final int EXPECTED_CURRENT_LOCATION_INDEX_FOR_STARTED_DRIVING = 0;
    public static final int EXPECTED_CURRENT_LOCATION_INDEX_FOR_FINISHED_DRIVING = -1;
    public static final int EXPECTED_CROSSED_WAYPOINTS = 0;
    public static final int CHOSEN_ROUTE_IDX = 1;

    public static DrivingNotification NOT_RESERVATION_DRIVING_NOTIFICATION_OUTDATED = new DrivingNotification(
            ROUTE, 4.0, new RegularUser(), LocalDateTime.now().minusDays(1), DURATION, false, false, VEHICLE_TYPE_INFO_DEFINED, new HashMap<>(), false
    );

    public static DrivingNotification NOT_RESERVATION_DRIVING_NOTIFICATION = new DrivingNotification(
            ROUTE, 4.0, new RegularUser(), LocalDateTime.now(), DURATION, false, false, VEHICLE_TYPE_INFO_DEFINED, new HashMap<>(), false
    );

    public static DrivingNotification RESERVATION_DRIVING_NOTIFICATION_OUTDATED = new DrivingNotification(
            ROUTE, 4.0, new RegularUser(), LocalDateTime.now().minusDays(1), DURATION, false, false, VEHICLE_TYPE_INFO_DEFINED, new HashMap<>(), true
    );

    public static DrivingNotification RESERVATION_DRIVING_NOTIFICATION = new DrivingNotification(
            ROUTE, 4.0, new RegularUser(), LocalDateTime.now(), DURATION, false, false, VEHICLE_TYPE_INFO_DEFINED, new HashMap<>(), true
    );

}
