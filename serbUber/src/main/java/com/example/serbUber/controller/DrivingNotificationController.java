package com.example.serbUber.controller;

import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.DrivingNotificationRequest;
import com.example.serbUber.service.DrivingNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/driving-notifications")
public class DrivingNotificationController {
    private SimpMessagingTemplate messagingTemplate;
    private DrivingNotificationService drivingNotificationService;

    public DrivingNotificationController(final SimpMessagingTemplate messagingTemplate, final DrivingNotificationService drivingNotificationService){
        this.messagingTemplate = messagingTemplate;
        this.drivingNotificationService = drivingNotificationService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_REGULAR_USER')")

    public List<DrivingNotificationDTO> create(@Valid @RequestBody DrivingNotificationRequest drivingNotificationRequest) throws EntityNotFoundException {

        return this.drivingNotificationService.createNotifications(
            drivingNotificationRequest.getLonStarted(),
            drivingNotificationRequest.getLatStarted(),
            drivingNotificationRequest.getLonEnd(),
            drivingNotificationRequest.getLatEnd(),
            drivingNotificationRequest.getSenderEmail(),
            drivingNotificationRequest.getPrice(),
            drivingNotificationRequest.getPassengers(),
            drivingNotificationRequest.getStarted(),
            drivingNotificationRequest.getDuration()
        );
    }

    @PostMapping("/answered/{id}")
    @ResponseStatus(HttpStatus.OK)
    private DrivingNotificationDTO setDrivingNotificationAnswered(@PathVariable Long id) throws EntityNotFoundException {

        return this.drivingNotificationService.setDrivingNotificationAnswered(id);
    }
}
