package com.example.serbUber.server.helper;

import com.example.serbUber.model.Location;

public class LocationConstants {
    //LOCATION
    public static final double[] LOCATION_LON_LAT_1 = new double[]{19.3443, 45.332};
    public static final double[] LOCATION_LON_LAT_2 = new double[]{18.6, 45};
    public static final double[] NOT_EXIST_LOCATION = new double[]{-1.0, -1.0};
    public static final String CITY = "Novi Sad";
    public static final String STREET_1 = "Lasla Gala";
    public static final String STREET_2 = "Futoski put";
    public static final String NUMBER_1 = "21";
    public static final String NUMBER_2 = "103";
    public static final String ZIP_CODE = "21000";

    public static final Location FIRST_LOCATION = new Location(CITY, STREET_1, NUMBER_1, ZIP_CODE, LOCATION_LON_LAT_1[0], LOCATION_LON_LAT_1[1]);

    public static final Location SECOND_LOCATION = new Location(CITY, STREET_2, NUMBER_2, ZIP_CODE, LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_2[1]);
}
