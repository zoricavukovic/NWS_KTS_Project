package com.example.serbUber.controller;

import com.example.serbUber.request.message.ChatRoomRequest;
import com.example.serbUber.request.message.ChatRoomWithNotifyRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send/message")
    public void send(@Valid @Payload ChatRoomWithNotifyRequest chatRoomWithNotifyRequest) {

        this.messagingTemplate.convertAndSendToUser(
                chatRoomWithNotifyRequest.getChatRoom().getClient().getEmail(),"/connect",
                chatRoomWithNotifyRequest
        );
        this.messagingTemplate.convertAndSendToUser(
                chatRoomWithNotifyRequest.getChatRoom().getAdmin().getEmail(),"/connect",
                chatRoomWithNotifyRequest
        );
    }

}
