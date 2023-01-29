package com.example.serbUber.server.service.helper;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.Driver;

import java.time.LocalDateTime;
import java.util.SortedSet;

public class DrivingConstants {
    public static final Long DRIVING_ID_1 = 1L;
    public static final Driving DRIVING_1 = new Driving(Constants.DURATION, null, null, null, DrivingStatus.ACCEPTED, null, Constants.PRICE);
    public static final Driving DRIVING_2 = new Driving(Constants.DURATION, null, null, null, DrivingStatus.ACCEPTED, null, Constants.PRICE);
    public static final Driving DRIVING_3 = new Driving(Constants.DURATION, null, null, null, DrivingStatus.ACCEPTED, null, Constants.PRICE);

    public static Driving createActiveDriving(int minutesBefore, Driver driver){
        SortedSet<DrivingLocationIndex> locations = LocationHelper.createDrivingLocationIndex();
        Route route = new Route(locations, Constants.TIME_IN_MIN, Constants.DISTANCE);
        Driving driving =  new Driving(Constants.DURATION, LocalDateTime.now().minusMinutes(minutesBefore), null, route, DrivingStatus.ACCEPTED, driver, Constants.PRICE);
        driving.setActive(true);

        return driving;
    }

    public static Driving createFutureDriving(int minutesAfter, Driver driver){
        SortedSet<DrivingLocationIndex> locations = LocationHelper.createDrivingLocationIndex();
        Route route = new Route(locations, Constants.TIME_IN_MIN, Constants.DISTANCE);
        Driving driving =  new Driving(Constants.DURATION, LocalDateTime.now().plusMinutes(minutesAfter), null, route, DrivingStatus.ACCEPTED, driver, Constants.PRICE);

        return driving;
    }
}
