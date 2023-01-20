package com.example.serbUber.util;

import com.example.serbUber.dto.PossibleRoutesViaPointsDTO;
import com.example.serbUber.request.LongLatRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Constants {

    public static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static final String PHOTOS_FILE_PATH = "src/main/resources/static/images/";
    public static final String TARGET_PHOTO_FILE_PATH = "./src/main/resources/static/images/";
    public static final double STARTING_RATE = 0.0;
    public static final double EMPTY_BANK_ACCOUNT = 0.0;
    public static final int ZERO_TOKENS = 0;
    public static final int MAX_LENGTH_OF_MESSAGE = 100;
    public static final int MIN_RATE = 0;
    public static final int MAX_RATE = 5;
    public static final String LEGIT_PASSWORD_REG = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,100}$";
    public static final String LEGIT_NAME_REG = "^[A-Za-z]{1,1}[a-z]{1,20}([ ]?[A-Za-z]?[a-z]{1,20}|[a-z]{1,20})$";
    public static final String LEGIT_PHONE_NUMBER_REG = "^(?!\\s*$)[0-9\\s]{8,12}$";
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
    public static final int MAX_WORKING_MINUTES = 480;
    public static final int HOURS_IN_A_DAY = 24;
    public static final int NUM_OF_LETTERS_REASON_TOO_LONG = 50;
    public static final String ROLE_DRIVER = "ROLE_DRIVER";
    public static final String ROLE_REGULAR_USER = "ROLE_REGULAR_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final int START_LIST_INDEX = 0;
    public static final String BLOCKED_NOTIFICATION = "{\"blockConfirmed\":true}";
    public static final String TRANSACTION_DESCRIPTION = "Buying tokens for SerbUber";
    public static final String PAYMENT_METHOD = "paypal";
    public static final String PAYMENT_INTENT = "sale";
    public static final String REDIRECT_URL_CANCEL = "http://localhost:4200/serb-uber/regular-user/payment/status/-1";
    public static final String REDIRECT_URL_SUCCESS = "http://localhost:4200/serb-uber/regular-user/payment/process-payment";
    public static final String PAYPAL_APPROVAL_URL = "approval_url";
    public static final Long DEFAULT_PAYING_INFO_ID = 1L;
    public static final int MAX_MINUTES_BEFORE_DRIVING_CAN_START = 5;
    public static final Long TAXI_START_LOCATION_ID = 1L;
    public static final String DRIVER_NOT_FOUND_MESSAGE = "Ride is rejected because driver is not found.";
    public static final String UNSUCCESSFUL_PAYMENT_MESSAGE = "Ride is rejected because payment was not successful. Please, check your tokens!";
    public static final String DELETED_DRIVING_MESSAGE = "Ride is rejected because not all linked passengers reviewed invitation.";
    public static final String SUCCESSFUL_DRIVING_CREATION = "Driving is successfully created!";
    public static final String UNSUCCESSFUL_PAYMENT_PATH = "/unsuccessful-payment";
    public static final String DRIVER_NOT_FOUND_PATH = "/driver-not-found";
    public static final double WITHOUT_TOKENS = 0.0;
    public static final double START_MISSING_NUM_OF_TOKENS = 0;
    public static final int HALF_AN_HOUR = 30;
    public static final int TWENTY_MINUTES = 20;
    public static final int TEN_MINUTES = 10;
    public static final double ONE_DRIVING = 1.0;
    public static final double NOT_BY_SPECIFIC_USER = -1;
    public static final boolean SHOULD_REDIRECT = true;

    public static int generateSecurityCode() {
        return (int)(Math.random() * (Constants.MAX_SECURITY_NUM - Constants.MIN_SECURITY_NUM + 1) + Constants.MIN_SECURITY_NUM);
    }

    public static String getAgreementMessage(String user) {

        return String.format("User %s added you as linked passenger. Tap to accept!", user);
    }

    public static String getDrivingNotificationPath(String id) {

        return String.format("serb-uber/user/driving-notification/%s", id);
    }

    public static String getDrivingCreationPath(String id) {

        return String.format("/serb-uber/user/map-page-view/%s", id);
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

    public static List<LocalDate> getDatesBetween(
            LocalDate startDate, LocalDate endDate) {

        return startDate.datesUntil(endDate.plusDays(1))
                .collect(Collectors.toList());
    }
}
