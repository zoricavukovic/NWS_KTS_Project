package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.DrivingLocationIndexRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface IDrivingNotificationService {

   DrivingNotificationDTO createDrivingNotificationDTO( ///driving dto
                                                        final List<DrivingLocationIndexRequest> locations,
                                                        final String senderEmail,
                                                        final double price,
                                                        final List<String> passengers,
                                                        final LocalDateTime started,
                                                        final int duration,
                                                        final boolean babySeat,
                                                        final boolean petFriendly,
                                                        final String vehicleType,
                                                        final double time,
                                                        final double distance

    ) throws EntityNotFoundException;

    DrivingNotificationDTO acceptDriving(final Long id) throws EntityNotFoundException;
    
}
