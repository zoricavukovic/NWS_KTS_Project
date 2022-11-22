package com.example.serbUber.controller;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.DrivingNotificationRequest;
import com.example.serbUber.service.DrivingNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/driving-notifications")
public class DrivingNotificationController {
    private SimpMessagingTemplate messagingTemplate;
    private DrivingNotificationService drivingNotificationService;

    public DrivingNotificationController(final SimpMessagingTemplate messagingTemplate, final DrivingNotificationService drivingNotificationService){
        this.messagingTemplate = messagingTemplate;
        this.drivingNotificationService = drivingNotificationService;
    }

    @MessageMapping("/send/notification")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public void send(@Payload @Valid DrivingNotificationRequest drivingNotificationRequest) {
        System.out.println(drivingNotificationRequest.getPrice() + "blaaaa");
        for(String email : drivingNotificationRequest.getPassengers()) {
            this.messagingTemplate.convertAndSendToUser(email, "/connect", drivingNotificationRequest);
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody DrivingNotificationRequest drivingNotificationRequest) throws EntityNotFoundException {
        this.drivingNotificationService.create(
                drivingNotificationRequest.getLonStarted(),
                drivingNotificationRequest.getLatStarted(),
                drivingNotificationRequest.getLonEnd(),
                drivingNotificationRequest.getLatEnd(),
                drivingNotificationRequest.getSenderEmail(),
                drivingNotificationRequest.getPrice(),
                drivingNotificationRequest.getPassengers()
        );
    }
}
