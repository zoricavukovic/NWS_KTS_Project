package com.example.serbUber.service;

import com.example.serbUber.dto.*;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.DrivingNotificationType;
import com.example.serbUber.model.user.RegularUser;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.serbUber.util.Constants.BLOCKED_NOTIFICATION;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    private final EmailService emailService;

    public WebSocketService(
            final SimpMessagingTemplate messagingTemplate,
            final EmailService emailService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.emailService = emailService;
    }

    public void sendVehicleCurrentLocation(List<VehicleCurrentLocationDTO> vehicleDTOs) {
        this.messagingTemplate.convertAndSend("/user/global/connect", vehicleDTOs);
    }

    public void sendDrivingNotification(
        final DrivingNotificationWebSocketDTO drivingNotificationDTO,
        final Map<RegularUser, Integer> users
    ) {
        users.forEach((key, value) -> {
            this.messagingTemplate.convertAndSendToUser(key.getEmail(), "/connect", drivingNotificationDTO);
        });
        if (drivingNotificationDTO.getDrivingNotificationType().equals(DrivingNotificationType.DELETED)){
            this.messagingTemplate.convertAndSendToUser(drivingNotificationDTO.getSenderEmail(), "/connect", drivingNotificationDTO);
        }
    }

    public void passengerNotAcceptDrivingNotification(Set<RegularUser> regularUsers, String userEmail, String senderEmail) {
        String messageForPassengers =  String.format("Passenger %s not accept ride. Ride is rejected.", userEmail);
        if (regularUsers.size() > 0) {
            regularUsers.forEach(user -> {
                if(!user.getEmail().equals(userEmail)) {
                    this.messagingTemplate.convertAndSendToUser(user.getEmail(), "/passenger-not-accept-driving", messageForPassengers);
                }
            });
        }
        String messageForSender = String.format("Your ride is rejected. Passenger %s is not accept ride.", userEmail);
        this.messagingTemplate.convertAndSendToUser(senderEmail, "/passenger-not-accept-driving", messageForSender);
    }



    public void sendActivityResetNotification(DriverActivityResetNotificationDTO dto) {
        this.messagingTemplate.convertAndSendToUser(dto.getEmail(), "/connect", dto);
    }

    public void sendDrivingStatus(DrivingStatusNotificationDTO dto, Map<RegularUser, Integer> receiversReviewed) {
        if (receiversReviewed.size() > 0) {
            receiversReviewed.forEach((key, value) -> {
                this.messagingTemplate.convertAndSendToUser(key.getEmail(), "/connect", dto);
            });
        }
    }

    public void sendRejectDriving(DrivingStatusNotificationDTO dto, Set<RegularUser> users) {
        if (users.size() > 0) {
            users.forEach(user -> {
                this.messagingTemplate.convertAndSendToUser(user.getEmail(), "/connect", dto);
            });
        }
    }

    public void sendBlockedNotification(final String email, final String reason) {
        this.messagingTemplate.convertAndSendToUser(email, "/connect", BLOCKED_NOTIFICATION);
    }

    public void finishDrivingNotification(SimpleDrivingInfoDTO simpleDrivingInfoDTO, Set<RegularUser> users) {
        if (users.size() > 0) {
            users.forEach(user -> {
                this.messagingTemplate.convertAndSendToUser(user.getEmail(), "/finish-driving", simpleDrivingInfoDTO);
            });
        }
    }

    public void startDrivingNotification(SimpleDrivingInfoDTO simpleDrivingInfoDTO, Set<RegularUser> users) {
        if (users.size() > 0) {
            users.forEach(user -> {
                this.messagingTemplate.convertAndSendToUser(user.getEmail(), "/start-driving", simpleDrivingInfoDTO);
            });
        }
    }

    public void sendVehicleCurrentLocation(VehicleCurrentLocationDTO vehicleCurrentLocationDTO, String driverEmail, Set<RegularUser> users) {
        this.messagingTemplate.convertAndSendToUser(driverEmail, "/update-driving", vehicleCurrentLocationDTO);
        if (users.size() > 0) {
            users.forEach(user -> {
                this.messagingTemplate.convertAndSendToUser(user.getEmail(), "/update-driving", vehicleCurrentLocationDTO);
            });
        }
    }
}
