package com.example.serbUber.server.service.helper;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.Driver;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

import static com.example.serbUber.model.DrivingStatus.ACCEPTED;
import static com.example.serbUber.model.DrivingStatus.FINISHED;
import static com.example.serbUber.server.service.helper.Constants.*;
import static com.example.serbUber.server.service.helper.Constants.PRICE;
import static com.example.serbUber.server.service.helper.DriverConstants.EXIST_DRIVER;

public class DrivingConstants {
    public static final Long DRIVING_ID_1 = 1L;
    public static final Driving DRIVING_1 = new Driving(Constants.DURATION, null, null, null, DrivingStatus.ACCEPTED, null, Constants.PRICE);
    public static final Driving DRIVING_2 = new Driving(Constants.DURATION, null, null, null, DrivingStatus.ACCEPTED, null, Constants.PRICE);
    public static final Driving DRIVING_3 = new Driving(Constants.DURATION, null, null, null, DrivingStatus.ACCEPTED, null, Constants.PRICE);

    public static final Driving DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_1 = new Driving(EXIST_OBJECT_ID, DURATION, LocalDateTime.now().plusMinutes(15).plusSeconds(30), null, ROUTE,
            ACCEPTED, EXIST_DRIVER, PRICE, null
    );

    public static final Driving DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_2 = new Driving(EXIST_OBJECT_ID+1, DURATION, LocalDateTime.now().plusMinutes(15).plusSeconds(30), null, ROUTE,
            ACCEPTED, EXIST_DRIVER, PRICE, null
    );

    public static final Driving DRIVING_SHOULD_RECEIVE_FIRST_NOTIFICATION_3 = new Driving(EXIST_OBJECT_ID+2, DURATION, LocalDateTime.now().plusMinutes(15).plusSeconds(30), null, ROUTE,
            ACCEPTED, EXIST_DRIVER, PRICE, LocalDateTime.now().minusMinutes(5).minusSeconds(30)
    );

    public static final Driving DRIVING_SHOULD_NOT_RECEIVE_REMINDER = new Driving(EXIST_OBJECT_ID+4, DURATION, LocalDateTime.now().plusMinutes(16).plusSeconds(30), null, ROUTE,
            ACCEPTED, EXIST_DRIVER, PRICE, null
    );

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

        return new Driving(Constants.DURATION, LocalDateTime.now().plusMinutes(minutesAfter), null, route, DrivingStatus.ACCEPTED, driver, Constants.PRICE);
    }

    public static List<Driving> createDrivingList(int minutesForFirst, int minutesForSecond) {
        LocalDateTime firstStarted = LocalDateTime.now().minusMinutes(10).plusMinutes(minutesForFirst);
        LocalDateTime secondStarted = LocalDateTime.now().minusMinutes(10).plusMinutes(minutesForSecond);

        Driving drivingFirst = new Driving(EXIST_OBJECT_ID, DURATION, firstStarted, null, ROUTE,
                ACCEPTED, EXIST_DRIVER, PRICE
        );
        drivingFirst.setUsers(new HashSet<>());

        Driving drivingSecond = new Driving(EXIST_OBJECT_ID+1, DURATION, secondStarted, null, ROUTE,
                ACCEPTED, EXIST_DRIVER, PRICE
        );
        drivingSecond.setUsers(new HashSet<>());

        List<Driving> drivings = new ArrayList<>();
        drivings.add(drivingFirst);
        drivings.add(drivingSecond);

        return drivings;
    }

    public static Driving createFinishedDriving(Driving driving) {
        driving.setActive(false);
        driving.setDrivingStatus(FINISHED);
        driving.setEnd(LocalDateTime.now());
        driving.getDriver().getVehicle().setCurrentLocationIndex(-1);
        driving.getDriver().getVehicle().setActiveRoute(null);
        driving.getDriver().getVehicle().setCrossedWaypoints(0);
        driving.getDriver().setDrive(false);

        return driving;
    }

    public static Driving createStartedDriving(Driving driving) {
        driving.setStarted(LocalDateTime.now());
        driving.setActive(true);
        driving.getDriver().getVehicle().setActiveRoute(driving.getRoute());
        driving.getDriver().getVehicle().setCurrentLocationIndex(0);
        driving.getDriver().getVehicle().setCrossedWaypoints(0);
        driving.getDriver().getVehicle().setCurrentStop(driving.getRoute().getLocations().first().getLocation());
        driving.getDriver().setDrive(true);
        driving.setDrivingStatus(DrivingStatus.ACCEPTED);

        return driving;
    }


}
