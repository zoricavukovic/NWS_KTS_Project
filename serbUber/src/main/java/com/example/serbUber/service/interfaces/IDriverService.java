package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.user.Driver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IDriverService {
    List<DriverDTO> getAll();
    DriverDTO get(final Long id) throws EntityNotFoundException;
    Driver getDriverById(final Long id) throws EntityNotFoundException;
    UserDTO create(
            final String email,
            final String password,
            final String confirmPassword,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture,
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleType vehicleType
    ) throws PasswordsDoNotMatchException, EntityNotFoundException, EntityAlreadyExistsException, MailCannotBeSentException;
    Driver updateRate(final Long id, final double rate) throws EntityNotFoundException;
    Double getDriverRating(final Long id);
    UserDTO activate(final Long verifyId, final int securityCode)  throws EntityNotFoundException, WrongVerifyTryException;
}