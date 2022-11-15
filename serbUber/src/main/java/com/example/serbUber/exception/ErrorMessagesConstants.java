package com.example.serbUber.exception;

public class ErrorMessagesConstants {

    public static final String WRONG_EMAIL = "Email is not in the right format.";
    public static final String EMPTY_EMAIL = "Email cannot be empty.";
    public static final String TOO_LONG_EMAIL = "Email length is too long. Email cannot contain more than 60 characters.";
    public static final String WRONG_PASSWORD =
                    "Password must contain at least 8 characters. " +
                    "At least one number and one special character.";
    public static final String WRONG_NAME = "Name must contain only letters and cannot be too long.";
    public static final String WRONG_SURNAME = "Surname must contain only letters and cannot be too long.";
    public static final String WRONG_PHONE_NUM = "Phone number must contain 9 digits.";
    public static final String WRONG_CITY = "City must contain between 2 and 30 letters.";
    public static final String WRONG_MESSAGE_LENGTH = "Message must have between 5-50 characters!";
    public static final String WRONG_RATE = "Rate must be value between 1 and 5!";
    public static final String WRONG_KM_NUM = "Kilometers must be greater than 0.";
    public static final String PASSWORDS_DO_NOT_MATCH_MESSAGE = "Passwords don't match. Try again.";
    public static final String UPDATE_ERROR_MESSAGE = "Entity cannot be updated due to server error.";
    public static final String WRONG_SECURITY_CODE = "Security code is number greater than 0.";
    public static final String WRONG_VERIFY_ID = "Verify id must be added.";
    public static final String NO_AVAILABLE_ADMIN_EXC = "Chat cannot be created right now, all out admins are busy.";
    public static final String ADDING_MESSAGE_TO_RESOLVED_CHAT_ROOM = "You cannot add message to resolved chat room.";

}
