package com.example.serbUber.service;

import com.example.serbUber.model.user.DriverUpdateApproval;
import com.example.serbUber.repository.user.DriverUpdateApprovalRepository;
import org.springframework.stereotype.Service;

@Service
public class DriverUpdateApprovalService {

    private DriverUpdateApprovalRepository driverUpdateApprovalRepository;

    public DriverUpdateApprovalService(
            final DriverUpdateApprovalRepository driverUpdateApprovalRepository
    ) {
        this.driverUpdateApprovalRepository = driverUpdateApprovalRepository;
    }

    public DriverUpdateApproval save(
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city
    ) {
        return driverUpdateApprovalRepository.save(
                new DriverUpdateApproval(email, name, surname, phoneNumber, city));
    }

}
