package com.example.serbUber.server.controller.helper;

import com.example.serbUber.request.LongLatRequest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RouteConstants {
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
