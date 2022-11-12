package com.example.serbUber.dto;

import com.example.serbUber.dto.user.MessageUserDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.model.Message;
import com.example.serbUber.model.user.User;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class MessageDTO {

    private String message;

    private LocalDateTime timeStamp;

    private MessageUserDTO sender;

    private MessageUserDTO receiver;

    private boolean adminResponse;

    public MessageDTO(
            final String message,
            final LocalDateTime timeStamp,
            final MessageUserDTO sender,
            final MessageUserDTO receiver,
            final boolean adminResponse) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.sender = sender;
        this.receiver = receiver;
        this.adminResponse = adminResponse;
    }

    public MessageDTO(final Message message) {
        this.message = message.getMessage();
        this.timeStamp = message.getTimeStamp();
        this.sender = new MessageUserDTO(message.getSender());
        this.receiver = new MessageUserDTO(message.getReceiver());
        this.adminResponse = message.isAdminResponse();
    }

    public static List<MessageDTO> fromMessages(final List<Message> messages){
        List<MessageDTO> messageDTOs = new LinkedList<>();
        messages.forEach(message ->
                messageDTOs.add(new MessageDTO(message))
        );

        return messageDTOs;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public MessageUserDTO getSender() {
        return sender;
    }

    public MessageUserDTO getReceiver() {
        return receiver;
    }

    public boolean isAdminResponse() {
        return adminResponse;
    }
}
