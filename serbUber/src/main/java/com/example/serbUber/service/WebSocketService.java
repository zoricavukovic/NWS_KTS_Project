package com.example.serbUber.service;

import com.example.serbUber.dto.*;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.user.RegularUser;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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
                drivingNotification.getSender().getEmail()
        );
        drivingNotification.getReceivers().forEach(receiver -> {
            this.messagingTemplate.convertAndSendToUser(receiver.getEmail(), "/connect", dto);
        });
    }

    public void sendActivityResetNotification(DriverActivityResetNotificationDTO dto) {
        this.messagingTemplate.convertAndSendToUser(dto.getEmail(), "/connect", dto);
    }

    public void sendDrivingStatus(DrivingStatusNotificationDTO dto, Set<RegularUser> receivers) {
        if (receivers.size() > 0) {
            receivers.forEach(receiver -> {
                this.messagingTemplate.convertAndSendToUser(receiver.getEmail(), "/connect", dto);
            });
        }
    }
    public void sendBlockedNotification(final String email, final String reason) {
        this.messagingTemplate.convertAndSendToUser(email, "/connect", BLOCKED_NOTIFICATION);
    }
}
