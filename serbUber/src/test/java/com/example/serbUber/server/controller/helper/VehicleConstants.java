package com.example.serbUber.server.controller.helper;

import com.example.serbUber.request.LongLatRequest;
import com.example.serbUber.request.VehicleCurrentPositionRequest;

public class VehicleConstants {

    public static final LongLatRequest LONG_LAT_REQUEST = new LongLatRequest(45.0,45.0);

    public static final VehicleCurrentPositionRequest VEHICLE_CURRENT_POSITION_REQUEST = new VehicleCurrentPositionRequest(LONG_LAT_REQUEST, 0, 1);

    public static final Long INVALID_VEHICLE_WITHOUT_DRIVER_ID = 7L;
    public static final Long EXIST_VEHICLE_ID = 1L;
    public static final Long VEHICLE_WITHOUT_ACTIVE_ROUTE_ID = 2L;

}
