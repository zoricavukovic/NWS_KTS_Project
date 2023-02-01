package com.example.serbUber.selenium.helper;

import java.time.LocalDateTime;

public class Util {

    public static String getHour(int hours, int minutes) {

        return String.valueOf(LocalDateTime.now().plusHours(hours).plusMinutes(minutes).getHour());
    }

    public static String getMinutes(int hours, int minutes) {

        return String.valueOf(LocalDateTime.now().plusHours(hours).plusMinutes(minutes).getMinute());
    }

}
