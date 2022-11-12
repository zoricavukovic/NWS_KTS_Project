package com.example.serbUber.dto.message;

import com.example.serbUber.model.ChatRoom;

import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.dto.message.MessageDTO.fromMessages;

public class ChatRoomDTO {

    private Long id;

    private boolean resolved;

    private MessageUserDTO client;

    private MessageUserDTO admin;

    private List<MessageDTO> messages = new LinkedList<>();

    public ChatRoomDTO(
            final Long id,
            final boolean resolved,
            final MessageUserDTO client,
            final MessageUserDTO admin,
            final List<MessageDTO> messages
    ) {
        this.id = id;
        this.resolved = resolved;
        this.client = client;
        this.admin = admin;
        this.messages = messages;
    }

    public ChatRoomDTO(final ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.resolved = chatRoom.isResolved();
        this.client = new MessageUserDTO(chatRoom.getClient());
        this.admin = new MessageUserDTO(chatRoom.getAdmin());
        this.messages = fromMessages(chatRoom.getMessages());
    }

    public Long getId() {
        return id;
    }

    public boolean isResolved() {
        return resolved;
    }

    public MessageUserDTO getClient() {
        return client;
    }

    public MessageUserDTO getAdmin() {
        return admin;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }
}
