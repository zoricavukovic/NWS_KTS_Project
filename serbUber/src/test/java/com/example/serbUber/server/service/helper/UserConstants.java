package com.example.serbUber.server.service.helper;

import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.model.user.User;

public class UserConstants {
    public static final String USER_NAME = "User";
    public static final String USER_SURNAME = "User";
    public static final String USER_EMAIL_1 = "user1@gmail.com";

    public static final Long USER_ID_1 = 1L;

    public static User createUser(Long id, String email){
        return new RegularUser(id, email, Constants.EXIST_PASSWORD, USER_NAME, USER_SURNAME, Constants.PHONE_NUMBER, Constants.CITY, Constants.PROFILE_PICTURE, new Role("ROLE_REGULAR_USER"));
    }
}
