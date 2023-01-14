package com.example.serbUber.exception;

import static com.example.serbUber.exception.ErrorMessagesConstants.INVALID_STARTED_DATE_TIME_EXCEPTION;
public class InvalidStartedDateTimeException extends AppException{
    public InvalidStartedDateTimeException(){
        super(INVALID_STARTED_DATE_TIME_EXCEPTION);
    }
}
