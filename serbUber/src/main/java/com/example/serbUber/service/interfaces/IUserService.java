package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.user.RegistrationDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.User;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAll();
    User getUserByEmail(String email) throws EntityNotFoundException;
    User getVerifiedUser(final String email) throws EntityNotFoundException;
    UserDTO getUserDTOByEmail(String email) throws EntityNotFoundException;
    User getUserById(Long id) throws EntityNotFoundException;
    UserDTO get(Long id) throws EntityNotFoundException;
    UserDTO updateDriver(
            final User user,
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final VehicleType vehicleType,
            final boolean petFriendly,
            final boolean babySeat
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
            final String city,
            final VehicleType vehicleType,
            final boolean petFriendly,
            final boolean babySeat
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
    UserDTO setOnlineStatus(final String email) throws EntityNotFoundException;
    UserDTO setOfflineStatus(final String email) throws EntityNotFoundException, ActivityStatusCannotBeChangedException;
    User findOnlineAdmin() throws NoAvailableAdminException;
    boolean checkIfUserAlreadyExists(String email);
    boolean activate(final Long verifyId, final int securityCode)
            throws EntityNotFoundException, WrongVerifyTryException;
    boolean block(final Long id, final String reason)
            throws EntityNotFoundException, EntityUpdateException;

    RegistrationDTO createRegularUser(
        final String email,
        final String password,
        final String confirmPassword,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city, final String profilePicture
    ) throws PasswordsDoNotMatchException, EntityAlreadyExistsException, MailCannotBeSentException, EntityNotFoundException;

    Driver getDriverById(final Long driverId) throws EntityNotFoundException;
    User findAdminForReportHandling();
}
