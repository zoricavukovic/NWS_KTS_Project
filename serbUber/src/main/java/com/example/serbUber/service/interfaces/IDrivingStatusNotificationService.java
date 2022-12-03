package com.example.serbUber.service.interfaces;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.DrivingStatusNotification;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface IDrivingStatusNotificationService {
    DrivingStatusNotification create(
            final String reason,
            final DrivingStatus drivingStatus,
            final Driving driving);

}
