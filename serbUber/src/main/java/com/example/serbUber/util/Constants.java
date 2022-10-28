package com.example.serbUber.util;

public class Constants {

    public static final double STARTING_RATE = 0.0;
    public static final int MIN_LENGTH_OF_MESSAGE = 20;
    public static final int MAX_LENGTH_OF_MESSAGE = 100;
    public static final int MIN_RATE = 0;
    public static final int MAX_RATE = 5;
    public static final String LEGIT_PASSWORD_REG = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{5,100}$";
    public static final String LEGIT_NAME_REG = "^[A-Z]{1,1}[a-z]{1,20}([ ]?[A-Z]?[a-z]{1,20}|[a-z]{1,20})$";
    public static final String LEGIT_PHONE_NUMBER_REG = "^(?!\\s*$)[0-9\\s]{9}$";
}
