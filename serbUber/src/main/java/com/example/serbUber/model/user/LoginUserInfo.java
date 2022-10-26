package com.example.serbUber.model.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "loginUsersInfo")
public class LoginUserInfo {

    @Id
    private String id;

    private String email;
    private String password;
    private Role role;

    public LoginUserInfo(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
