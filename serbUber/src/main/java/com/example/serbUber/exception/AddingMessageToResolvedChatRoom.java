package com.example.serbUber.exception;


import static com.example.serbUber.exception.ErrorMessagesConstants.ADDING_MESSAGE_TO_RESOLVED_CHAT_ROOM;

public class AddingMessageToResolvedChatRoom extends AppException {

    public AddingMessageToResolvedChatRoom() {
        super(ADDING_MESSAGE_TO_RESOLVED_CHAT_ROOM);
    }

    public AddingMessageToResolvedChatRoom(String message) {
        super(message);
    }
}
