package com.example.serbUber.server.controller.helper;

import org.springframework.http.MediaType;

public class ControllerConstants {

    public static final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype());

    public static final Long EXIST_ID = 1L;
    public static final Long NOT_EXIST_ID = 0L;
    public static final Long DRIVER_ID_FOR_ACTIVE_DRIVING = 14L;

    public static final String INVALID_EMAIL = "ana";
    public static final String NOT_EXIST_USER_EMAIL = "not_found_email@gmail.com";
    public static final String USER_ACTIVE_DRIVING_EMAIL = "milos@gmail.com";
    public static final String USER_DRIVING_EMAIL = "srki@gmail.com";
    public static final String EXIST_EMAIL = "serbuber2@gmail.com";
    public static final String EXIST_PASSENGER_EMAIL = "milos@gmail.com";

    public static final Long NOT_ACTIVE_DRIVING_ID = 3L;
    public static final String STARTED_DRIVING_THREE_ID = "2023-01-25T18:50:00";
    public static final Long ACTIVE_DRIVING_ID = 2L;
    public static final Long DRIVING_CANNOT_BE_STARTED_ID = 4L;
    public static final Long DRIVING_SHOULD_NOT_START_YET_ID = 5L;
    public static final Long DRIVING_CAN_BE_STARTED = 6L;
}
