package com.example.serbUber.controller.user;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityUpdateException;
import com.example.serbUber.exception.PasswordsDoNotMatchException;
import com.example.serbUber.request.user.*;
import com.example.serbUber.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.NOT_NULL_MESSAGE;
import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path="/logout")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    private UserDTO logout(@RequestBody final String email)
            throws EntityNotFoundException
    {

        return userService.setOfflineStatus(email);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAll() {

        return userService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public UserDTO get(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id
    ) throws EntityNotFoundException {

        return userService.get(id);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public UserDTO update(@Valid @RequestBody UsersProfileUpdateRequest userData) throws EntityUpdateException, EntityNotFoundException {

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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public UserDTO update(@Valid @RequestBody UserProfilePictureRequest userData)
        throws EntityUpdateException
    {

        return userService.updateProfilePicture(
                userData.getEmail(),
                userData.getProfilePicture()
        );
    }

    @PutMapping("password")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public UserDTO update(@Valid @RequestBody UserPasswordUpdateRequest userData)
        throws PasswordsDoNotMatchException, EntityNotFoundException {

        return userService.updatePassword(
                userData.getEmail(),
                userData.getCurrentPassword(),
                userData.getNewPassword(),
                userData.getConfirmPassword()
        );
    }

    @PutMapping("reset-password")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public UserDTO resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest)
        throws PasswordsDoNotMatchException, EntityNotFoundException {

        return userService.resetPassword(
            passwordResetRequest.getEmail(),
            passwordResetRequest.getNewPassword(),
            passwordResetRequest.getConfirmPassword()
        );
    }

    @GetMapping("/send-rest-password-link/{email}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public boolean sendResetPasswordLink(@Valid @PathVariable("email") UserEmailRequest userEmailRequest)
        throws EntityNotFoundException {

        return userService.sendEmailForResetPassword(userEmailRequest.getEmail());
    }
}
