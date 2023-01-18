package com.example.serbUber.service;

import com.example.serbUber.dto.user.DriverUpdateApprovalDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.EntityUpdateException;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.user.DriverUpdateApproval;
import com.example.serbUber.repository.user.DriverUpdateApprovalRepository;
import com.example.serbUber.service.interfaces.IDriverUpdateApprovalService;
import com.example.serbUber.service.user.DriverService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.serbUber.dto.user.DriverUpdateApprovalDTO.fromApprovals;

@Component
@Qualifier("driverUpdateApprovalServiceConfiguration")
public class DriverUpdateApprovalService implements IDriverUpdateApprovalService {

    private final DriverUpdateApprovalRepository driverUpdateApprovalRepository;
    private final DriverService driverService;

    public DriverUpdateApprovalService(
            final DriverUpdateApprovalRepository driverUpdateApprovalRepository,
            final DriverService driverService
            ) {
        this.driverUpdateApprovalRepository = driverUpdateApprovalRepository;
        this.driverService = driverService;
    }

    public List<DriverUpdateApprovalDTO> getAllNotApproved() {

        return fromApprovals(driverUpdateApprovalRepository.getAllNotApproved());
    }

    public DriverUpdateApproval getById(final Long id) throws EntityNotFoundException {
        return driverUpdateApprovalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVER_UPDATE_APPROVAL));
    }

    public boolean reject(final Long id) throws EntityNotFoundException, EntityUpdateException {
        try {
            driverUpdateApprovalRepository.delete(getById(id));
        } catch (Exception e) {
            throw new EntityUpdateException("Driver Update Approval cannot be deleted");
        }

        return true;
    }

    public boolean approve(final Long id) throws EntityNotFoundException {
        DriverUpdateApproval driverUpdateApproval = getById(id);
        driverUpdateApproval.setApproved(true);
        driverUpdateApprovalRepository.save(driverUpdateApproval);
        this.driverService.approveDriverChanges(driverUpdateApproval);

        return true;
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
