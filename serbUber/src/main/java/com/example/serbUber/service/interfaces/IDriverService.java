package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.user.DriverDTO;
import com.example.serbUber.dto.user.DriverPageDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.DriverUpdateApproval;
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
    DriverDTO updateActivityStatus(final Long id, boolean active)
            throws EntityNotFoundException, ActivityStatusCannotBeChangedException;
    boolean blockDriver(final Long id, final String reason)
            throws EntityNotFoundException, EntityUpdateException;
    boolean getIsBlocked(Long id);
    List<DriverPageDTO> getDriversWithPagination(int pageNumber, int pageSize);

    boolean approveDriverChanges(final DriverUpdateApproval driverUpdateApproval) throws EntityNotFoundException;
    Driver getDriverByEmail(final String email) throws EntityNotFoundException;

    List<Driver> getActiveDrivers();
}
