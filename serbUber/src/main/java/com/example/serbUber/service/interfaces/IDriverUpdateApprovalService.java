package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.user.DriverUpdateApprovalDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityUpdateException;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.user.DriverUpdateApproval;

import java.util.List;

public interface IDriverUpdateApprovalService {

    DriverUpdateApproval save(
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final VehicleType vehicleType,
            final boolean petFriendly,
            final boolean babySeat
    ) throws EntityUpdateException;

    List<DriverUpdateApprovalDTO> getAllNotApproved();

    boolean reject(final Long id) throws EntityNotFoundException, EntityUpdateException;
    boolean approve(final Long id) throws EntityNotFoundException;
}
