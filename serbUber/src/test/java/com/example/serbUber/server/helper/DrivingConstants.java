package com.example.serbUber.server.helper;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.Driver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;

import static com.example.serbUber.server.helper.Constants.*;
import static com.example.serbUber.server.helper.LocationHelper.createDrivingLocationIndex;

public class DrivingConstants {
    public static final Long DRIVING_ID_1 = 1L;
    public static final Driving DRIVING_1 = new Driving(DURATION, null, null, null, DrivingStatus.ACCEPTED, null, PRICE);
    public static final Driving DRIVING_2 = new Driving(DURATION, null, null, null, DrivingStatus.ACCEPTED, null, PRICE);
    public static final Driving DRIVING_3 = new Driving(DURATION, null, null, null, DrivingStatus.ACCEPTED, null, PRICE);

    public static Driving createActiveDriving(int minutesBefore, Driver driver){
        SortedSet<DrivingLocationIndex> locations = createDrivingLocationIndex();
        Route route = new Route(locations, TIME_IN_MIN, DISTANCE);
        Driving driving =  new Driving(DURATION, LocalDateTime.now().minusMinutes(minutesBefore), null, route, DrivingStatus.ACCEPTED, driver, PRICE);
        driving.setActive(true);

        return driving;
    }

    public static Driving createFutureDriving(int minutesAfter, Driver driver){
        SortedSet<DrivingLocationIndex> locations = createDrivingLocationIndex();
        Route route = new Route(locations, TIME_IN_MIN, DISTANCE);
        Driving driving =  new Driving(DURATION, LocalDateTime.now().plusMinutes(minutesAfter), null, route, DrivingStatus.ACCEPTED, driver, PRICE);

        return driving;
    }
}
