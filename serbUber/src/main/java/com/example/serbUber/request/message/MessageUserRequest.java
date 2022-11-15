package com.example.serbUber.request.message;

import com.example.serbUber.model.user.Role;
import com.example.serbUber.util.Constants;

import javax.validation.constraints.*;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

public class MessageUserRequest {

    @Email(message = WRONG_EMAIL)
    @NotBlank(message = EMPTY_EMAIL)
    @Size(max = 60, message = TOO_LONG_EMAIL)
    private final String email;

    @NotBlank(message = WRONG_NAME)
    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = WRONG_NAME)
    private final String name;

    @NotBlank(message = WRONG_SURNAME)
    @Pattern(regexp = Constants.LEGIT_NAME_REG, message = WRONG_SURNAME)
    private final String surname;

    @NotNull(message = "Role cannot be null.")
    private Role role;

    public MessageUserRequest(
            final String email,
            final String name,
            final String surname,
            final Role role) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.role = role;
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
