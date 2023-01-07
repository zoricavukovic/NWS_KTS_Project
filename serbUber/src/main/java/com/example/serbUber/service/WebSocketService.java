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

    public void sendDrivingNotification(DrivingNotification drivingNotification) {
        DrivingNotificationWebSocketDTO dto = new DrivingNotificationWebSocketDTO(
                drivingNotification.getId(),
                drivingNotification.getSender().getEmail(),
                DrivingNotificationType.LINKED_USER
        );
        drivingNotification.getReceiversReviewed().forEach((key, value) -> {
            this.messagingTemplate.convertAndSendToUser(key.getEmail(), "/connect", dto);
        });
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
}
