package com.example.serbUber.service;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityUpdateException;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.user.DriverUpdateApproval;
import com.example.serbUber.repository.user.DriverUpdateApprovalRepository;
import com.example.serbUber.service.interfaces.IDriverUpdateApprovalService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Qualifier("driverUpdateApprovalServiceConfiguration")
public class DriverUpdateApprovalService implements IDriverUpdateApprovalService {

    private final DriverUpdateApprovalRepository driverUpdateApprovalRepository;

    public DriverUpdateApprovalService(
            final DriverUpdateApprovalRepository driverUpdateApprovalRepository
    ) {
        this.driverUpdateApprovalRepository = driverUpdateApprovalRepository;
    }

    public List<UserDTO> getAllNotApproved() {

        return driverUpdateApprovalRepository.getAllNotApproved();
    }

    public DriverUpdateApproval save(
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final VehicleType vehicleType,
            final boolean petFriendly,
            final boolean babySeat
    ) throws EntityUpdateException {
        try {

            return driverUpdateApprovalRepository.save(
                new DriverUpdateApproval(email, name, surname, phoneNumber, city, vehicleType, petFriendly, babySeat));
        }catch (IllegalArgumentException  ex){

            throw new EntityUpdateException();
        }
    }


}
