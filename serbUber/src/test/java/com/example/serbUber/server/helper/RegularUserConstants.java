package com.example.serbUber.server.helper;

import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.Role;

public class RegularUserConstants {
    public static final String NOT_EXIST_USER_EMAIL = "not_found@gmail.com";

    public static final String FIRST_USER_EMAIL = "john@gmail.com";
    public static final String FIRST_USER_NAME = "John";
    public static final String FIRST_USER_SURNAME = "Snow";

    public static final String SECOND_USER_EMAIL = "deny@gmail.com";
    public static final String SECOND_USER_NAME = "Daenerys";
    public static final String SECOND_USER_SURNAME = "Targaryen";

    public static final String USER_PHONE_NUMBER = "111111111";
    public static final String USER_CITY = "Novi Sad";
    public static final String USER_PASSWORD = "sifra123@";
    public static final String USER_PROFILE_PICTURE = "default";
    public static final Role ROLE_REGULAR_USER = new Role("ROLE_REGULAR_USER");

    public static final RegularUser FIRST_USER = new RegularUser(FIRST_USER_EMAIL, USER_PASSWORD, FIRST_USER_NAME, FIRST_USER_SURNAME, USER_PHONE_NUMBER, USER_CITY, USER_PROFILE_PICTURE, ROLE_REGULAR_USER);
    public static final RegularUser SECOND_USER = new RegularUser(SECOND_USER_EMAIL, USER_PASSWORD, SECOND_USER_NAME, SECOND_USER_SURNAME, USER_PHONE_NUMBER, USER_CITY, USER_PROFILE_PICTURE, ROLE_REGULAR_USER);


}
