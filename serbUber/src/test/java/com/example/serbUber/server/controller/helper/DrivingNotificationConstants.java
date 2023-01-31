package com.example.serbUber.server.controller.helper;

import com.example.serbUber.request.DrivingLocationIndexRequest;
import com.example.serbUber.request.DrivingNotificationRequest;
import com.example.serbUber.request.LocationRequest;
import com.example.serbUber.request.RouteRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


public class DrivingNotificationConstants {
    public static final double PRICE = 10;
    public static final String NOT_FOUND_EMAIL = "not_found_email@gmail.com";
    public static final String EXIST_EMAIL = "serbuber2@gmail.com";
    public static final String EXIST_PASSENGER_EMAIL = "milos@gmail.com";
    public static final RouteRequest EMPTY_ROUTE_REQUEST = new RouteRequest();
    public static final String NOT_EXIST_VEHICLE_TYPE = "NOT_EXIST";
    public static final String EXIST_VEHICLE_TYPE = "SUV";
    public static final LocationRequest locationRequest = new LocationRequest(
        "Novi Sad",
        "Bulevar Evrope",
        "20",
        "21000",
        19.8187936,45.246117299999
    );

    public static final LocationRequest locationRequest2 = new LocationRequest(
        "Novi Sad",
        "Bulevar Cara Lazara",
        "10",
        "21000",
        19.8485285, 45.2467885
    );

    public static final DrivingLocationIndexRequest drivingLocationIndexRequest = new DrivingLocationIndexRequest(
        locationRequest,
        1
    );

    public static final DrivingLocationIndexRequest drivingLocationIndexRequest2 = new DrivingLocationIndexRequest(
        locationRequest2,
        2
    );

    public static final RouteRequest route = new RouteRequest(
        5,
        3361.9,
        Arrays.asList(drivingLocationIndexRequest, drivingLocationIndexRequest2),
        List.of(0)
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_SENDER_NOT_FOUND = new DrivingNotificationRequest(
        EMPTY_ROUTE_REQUEST, PRICE, NOT_FOUND_EMAIL, new LinkedList<>(), false,
        false, EXIST_VEHICLE_TYPE, new Timestamp((new Date()).getTime())
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_PASSENGER_NOT_FOUND = new DrivingNotificationRequest(
        EMPTY_ROUTE_REQUEST, PRICE, EXIST_EMAIL, List.of(NOT_FOUND_EMAIL), false,
        false, EXIST_VEHICLE_TYPE, new Timestamp((new Date()).getTime())
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_VEHICLE_TYPE_NOT_FOUND = new DrivingNotificationRequest(
        EMPTY_ROUTE_REQUEST, PRICE, EXIST_EMAIL, List.of(EXIST_PASSENGER_EMAIL), false,
        false, NOT_EXIST_VEHICLE_TYPE, new Timestamp((new Date()).getTime())
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_EXCESSIVE_NUM_OF_PASSENGERS_ON_EDGE = new DrivingNotificationRequest(
        EMPTY_ROUTE_REQUEST, PRICE, EXIST_EMAIL, Arrays.asList(EXIST_PASSENGER_EMAIL, EXIST_PASSENGER_EMAIL,EXIST_PASSENGER_EMAIL,EXIST_PASSENGER_EMAIL, EXIST_PASSENGER_EMAIL),
        false, false, EXIST_VEHICLE_TYPE, new Timestamp((new Date()).getTime())
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_EXCESSIVE_NUM_OF_PASSENGERS = new DrivingNotificationRequest(
        EMPTY_ROUTE_REQUEST, PRICE, EXIST_EMAIL, Arrays.asList(EXIST_PASSENGER_EMAIL, EXIST_PASSENGER_EMAIL,EXIST_PASSENGER_EMAIL,EXIST_PASSENGER_EMAIL, EXIST_PASSENGER_EMAIL, EXIST_PASSENGER_EMAIL),
        false, false, EXIST_VEHICLE_TYPE, new Timestamp((new Date()).getTime())
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_INVALID_TIME_30_MIN = new DrivingNotificationRequest(
        route, PRICE, EXIST_EMAIL, List.of(EXIST_PASSENGER_EMAIL),
        false, false, EXIST_VEHICLE_TYPE, Timestamp.valueOf(LocalDateTime.now().plusMinutes(29)), true
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_INVALID_TIME_5_HOURS = new DrivingNotificationRequest(
        route, PRICE, EXIST_EMAIL, List.of(EXIST_PASSENGER_EMAIL),
        false, false, EXIST_VEHICLE_TYPE, Timestamp.valueOf(LocalDateTime.now().plusHours(6)), true
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_NUM_OF_PASSENGERS_IS_GRATER_THAN_ZERO = new DrivingNotificationRequest(
        route, PRICE, EXIST_EMAIL, List.of(EXIST_PASSENGER_EMAIL),
        false, false, EXIST_VEHICLE_TYPE, new Timestamp((new Date()).getTime())
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_WITHOUT_PASSENGERS_AND_IS_RESERVATION = new DrivingNotificationRequest(
        route, 15, EXIST_EMAIL, new LinkedList<>(),
        false, false, EXIST_VEHICLE_TYPE, Timestamp.valueOf(LocalDateTime.now().plusMinutes(35)), true
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_WITHOUT_TOKEN_BANK = new DrivingNotificationRequest(
        route, 15, "userwithoutbank@gmail.com", new LinkedList<>(),
        false, false, EXIST_VEHICLE_TYPE, Timestamp.valueOf(LocalDateTime.now().plusMinutes(35))
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_USER_DOESNOT_HAVE_ENOUGH_MONEY = new DrivingNotificationRequest(
        route, 100, EXIST_EMAIL, new LinkedList<>(),
        false, false, EXIST_VEHICLE_TYPE, Timestamp.valueOf(LocalDateTime.now().plusMinutes(35))
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_PRICE_LESS_THAN_ZERO = new DrivingNotificationRequest(
        route, -10, EXIST_EMAIL, new LinkedList<>(),
        false, false, EXIST_VEHICLE_TYPE, Timestamp.valueOf(LocalDateTime.now().plusMinutes(35))
    );

    public static final DrivingNotificationRequest DRIVING_NOTIFICATION_REQUEST_SENDER_EMAIL_IS_NOT_VALID = new DrivingNotificationRequest(
        route, 10, "novValid", new LinkedList<>(),
        false, false, EXIST_VEHICLE_TYPE, Timestamp.valueOf(LocalDateTime.now().plusMinutes(35))
    );
}
