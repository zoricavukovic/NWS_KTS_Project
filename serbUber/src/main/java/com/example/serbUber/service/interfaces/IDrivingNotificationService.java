package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.ExcessiveNumOfPassengersException;
import com.example.serbUber.exception.InvalidChosenTimeForReservationException;
import com.example.serbUber.exception.PassengerNotHaveTokensException;
import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.request.RouteRequest;
import com.google.maps.errors.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface IDrivingNotificationService {

   DrivingNotificationDTO createDrivingNotificationDTO(
       final RouteRequest routeRequest,
       final String senderEmail,
       final double price,
       final List<String> passengers,
       final double duration,
       final boolean babySeat,
       final boolean petFriendly,
       final String vehicleType,
       final LocalDateTime chosenDateTime,
       final boolean isReservation
    ) throws EntityNotFoundException, ExcessiveNumOfPassengersException, PassengerNotHaveTokensException, InvalidChosenTimeForReservationException, NotFoundException;

    DrivingNotificationDTO updateStatus(final Long id, final String email, final boolean accepted) throws EntityNotFoundException;

    boolean checkIfDrivingNotificationIsOutdated(final DrivingNotification drivingNotification);

    boolean checkIfUsersReviewed(final DrivingNotification drivingNotification) throws PassengerNotHaveTokensException, EntityNotFoundException;

    List<DrivingNotification> getAllReservation();

    List<DrivingNotification> getAllNotReservation();
}
