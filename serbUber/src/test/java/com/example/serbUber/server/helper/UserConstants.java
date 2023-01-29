package com.example.serbUber.server.helper;

import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.model.user.User;

import static com.example.serbUber.server.helper.Constants.*;

public class UserConstants {
//    final String email,
//    final String password,
//    final String name,
//    final String surname,
//    final String phoneNumber,
//    final String city,
//    final String profilePicture,
//    final Role role
    public static final String USER_NAME = "User";
    public static final String USER_SURNAME = "User";
    public static final String USER_EMAIL_1 = "user1@gmail.com";
    public static final String USER_EMAIL_2 = "user2@gmail.com";
    public static final String USER_EMAIL_3 = "user3@gmail.com";

    public static final Long USER_ID_1 = 1L;
    public static final Long USER_ID_2 = 2L;
    public static final Long USER_ID_3 = 3L;
    public static User createUser(Long id, String email){
        return new RegularUser(
                id,
                email,
                EXIST_PASSWORD,
                USER_NAME,
                USER_SURNAME,
                PHONE_NUMBER,
                CITY,
                PROFILE_PICTURE,
                new Role("ROLE_REGULAR_USER")
        );
    }
}
