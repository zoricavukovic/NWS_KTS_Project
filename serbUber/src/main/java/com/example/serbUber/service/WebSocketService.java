package com.example.serbUber.service;

import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void send(List<VehicleCurrentLocationDTO> vehicleDTOs) {
        this.messagingTemplate.convertAndSend("/user/global/connect", vehicleDTOs);
    }
}
