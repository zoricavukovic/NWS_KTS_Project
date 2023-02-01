package com.example.serbUber.server.service.helper;

import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;
import com.example.serbUber.request.*;

import java.util.*;


public class LocationHelper {

    public static final double[] LOCATION_LON_LAT_1 = new double[]{19.839030, 45.247460};
    public static final double[] LOCATION_LON_LAT_2 = new double[]{19.789230, 45.242610, };
    public static final double[] LOCATION_LON_LAT_3 = new double[]{19.837379, 45.2457200};
    public static final double[] NOT_EXIST_LOCATION = new double[]{-1.0, -1.0};
    public static final String CITY = "Novi Sad";
    public static final String STREET_1 = "Lasla Gala";
    public static final String STREET_2 = "Futoski put";
    public static final String NUMBER_1 = "2";
    public static final String NUMBER_2 = "103";
    public static final String NUMBER_3 = "21";
    public static final String ZIP_CODE = "21000";

    public static final Location FIRST_LOCATION = new Location(CITY, STREET_1, NUMBER_1, ZIP_CODE, LOCATION_LON_LAT_1[0], LOCATION_LON_LAT_1[1]);

    public static final Location SECOND_LOCATION = new Location(CITY, STREET_2, NUMBER_2, ZIP_CODE, LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_2[1]);

    public static final Location THIRD_LOCATION = new Location(CITY, STREET_1, NUMBER_3, ZIP_CODE, LOCATION_LON_LAT_3[0], LOCATION_LON_LAT_3[1]);

    public static SortedSet<DrivingLocationIndex> createDrivingLocationIndex() {
        SortedSet<DrivingLocationIndex> locations = new TreeSet<>();
        locations.add(new DrivingLocationIndex(FIRST_LOCATION, 1, 1));
        locations.add(new DrivingLocationIndex(SECOND_LOCATION, 2, -1));

        return locations;
    }

    public static Route createTestRoute(final SortedSet<DrivingLocationIndex> drivingLocationIndexList, final double time, final double distance) {

        return new Route(drivingLocationIndexList, distance, time);
    }



    public static LocationsForRoutesRequest createLocationsForRoutesRequest(int numberOfElementsInList) {
        LongLatRequest firstLonLat = new LongLatRequest(1, 2);
        LongLatRequest secondLonLat = new LongLatRequest(3, 4);
        LongLatRequest thirdLonLat = new LongLatRequest(5, 6);

        List<LongLatRequest> longLats = new LinkedList<>();
        switch (numberOfElementsInList) {
            case 0 -> {
            }
            case 1 ->
                longLats.add(firstLonLat);
            case 2 -> {
                longLats.add(firstLonLat);
                longLats.add(secondLonLat);
            }
            default -> {
                longLats.add(firstLonLat);
                longLats.add(secondLonLat);
                longLats.add(thirdLonLat);
            }
        }

        return new LocationsForRoutesRequest(longLats);
    }

    public static List<DrivingLocationIndexRequest> createDrivingLocationIndexRequests() {
        LocationRequest firstLocationRequest = new LocationRequest(CITY, STREET_1, NUMBER_1, ZIP_CODE, LOCATION_LON_LAT_1[0], LOCATION_LON_LAT_1[1]);
        LocationRequest secondLocationRequest = new LocationRequest(CITY, STREET_2, NUMBER_2, ZIP_CODE, LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_2[1]);

        List<DrivingLocationIndexRequest> locationIndexRequests = new ArrayList<>();
        locationIndexRequests.add(new DrivingLocationIndexRequest(firstLocationRequest, 1));
        locationIndexRequests.add(new DrivingLocationIndexRequest(secondLocationRequest, 2));

        return locationIndexRequests;
    }

    public static RouteRequest createRouteRequest(){
        List<Integer> routePathIndex = Arrays.asList(1, 2);
        RouteRequest routeRequest = new RouteRequest(Constants.TIME_IN_MIN, Constants.DISTANCE, createDrivingLocationIndexRequests(), routePathIndex);

        return routeRequest;
    }
}
