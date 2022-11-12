package com.example.serbUber.controller;

import com.example.serbUber.dto.MessageDTO;
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
    public void send(@Payload MessageDTO messageReq) {
        //String path = "/user/" + (messageDTO.isAdminResponse() ? messageDTO.getReceiver().getEmail()
        //        : messageDTO.getSender().getEmail());

        this.messagingTemplate.convertAndSendToUser("ana@gmail.com","/messages", messageReq);
    }

}
