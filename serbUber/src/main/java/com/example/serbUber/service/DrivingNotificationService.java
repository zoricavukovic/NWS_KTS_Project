package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.DrivingNotificationType;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.DrivingNotificationRepository;
import com.example.serbUber.service.interfaces.IDrivingNotificationService;
import com.example.serbUber.service.user.RegularUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Qualifier("drivingNotificationServiceConfiguration")
public class DrivingNotificationService implements IDrivingNotificationService {
    private final DrivingNotificationRepository drivingNotificationRepository;
    private final RegularUserService regularUserService;
    private final WebSocketService webSocketService;

    public DrivingNotificationService(
        final DrivingNotificationRepository drivingNotificationRepository,
        final RegularUserService regularUserService,
        final WebSocketService webSocketService
    ){
        this.drivingNotificationRepository = drivingNotificationRepository;
        this.regularUserService = regularUserService;
        this.webSocketService = webSocketService;
    }

    public List<DrivingNotificationDTO> createNotifications(
            final double lonStarted,
            final double latStarted,
            final double lonEnd,
            final double latEnd,
            final String senderEmail,
            final double price,
            final List<String> passengers
    ) throws EntityNotFoundException {
        RegularUser sender = regularUserService.getRegularByEmail(senderEmail);
        List<DrivingNotificationDTO> notificationDTOs = new ArrayList<>();

        passengers.forEach(passengerEmail -> {
            try {
                notificationDTOs.add(
                    createDrivingNotification(lonStarted, latStarted, lonEnd, latEnd, price, passengerEmail, sender)
                );
            } catch (EntityNotFoundException e) {
                System.out.println("User: " + passengerEmail + " is not found");
            }

        });

        webSocketService.sendDrivingNotification(notificationDTOs);

        return notificationDTOs;
    }

    public DrivingNotificationDTO setDrivingNotificationAnswered(final Long id) throws EntityNotFoundException {
        DrivingNotification drivingNotification = drivingNotificationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING_NOTIFICATION));

        drivingNotification.setRead(true);
        drivingNotificationRepository.save(drivingNotification);

        return new DrivingNotificationDTO(drivingNotification);
    }

    public List<DrivingNotificationDTO> createNotifications(
        final Location startLocation,
        final Location destination,
        final double price,
        final User sender,
        final Set<RegularUser> receivers,
        final DrivingNotificationType notificationType,
        final String reason
    ) {
        List<DrivingNotificationDTO> notificationDTOs = new ArrayList<>();
        receivers.forEach(receiver ->
            notificationDTOs.add(
                createDrivingNotification(startLocation, destination, price, sender, receiver, notificationType, reason)
            )
        );

        return notificationDTOs;
    }

    private DrivingNotificationDTO createDrivingNotification(
        final Location startLocation,
        final Location destination,
        final double price,
        final User sender,
        final User receiver,
        final DrivingNotificationType notificationType,
        final String reason
    ) {
        DrivingNotification drivingNotification =  drivingNotificationRepository.save(
            new DrivingNotification(
                startLocation.getLon(),
                startLocation.getLat(),
                destination.getLon(),
                destination.getLat(),
                price,
                sender,
                receiver,
                notificationType,
                reason
            ));

        return new DrivingNotificationDTO(drivingNotification);
    }

    public DrivingNotificationDTO getDrivingNotification(Long id) throws EntityNotFoundException {
        DrivingNotification drivingNotification =  drivingNotificationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING_NOTIFICATION));

        return new DrivingNotificationDTO(drivingNotification);
    }

    private DrivingNotificationDTO createDrivingNotification(
        final double lonStarted,
        final double latStarted,
        final double lonEnd,
        final double latEnd,
        final double price,
        final String passengerEmail,
        final User sender
    ) throws EntityNotFoundException {
        User passenger = regularUserService.getRegularByEmail(passengerEmail);

        DrivingNotification drivingNotification =  drivingNotificationRepository.save(
            new DrivingNotification(lonStarted, latStarted, lonEnd, latEnd, price, sender, passenger, DrivingNotificationType.LINKED_USER)
        );

        return new DrivingNotificationDTO(drivingNotification);
    }
}
