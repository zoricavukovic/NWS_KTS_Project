package com.example.serbUber.service.interfaces;

import com.example.serbUber.exception.EntityUpdateException;
import com.example.serbUber.model.user.DriverUpdateApproval;

public interface IDriverUpdateApprovalService {

    DriverUpdateApproval save(
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city
    ) throws EntityUpdateException;
}
