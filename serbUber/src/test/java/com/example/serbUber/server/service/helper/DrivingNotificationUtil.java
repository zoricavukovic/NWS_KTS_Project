package com.example.serbUber.server.service.helper;

import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.user.RegularUser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.serbUber.server.service.helper.Constants.DURATION;
import static com.example.serbUber.server.service.helper.Constants.ROUTE;
import static com.example.serbUber.server.service.helper.RegularUserConstants.SECOND_USER;
import static com.example.serbUber.server.service.helper.RegularUserConstants.THIRD_USER;
import static com.example.serbUber.server.service.helper.VehicleTypeInfoConstants.VEHICLE_TYPE_INFO_SUV;

public class DrivingNotificationUtil {
    public static DrivingNotification getDrivingNotification(RegularUser regularUser, LocalDateTime started, boolean isReservation, double price, boolean moreUsers) {
        Map<RegularUser, Integer> map = new HashMap<>();
        if (moreUsers){
            map = getUsers();
        }
        return new DrivingNotification(
            ROUTE, price, regularUser, started, DURATION, false, false, VEHICLE_TYPE_INFO_SUV,
            map, isReservation);
    }

    public static Map<RegularUser, Integer> getUsers() {
        HashMap<RegularUser, Integer> map = new HashMap<>();
        map.put(SECOND_USER, 0);
        map.put(THIRD_USER, 0);

        return map;
    }
}
