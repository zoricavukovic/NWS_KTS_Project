package com.example.serbUber.controller.user;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.PasswordsDoNotMatchException;
import com.example.serbUber.exception.UsersUpdateException;
import com.example.serbUber.request.user.*;
import com.example.serbUber.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAll() {

        return userService.getAll();
    }

    @GetMapping("/byEmail")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO get(
        @Valid @RequestBody UserEmailRequest userEmailRequest
    ) throws EntityNotFoundException {

        return userService.get(userEmailRequest.getEmail());
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@Valid @RequestBody UsersProfileUpdateRequest userData)
            throws EntityNotFoundException, UsersUpdateException {

        return userService.update(
                userData.getEmail(),
                userData.getName(),
                userData.getSurname(),
                userData.getPhoneNumber(),
                userData.getCity()
        );
    }

    @PutMapping("profile-picture")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@Valid @RequestBody UserProfilePictureRequest userData)
            throws EntityNotFoundException, UsersUpdateException {

        return userService.updateProfilePicture(
                userData.getEmail(),
                userData.getProfilePicture()
        );
    }

    @PutMapping("password")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@Valid @RequestBody UserPasswordUpdateRequest userData)
            throws EntityNotFoundException, PasswordsDoNotMatchException {

        return userService.updatePassword(
                userData.getEmail(),
                userData.getCurrentPassword(),
                userData.getNewPassword(),
                userData.getConfirmPassword()
        );
    }

    @PutMapping("reset-password")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest)
        throws EntityNotFoundException, PasswordsDoNotMatchException {

        return userService.resetPassword(
            passwordResetRequest.getEmail(),
            passwordResetRequest.getNewPassword(),
            passwordResetRequest.getConfirmPassword()
        );
    }

    @GetMapping("/send-rest-password-link/{email}")
    @ResponseStatus(HttpStatus.OK)
    public boolean sendResetPasswordLink(@Valid @PathVariable("email") UserEmailRequest userEmailRequest
    ) throws EntityNotFoundException {

        return userService.sendEmailForResetPassword(userEmailRequest.getEmail());
    }
}
