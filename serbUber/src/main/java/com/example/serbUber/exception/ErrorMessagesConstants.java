package com.example.serbUber.exception;

public class ErrorMessagesConstants {

    public static final String WRONG_EMAIL = "Email is not in the right format.";
    public static final String EMPTY_EMAIL = "Email cannot be empty.";
    public static final String TOO_LONG_EMAIL = "Email length is too long. Email cannot contain more than 60 characters.";
    public static final String WRONG_PASSWORD =
                    "Password must contain at least 5 characters. " +
                    "At least one number and one special character.";
    public static final String WRONG_NAME = "Name must contain only letters and cannot be too long.";
    public static final String WRONG_SURNAME = "Surname must contain only letters and cannot be too long.";
    public static final String WRONG_PHONE_NUM = "Phone number must contain 9 digits.";
    public static final String WRONG_CITY = "City must contain between 2 and 30 letters.";
}
