package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingNotificationDTO;
import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.dto.user.DriverDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendVehicleCurrentLocation(List<VehicleCurrentLocationDTO> vehicleDTOs) {
        this.messagingTemplate.convertAndSend("/user/global/connect", vehicleDTOs);
    }

    public void sendDrivingNotification(List<DrivingNotificationDTO> drivingNotificationDTOs) {
        drivingNotificationDTOs.forEach(notification -> {
            this.messagingTemplate.convertAndSendToUser(notification.getReceiverEmail(), "/connect", notification);
        });
    }

    public void sendActivityResetNotification(DriverDTO driverDTO) {
        this.messagingTemplate.convertAndSendToUser(driverDTO.getEmail(), "/connect", driverDTO);
    }
}
