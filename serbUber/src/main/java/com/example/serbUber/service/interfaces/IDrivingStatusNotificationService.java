package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.DrivingStatusNotificationDTO;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.DrivingStatusNotification;
import com.example.serbUber.model.user.Driver;
import org.springframework.stereotype.Service;

@Service
public interface IDrivingStatusNotificationService {
    DrivingStatusNotification create(
            final String reason,
            final DrivingStatus drivingStatus,
            final Driving driving
    );
}
