package com.example.serbUber.request.message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_MESSAGE_LENGTH;
import static com.example.serbUber.util.Constants.MAX_LENGTH_OF_MESSAGE;

public class MessageFromSocketRequest {

    @NotBlank(message = WRONG_MESSAGE_LENGTH)
    @Size(max = MAX_LENGTH_OF_MESSAGE, message = WRONG_MESSAGE_LENGTH)
    private String message;

    @NotNull(message = "Time must exist.")
    private LocalDateTime timeStamp;

    @NotNull(message = "Admin response must be selected.")
    private boolean adminResponse;

    public MessageFromSocketRequest(
            final String message,
            final LocalDateTime timeStamp,
            final boolean adminResponse) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.adminResponse = adminResponse;
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
