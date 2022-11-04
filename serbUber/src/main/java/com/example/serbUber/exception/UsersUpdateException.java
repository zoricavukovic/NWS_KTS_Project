package com.example.serbUber.exception;

import static com.example.serbUber.exception.ErrorMessagesConstants.USER_UPDATE_MESSAGE;

public class UsersUpdateException extends Exception {


    public UsersUpdateException() {
        super(USER_UPDATE_MESSAGE);
    }

    public UsersUpdateException(String message) {super(message);}
}
