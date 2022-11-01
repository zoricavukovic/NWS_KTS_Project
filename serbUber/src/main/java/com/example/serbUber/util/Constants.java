package com.example.serbUber.util;

public class Constants {

    public static final String FRONT_VERIFY_URL = "http://localhost:4200/verify/";
    public static final double STARTING_RATE = 0.0;
    public static final int MIN_LENGTH_OF_MESSAGE = 20;
    public static final int MAX_LENGTH_OF_MESSAGE = 100;
    public static final int MIN_RATE = 0;
    public static final int MAX_RATE = 5;
    public static final String LEGIT_PASSWORD_REG = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{5,100}$";
    public static final String LEGIT_NAME_REG = "^[A-Z]{1,1}[a-z]{1,20}([ ]?[A-Z]?[a-z]{1,20}|[a-z]{1,20})$";
    public static final String LEGIT_PHONE_NUMBER_REG = "^(?!\\s*$)[0-9\\s]{9}$";
    public static final String POSITIVE_INT_NUM_REG = "^\\d*[1-9]\\d*$";
    public static final int MIN_SECURITY_NUM = 1000;
    public static final int MAX_SECURITY_NUM = 9999;
    public static final double START_RATE = 0.0;
    public static final int START_WORKING_MINUTES = 0;
    public static final String DEFAULT_PICTURE = "default.jpg";
    public static final int YAHOO_PORT_NUM = 587;
    public static final String EMAIL_ADDRESS = "mail_za_isa_mrs@yahoo.com";
    public static final String EMAIL_PASSWORD = "qmnbsxeitomdwqmg";
    public static final int MAX_NUM_VERIFY_TRIES = 3;

    public static int generateSecurityCode() {
        return (int)(Math.random() * (Constants.MAX_SECURITY_NUM - Constants.MIN_SECURITY_NUM + 1) + Constants.MIN_SECURITY_NUM);
    }

    public static String getProfilePicture(String profilePicture) {
        return (profilePicture == null) ? DEFAULT_PICTURE : profilePicture;
    }
}
