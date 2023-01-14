package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.ExcessiveNumOfPassengersException;
import com.example.serbUber.exception.InvalidStartedDateTimeException;
import com.example.serbUber.request.DrivingLocationIndexRequest;
import com.example.serbUber.request.RouteRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface IDrivingNotificationService {

   DrivingNotificationDTO createDrivingNotificationDTO( ///driving dto
                                                        final RouteRequest routeRequest,
                                                        final String senderEmail,
                                                        final double price,
                                                        final List<String> passengers,
                                                        final int duration,
                                                        final boolean babySeat,
                                                        final boolean petFriendly,
                                                        final String vehicleType,
                                                        final LocalDateTime chosenDateTime

    ) throws EntityNotFoundException, ExcessiveNumOfPassengersException, InvalidStartedDateTimeException;

    DrivingNotificationDTO updateStatus(final Long id, final String email, final boolean accepted) throws EntityNotFoundException;
    
}
