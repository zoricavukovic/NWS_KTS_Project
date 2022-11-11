package com.example.serbUber.controller;

import com.example.serbUber.request.MessageRequest;
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

    @MessageMapping("/send/message")
    public void send(@Payload MessageRequest messageRequest) {

        this.messagingTemplate.convertAndSend("/socket-publisher", messageRequest.getMessage());
    }

}
