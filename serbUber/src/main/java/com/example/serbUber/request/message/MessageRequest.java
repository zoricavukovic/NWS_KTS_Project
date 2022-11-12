package com.example.serbUber.request.message;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;
import static com.example.serbUber.exception.ErrorMessagesConstants.TOO_LONG_EMAIL;
import static com.example.serbUber.util.Constants.MAX_LENGTH_OF_MESSAGE;
import static com.example.serbUber.util.Constants.MIN_LENGTH_OF_MESSAGE;

public class MessageRequest {

    //moze biti null, ako nema chat room-a, pa se pravi novi
    private Long chatId;

    @NotBlank(message = WRONG_MESSAGE_LENGTH)
    @Size(min = MIN_LENGTH_OF_MESSAGE, max = MAX_LENGTH_OF_MESSAGE, message = WRONG_MESSAGE_LENGTH)
    private String message;

    @Email(message = WRONG_EMAIL)
    @NotBlank(message = EMPTY_EMAIL)
    @Size(max = 60, message = TOO_LONG_EMAIL)
    private String senderEmail;

    //moze biti null ako je prva poruka koja se salje adminima
    private String receiverEmail;

    @NotNull(message = "Admin response must be selected.")
    private boolean adminResponse;

    public MessageRequest(
            final Long chatId,
            final String message,
            final String senderEmail,
            final String receiverEmail,
            final boolean adminResponse
    ) {
        this.chatId = chatId;
        this.message = message;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.adminResponse = adminResponse;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public boolean isAdminResponse() {
        return adminResponse;
    }
}
