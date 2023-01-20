package com.example.serbUber.controller;

import com.example.serbUber.request.message.ChatRoomRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private SimpMessagingTemplate messagingTemplate;

    public ChatController(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send")
    public void send(@Payload ChatRoomRequest chatRoomRequest) {

        this.messagingTemplate.convertAndSendToUser(chatRoomRequest.getClient().getEmail(),"/connect", "imas poruku");
        this.messagingTemplate.convertAndSendToUser(chatRoomRequest.getAdmin().getEmail(),"/connect", "imas poruku");
    }

}
