package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityUpdateException;
import com.example.serbUber.exception.NoAvailableAdminException;
import com.example.serbUber.exception.PasswordsDoNotMatchException;
import com.example.serbUber.model.user.User;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAll();
    User getUserByEmail(String email) throws EntityNotFoundException;
    User getUserById(Long id) throws EntityNotFoundException;
    UserDTO get(Long id) throws EntityNotFoundException;
    UserDTO updateDriver(
            final User user,
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city
    ) throws EntityUpdateException;
    UserDTO updateRegularOrAdmin(
            final User user,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city
    );
    UserDTO update(
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city
    ) throws EntityNotFoundException, EntityUpdateException;
    UserDTO updateProfilePicture(final String email, final String profilePicture)
            throws EntityUpdateException;
    UserDTO updatePassword(
            final String email,
            final String currentPassword,
            final String newPassword,
            final String confirmPassword
    ) throws PasswordsDoNotMatchException, EntityNotFoundException;
    boolean sendEmailForResetPassword(String email) throws EntityNotFoundException;
    UserDTO resetPassword(String email, String newPassword, String confirmPassword)
            throws EntityNotFoundException, PasswordsDoNotMatchException;
    User findFirstAdmin() throws EntityNotFoundException;
    UserDTO setOnlineStatus(final String email) throws EntityNotFoundException;
    UserDTO setOfflineStatus(final String email) throws EntityNotFoundException;
    User findOnlineAdmin() throws NoAvailableAdminException;
    boolean checkIfUserAlreadyExists(String email);

}
