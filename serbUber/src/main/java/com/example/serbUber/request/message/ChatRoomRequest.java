package com.example.serbUber.request.message;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;

public class ChatRoomRequest {

    @NotNull(message = MISSING_ID)
    private Long id;

    @NotNull(message = "Resolved must be selected.")
    private boolean resolved;

    @Valid
    @NotNull(message = "Client cannot be empty.")
    private MessageUserRequest client;

    @Valid
    @NotNull(message = "Admin cannot be empty.")
    private MessageUserRequest admin;

    @NotNull(message = "Messages must exist.")
    @Size(min = 1, message = "Messages must exist")
    private List<MessageFromSocketRequest> messages = new LinkedList<>();

    public ChatRoomRequest(
            final Long id,
            final boolean resolved,
            final MessageUserRequest client,
            final MessageUserRequest admin,
            final List<MessageFromSocketRequest> messages) {
        this.id = id;
        this.resolved = resolved;
        this.client = client;
        this.admin = admin;
        this.messages = messages;
    }

    public Long getId() {
        return id;
    }

    public boolean isResolved() {
        return resolved;
    }

    public MessageUserRequest getClient() {
        return client;
    }

    public MessageUserRequest getAdmin() {
        return admin;
    }

    public List<MessageFromSocketRequest> getMessages() {
        return messages;
    }
}
