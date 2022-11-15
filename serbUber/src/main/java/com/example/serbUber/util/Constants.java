package com.example.serbUber.util;

import com.example.serbUber.dto.PossibleRoutesViaPointsDTO;
import com.example.serbUber.request.LongLatRequest;

import java.util.List;

public class Constants {

    public static final String PHOTOS_FILE_PATH = "src/main/resources/static/images/";
    public static final String TARGET_PHOTO_FILE_PATH = "./src/main/resources/static/images/";
    public static final double STARTING_RATE = 0.0;
    public static final int MAX_LENGTH_OF_MESSAGE = 100;
    public static final int MIN_RATE = 0;
    public static final int MAX_RATE = 5;
    public static final String LEGIT_PASSWORD_REG = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,100}$";
    public static final String LEGIT_NAME_REG = "^[A-Za-z]{1,1}[a-z]{1,20}([ ]?[A-Za-z]?[a-z]{1,20}|[a-z]{1,20})$";
    public static final String LEGIT_PHONE_NUMBER_REG = "^(?!\\s*$)[0-9\\s]{9}$";
    public static final String POSITIVE_INT_NUM_REG = "^\\d*[1-9]\\d*$";
    public static final int MIN_SECURITY_NUM = 1000;
    public static final int MAX_SECURITY_NUM = 9999;
    public static final double START_RATE = 0.0;
    public static final int START_WORKING_MINUTES = 0;
    public static final String DEFAULT_PICTURE = "default-user.png";
    public static final int YAHOO_PORT_NUM = 587;
    public static final String EMAIL_ADDRESS = "mail_za_isa_mrs@yahoo.com";
    public static final String EMAIL_PASSWORD = "qmnbsxeitomdwqmg";
    public static final int MAX_NUM_VERIFY_TRIES = 3;

    public static final String ROLE_DRIVER = "ROLE_DRIVER";

    public static final String ROLE_REGULAR_USER = "ROLE_REGULAR_USER";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final int START_LIST_INDEX = 0;

    public static int generateSecurityCode() {
        return (int)(Math.random() * (Constants.MAX_SECURITY_NUM - Constants.MIN_SECURITY_NUM + 1) + Constants.MIN_SECURITY_NUM);
    }

    public static String getProfilePicture(String profilePicture) {

        return ((isDefaultPicture(profilePicture)) ? DEFAULT_PICTURE : profilePicture);
    }

    public static int getBeforeLastIndexOfList(List<LongLatRequest> list){

        return list.size()-1;
    }
    private static boolean isDefaultPicture(String profilePicture) {
        return profilePicture == null || profilePicture.equalsIgnoreCase(DEFAULT_PICTURE);
    }


}
