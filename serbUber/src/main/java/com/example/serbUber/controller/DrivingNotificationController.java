package com.example.serbUber.controller;

import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.ExcessiveNumOfPassengersException;
import com.example.serbUber.exception.InvalidChosenTimeForReservationException;
import com.example.serbUber.exception.PassengerNotHaveTokensException;
import com.example.serbUber.request.DrivingNotificationRequest;
import com.example.serbUber.service.DrivingNotificationService;
import com.google.maps.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

@RestController
@RequestMapping("/driving-notifications")
@Validated
public class DrivingNotificationController {
    private final DrivingNotificationService drivingNotificationService;

    public DrivingNotificationController(@Qualifier("drivingNotificationServiceConfiguration") final DrivingNotificationService drivingNotificationService){
        this.drivingNotificationService = drivingNotificationService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_REGULAR_USER')")
    public DrivingNotificationDTO create(@Valid @RequestBody DrivingNotificationRequest drivingNotificationRequest)
        throws EntityNotFoundException,
        ExcessiveNumOfPassengersException,
        PassengerNotHaveTokensException,
        InvalidChosenTimeForReservationException,
        NotFoundException
    {

        return this.drivingNotificationService.createDrivingRequest(
            drivingNotificationRequest.getRoute(),
            drivingNotificationRequest.getSenderEmail(),
            drivingNotificationRequest.getPrice(),
            drivingNotificationRequest.getPassengers(),
            drivingNotificationRequest.getRoute().getTimeInMin(),
            drivingNotificationRequest.isBabySeat(),
            drivingNotificationRequest.isPetFriendly(),
            drivingNotificationRequest.getVehicleType(),
            drivingNotificationRequest.getChosenDateTime().toLocalDateTime(),
            drivingNotificationRequest.isReservation()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_REGULAR_USER')")
    public DrivingNotificationDTO get(@PathVariable final Long id) throws EntityNotFoundException {

        return drivingNotificationService.getDTO(id);
    }

    @GetMapping("/user-reviewed/{notificationId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_REGULAR_USER')")
    public boolean isUserReviewed(@PathVariable final Long notificationId, @PathVariable final Long userId){

        return drivingNotificationService.isUserReviewed(notificationId, userId);
    }

    @PutMapping("/update-status/{id}/{accepted}/{email}")
    @PreAuthorize("hasAnyRole('ROLE_REGULAR_USER')")
    @ResponseStatus(HttpStatus.OK)
    public DrivingNotificationDTO acceptDriving(
        @Valid @PathVariable Long id,
        @Valid @PathVariable boolean accepted,
        @Valid @Email(message = WRONG_EMAIL) @PathVariable String email
    ) throws EntityNotFoundException {

        return this.drivingNotificationService.updateStatus(id, email, accepted);
    }
}
