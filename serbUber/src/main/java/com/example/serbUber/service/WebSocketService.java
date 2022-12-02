package com.example.serbUber.service;

import com.example.serbUber.dto.DriverActivityResetNotificationDTO;
import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void sendDrivingNotification(List<DrivingNotificationDTO> drivingNotificationDTOs) {
        drivingNotificationDTOs.forEach(notification -> {
            this.messagingTemplate.convertAndSendToUser(notification.getReceiverEmail(), "/connect", notification);
        });
    }

    public void sendActivityResetNotification(DriverActivityResetNotificationDTO dto) {
        this.messagingTemplate.convertAndSendToUser(dto.getEmail(), "/connect", dto);
    }

    public void sendBlockedNotification(final String email, final String reason) {
        this.emailService.sendMail(email, "Blocke on SerbUber", reason);
        this.messagingTemplate.convertAndSendToUser(email, "/connect", BLOCKED_NOTIFICATION);
    }
}
