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
import static com.example.serbUber.util.Constants.DELETED_DRIVING_MESSAGE;

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

    public void sendVehicleCurrentLocation(VehicleCurrentLocationDTO vehicleDTO) {
        this.messagingTemplate.convertAndSend("/user/global/connect", vehicleDTO);
    }

    public void sendDeletedDrivingNotification(
        final DrivingNotificationWebSocketDTO drivingNotificationDTO,
        final Map<RegularUser, Integer> users
    ) {
        users.forEach((key, value) -> {
            this.messagingTemplate.convertAndSendToUser(key.getEmail(), "/delete-driving", DELETED_DRIVING_MESSAGE);
        });
        this.messagingTemplate.convertAndSendToUser(drivingNotificationDTO.getSenderEmail(), "/delete-driving-creator", DELETED_DRIVING_MESSAGE);
    }


    public void sendPassengerAgreementNotification( final DrivingNotificationWebSocketDTO drivingNotificationDTO,
                                                    final Map<RegularUser, Integer> users){
        users.forEach((key, value) -> {
            this.messagingTemplate.convertAndSendToUser(key.getEmail(), "/agreement-passenger", drivingNotificationDTO);
        });
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
        this.messagingTemplate.convertAndSendToUser(senderEmail, "/passenger-not-accept-driving-creator", messageForSender);
    }



    public void sendActivityResetNotification(DriverActivityResetNotificationDTO dto) {
        this.messagingTemplate.convertAndSendToUser(dto.getEmail(), "/connect", dto);
    }

    public void sendDrivingStatus(String destinationPath, String message, Map<RegularUser, Integer> receiversReviewed) {
        if (receiversReviewed.size() > 0) {
            receiversReviewed.forEach((key, value) -> {
                this.messagingTemplate.convertAndSendToUser(key.getEmail(), destinationPath, message);
            });
        }
    }

    public void sendSuccessfulDriving(DrivingStatusNotificationDTO dto,  Map<RegularUser, Integer> receiversReviewed){
        if (receiversReviewed.size() > 0) {
            receiversReviewed.forEach((key, value) -> {
                this.messagingTemplate.convertAndSendToUser(key.getEmail(), "/successful-driving", dto);
            });
        }
    }

    public void sendRejectDriving(String driverEmail, String reason, Set<RegularUser> users) {
        String message = String.format("Driver %s reject your driving. Reason for rejecting is %s", driverEmail, reason);
        if (users.size() > 0) {
            users.forEach(user -> {
                this.messagingTemplate.convertAndSendToUser(user.getEmail(), "/reject-driving", message);
            });
        }
    }

    public void sendBlockedNotification(final String email) {
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

    public void sendReservationReminder(Set<RegularUser> users, int minutes){
        String message = String.format("Your future ride start in %d minutes. Tap to see details.", minutes);
        if (users.size() > 0) {
            users.forEach(user -> {
                this.messagingTemplate.convertAndSendToUser(user.getEmail(), "/reservation-reminder", message);
            });
        }
    }

    public void sendNewDrivingNotification(DrivingStatusNotificationDTO drivingStatusNotificationDTO, String driverEmail) {
        this.messagingTemplate.convertAndSendToUser(driverEmail, "/new-driving", drivingStatusNotificationDTO);
    }
}
