package com.example.serbUber.server.controller.helper;

import com.example.serbUber.request.*;
import org.springframework.http.MediaType;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ControllerConstants {

    public static final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype());
    public static final Long NOT_EXIST_ENTITY = 100L;
    public static final Long ACTIVE_DRIVING_ID = 2L;
    public static final Long DRIVING_CANNOT_BE_STARTED_ID = 4L;
    public static final Long DRIVING_SHOULD_NOT_START_YET_ID = 5L;
    public static final Long DRIVING_CAN_BE_STARTED = 6L;

    public static final LongLatRequest FIRST_LONG_LAT_REQUESTS = new LongLatRequest(45.245881,19.837558);
    public static final LongLatRequest SECOND_LONG_LAT_REQUESTS = new LongLatRequest(45.247029,19.833693);
    public static final LongLatRequest THIRD_LONG_LAT_REQUESTS = new LongLatRequest(45.247815,19.830687);
    public static final LongLatRequest LONG_LAT_REQUESTS_WRONG_LAT = new LongLatRequest(5, 0);
    public static final List<LongLatRequest> LONG_LAT_REQUESTS_EMPTY = new LinkedList<>();
    public static final List<LongLatRequest> LONG_LAT_REQUESTS_ONE_ELEMENT = List.of(FIRST_LONG_LAT_REQUESTS);
    public static final List<LongLatRequest> LONG_LAT_REQUESTS_TWO_ELEMENTS = Arrays.asList(FIRST_LONG_LAT_REQUESTS, SECOND_LONG_LAT_REQUESTS);
    public static final List<LongLatRequest> LONG_LAT_REQUESTS_THREE_ELEMENTS =Arrays.asList(FIRST_LONG_LAT_REQUESTS, SECOND_LONG_LAT_REQUESTS, THIRD_LONG_LAT_REQUESTS);

    public static final List<LongLatRequest> LONG_LAT_REQUESTS_TWO_ELEMENTS_WRONG_LAT = Arrays.asList(FIRST_LONG_LAT_REQUESTS, LONG_LAT_REQUESTS_WRONG_LAT);

}
