package com.example.serbUber.dto;

import java.util.LinkedList;
import java.util.List;

public class ChatRoomDTO {

    private Long userId;

    private String userFullName;

    private String role;

    private List<MessageDTO> messages = new LinkedList<>();

    public ChatRoomDTO(Long userId, String userFullName, String role, List<MessageDTO> messages) {
        this.userId = userId;
        this.userFullName = userFullName;
        this.role = role;
        this.messages = messages;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public String getRole() {
        return role;
    }

    public List<MessageDTO> addMessages(MessageDTO messageDTO) {
        this.messages.add(messageDTO);

        return messages;
    }
}
