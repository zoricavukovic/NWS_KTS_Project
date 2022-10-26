package com.example.serbUber.dto.user;

import com.example.serbUber.model.user.LoginUserInfo;
import com.example.serbUber.model.user.Role;

public class LoginUserInfoDTO {
    private String email;
    private String password;
    private Role role;

    public LoginUserInfoDTO(LoginUserInfo loginUserInfo) {
        this.email = loginUserInfo.getEmail();
        this.password = loginUserInfo.getPassword();
        this.role = loginUserInfo.getRole();
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
