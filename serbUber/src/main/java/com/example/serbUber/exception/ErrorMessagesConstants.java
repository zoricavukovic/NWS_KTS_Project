package com.example.serbUber.exception;

import static com.example.serbUber.util.Constants.FIVE_MINUTES;

public class ErrorMessagesConstants {

    public static final String WRONG_EMAIL = "Email is not in the right format.";
    public static final String EMPTY_EMAIL = "Email cannot be empty.";
    public static final String TOO_LONG_EMAIL = "Email length is too long. Email cannot contain more than 60 characters.";
    public static final String WRONG_PASSWORD =
                    "Password must contain at least 8 characters. " +
                    "At least one number and one special character.";
    public static final String WRONG_NAME = "Name must contain only letters and cannot be too long.";
    public static final String WRONG_SURNAME = "Surname must contain only letters and cannot be too long.";
    public static final String WRONG_PHONE_NUM = "Phone number must contain 8-12 digits.";
    public static final String WRONG_CITY = "City must contain between 2 and 30 letters.";
    public static final String WRONG_MESSAGE_LENGTH = "Message cannot be too long!";
    public static final String WRONG_RATE = "Rate must be value between 1 and 5!";
    public static final String WRONG_KM_NUM = "Kilometers must be greater than 0.";
    public static final String PASSWORDS_DO_NOT_MATCH_MESSAGE = "Passwords don't match. Try again.";
    public static final String UPDATE_ERROR_MESSAGE = "Entity cannot be updated due to server error.";
    public static final String WRONG_SECURITY_CODE = "Security code is number greater than 0.";
    public static final String WRONG_VERIFY_ID = "Verify id must be added.";
    public static final String NO_AVAILABLE_ADMIN_EXC = "Chat cannot be created right now, all out admins are busy.";
    public static final String ADDING_MESSAGE_TO_RESOLVED_CHAT_ROOM = "You cannot add message to resolved chat room.";
    public static final String NOT_NULL_MESSAGE = "Field must not be null.";
    public static final String CHART_TYPE_MESSAGE = "Chart type must be valid.";
    public static final String POSITIVE_OR_ZERO_MESSAGE = "Value must be positive or zero.";
    public static final String POSITIVE_MESSAGE = "Value must be positive.";
    public static final String SEEN_NOT_EXIST = "Seen must exist in message.";
    public static final String MISSING_ID = "Id value cannot be empty.";
    public static final String REASON_TOO_LONG = "Entered reason must contain less than 50 letters.";
    public static final String UNAUTHORIZED_MESSAGE = "You are not authorized to perform this action.";
    public static final String ACTIVITY_STATUS_CANNOT_BE_CHANGED_MESSAGE = "Activity status cannot be changed, you worked 8 hours.";
    public static final String ACTIVE_DRIVING_IN_PROGRESS_MESSAGE = "You cannot change status to not active, you have active driving in progress.";
    public static final String UNBLOCK_UNBLOCKED_USER_MESSAGE = "Cannot unblock user that is not blocked.";
    public static final String MISSING_NUM_OF_TOKENS = "Number of tokens must be greater than 0.";
    public static final String MAX_NUM_OF_TOKENS = "Max num of tokens must be greater than 0.";
    public static final String PAYPAL_PAYMENT_EXCEPTION = "Payment cannot be realized, something went wrong.";
    public static final String DRIVER_ALREADY_HAS_STARTED_DRIVING_EXCEPTION = "Driver already has started driving.";
    public static final String INVALID_CHOSEN_TIME_AFTER_FOR_RESERVATION_MESSAGE = "You can only schedule your ride for later 5 hours in advance.";
    public static final String DRIVING_SHOULD_NOT_START_YET = String.format("The max number of minutes before driving can start is %d.", FIVE_MINUTES);
    public static final String INVALID_CHOSEN_TIME_BEFORE_FOR_RESERVATION_MESSAGE = "You can only schedule your ride for later 30 minutes after this time.";
}
