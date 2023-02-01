package com.example.serbUber.server.controller.helper;

import com.example.serbUber.request.LongLatRequest;
import com.example.serbUber.request.VehicleCurrentPositionRequest;

import static com.example.serbUber.server.controller.helper.RouteConstants.FIRST_LONG_LAT_REQUESTS;

public class VehicleConstants {

    public static final Long INVALID_VEHICLE_WITHOUT_DRIVER_ID = 7L;
    public static final Long VEHICLE_WITHOUT_ACTIVE_ROUTE_ID = 2L;
    public static final String NOT_EXIST_VEHICLE_TYPE = "NOT_EXIST";
    public static final String VEHICLE_TYPE_SUV = "SUV";
    public static final int NUM_OF_SEATS_SUV = 5;

    public static final VehicleCurrentPositionRequest VEHICLE_CURRENT_POSITION_REQUEST = new VehicleCurrentPositionRequest(FIRST_LONG_LAT_REQUESTS, 0, 1);
    public static VehicleCurrentPositionRequest createVehicleCurrentPositionRequest(double lng, double lat, int crossedWaypoints, int chosenRouteIdx){
        LongLatRequest longLatRequest = new LongLatRequest(lng, lat);

        return new VehicleCurrentPositionRequest(longLatRequest, crossedWaypoints, chosenRouteIdx);
    }
}
