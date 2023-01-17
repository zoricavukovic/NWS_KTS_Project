package com.example.serbUber.exception;

import static com.example.serbUber.exception.ErrorMessagesConstants.CHART_TYPE_MESSAGE;

public class ChartException  extends AppException {

    public ChartException() {
        super(CHART_TYPE_MESSAGE);
    }

}
