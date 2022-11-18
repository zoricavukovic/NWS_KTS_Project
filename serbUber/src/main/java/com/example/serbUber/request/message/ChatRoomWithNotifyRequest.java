package com.example.serbUber.request.message;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ChatRoomWithNotifyRequest {

    @Valid
    private ChatRoomRequest chatRoom;

    @NotNull(message = "Notify admin must be selected")
    private boolean notifyAdmin;

    public ChatRoomWithNotifyRequest(
            final ChatRoomRequest chatRoom,
            final boolean notifyAdmin
    ) {
        this.chatRoom = chatRoom;
        this.notifyAdmin = notifyAdmin;
    }

    public ChatRoomRequest getChatRoom() {
        return chatRoom;
    }

    public boolean isNotifyAdmin() {
        return notifyAdmin;
    }
}
