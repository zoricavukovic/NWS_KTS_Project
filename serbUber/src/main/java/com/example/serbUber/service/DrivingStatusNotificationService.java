package com.example.serbUber.service;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.DrivingStatusNotification;
import com.example.serbUber.repository.DrivingStatusNotificationRepository;
import com.example.serbUber.service.interfaces.IDrivingStatusNotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("drivingStatusNotificationService")
public class DrivingStatusNotificationService implements IDrivingStatusNotificationService{

    private DrivingStatusNotificationRepository drivingStatusNotificationRepository;

    public DrivingStatusNotificationService(final DrivingStatusNotificationRepository drivingStatusNotificationRepository){
        this.drivingStatusNotificationRepository = drivingStatusNotificationRepository;
    }

    public DrivingStatusNotification create(
            final String reason,
            final DrivingStatus drivingStatus,
            final Driving driving
    ){

        return drivingStatusNotificationRepository.save(new DrivingStatusNotification(reason, drivingStatus, driving));
    }
}
