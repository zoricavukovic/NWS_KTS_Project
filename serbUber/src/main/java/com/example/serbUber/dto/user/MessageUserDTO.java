package com.example.serbUber.dto.user;

import com.example.serbUber.model.user.Role;
import com.example.serbUber.model.user.User;

public class MessageUserDTO {

    private final String email;
    private final String name;
    private final String surname;
    private Role role;

    public MessageUserDTO(
            final String email,
            final String name,
            final String surname,
            final Role role
    ) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public MessageUserDTO(final User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.role = user.getRole();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Role getRole() {
        return role;
    }
}
