package com.example.serbUber.dto.message;

import com.example.serbUber.model.Message;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class MessageDTO {

    private String message;

    private LocalDateTime timeStamp;

    private boolean adminResponse;

    public MessageDTO(
            final String message,
            final LocalDateTime timeStamp,
            final boolean adminResponse
    ) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.adminResponse = adminResponse;
    }

    public MessageDTO(final Message message) {
        this.message = message.getMessage();
        this.timeStamp = message.getTimeStamp();
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

    public boolean isAdminResponse() {
        return adminResponse;
    }
}
