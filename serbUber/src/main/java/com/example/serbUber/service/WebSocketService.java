package com.example.serbUber.service;

import com.example.serbUber.dto.*;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.DrivingNotification;
import com.example.serbUber.model.user.RegularUser;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
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

    public void sendDrivingStatus(DrivingStatusNotificationDTO dto, Set<RegularUser> receivers, String senderEmail) {
        this.messagingTemplate.convertAndSendToUser(senderEmail, "/connect", dto);
        if(receivers.size() > 0){
            receivers.forEach(receiver -> {
                this.messagingTemplate.convertAndSendToUser(receiver.getEmail(), "/connect", dto);
            });
        }
    }
}
