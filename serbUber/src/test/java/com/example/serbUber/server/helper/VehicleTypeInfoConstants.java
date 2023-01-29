package com.example.serbUber.server.helper;

import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.VehicleTypeInfo;

public class VehicleTypeInfoConstants {
    public static final String SUV = "SUV";
    public static final String CAR = "CAR";
    public static final int SUV_INVALID_NUMBER_OF_PASSENGERS = 1;
    public static final double PRICE_FOR_SUV = 2.0;
    public static final int SUV_NUM_OF_PASSENGERS = 2;

    public static final VehicleTypeInfo VEHICLE_TYPE_INFO_SUV = new VehicleTypeInfo(VehicleType.SUV, PRICE_FOR_SUV, SUV_NUM_OF_PASSENGERS);

}
