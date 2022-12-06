package com.example.serbUber.controller;

import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.DrivingNotificationRequest;
import com.example.serbUber.service.DrivingNotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/driving-notifications")
public class DrivingNotificationController {
    private final DrivingNotificationService drivingNotificationService;

    public DrivingNotificationController(@Qualifier("drivingNotificationServiceConfiguration") final DrivingNotificationService drivingNotificationService){
        this.drivingNotificationService = drivingNotificationService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_REGULAR_USER')")

    public DrivingNotificationDTO create(@Valid @RequestBody DrivingNotificationRequest drivingNotificationRequest) throws EntityNotFoundException {
        System.out.println("blaaa");
        return this.drivingNotificationService.createDrivingNotificationDTO(
            drivingNotificationRequest.getRoute(),
            drivingNotificationRequest.getSenderEmail(),
            drivingNotificationRequest.getPrice(),
            drivingNotificationRequest.getPassengers(),
            drivingNotificationRequest.getStarted(),
            drivingNotificationRequest.getDuration(),
            drivingNotificationRequest.isBabySeat(),
            drivingNotificationRequest.isPetFriendly(),
            drivingNotificationRequest.getVehicleType()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_REGULAR_USER')")
    @ResponseStatus(HttpStatus.OK)
    public DrivingNotificationDTO acceptDriving(@PathVariable Long id) throws EntityNotFoundException {
        return this.drivingNotificationService.acceptDriving(id);
    }
}
