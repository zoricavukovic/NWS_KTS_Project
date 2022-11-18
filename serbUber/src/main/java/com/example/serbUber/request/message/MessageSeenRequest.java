package com.example.serbUber.request.message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;

public class MessageSeenRequest {

    @NotNull(message = MISSING_ID)
    private Long chatRoomId;

    @NotNull(message = "Role of logged user cannot be empty.")
    private boolean adminLogged;

    public MessageSeenRequest(Long chatRoomId, boolean adminLogged) {
        this.chatRoomId = chatRoomId;
        this.adminLogged = adminLogged;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public boolean isAdminLogged() {
        return adminLogged;
    }
}
