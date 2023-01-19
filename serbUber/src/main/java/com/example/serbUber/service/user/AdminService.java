package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.AdminDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.user.Admin;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.repository.user.AdminRepository;
import com.example.serbUber.service.DriverUpdateApprovalService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.user.AdminDTO.fromAdmins;
import static com.example.serbUber.util.Constants.ROLE_ADMIN;
import static com.example.serbUber.util.Constants.getProfilePicture;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;

@Component
@Qualifier("adminServiceConfiguration")
public class AdminService {

    private final AdminRepository adminRepository;
    private final RegularUserService regularUserService;
    private final DriverService driverService;
    private final DriverUpdateApprovalService driverUpdateApprovalService;

    public AdminService(
        final AdminRepository adminRepository,
        final RegularUserService regularUserService,
        final DriverService driverService,
        final DriverUpdateApprovalService driverUpdateApprovalService
    ) {
        this.adminRepository = adminRepository;
        this.regularUserService = regularUserService;
        this.driverService = driverService;
        this.driverUpdateApprovalService = driverUpdateApprovalService;
    }

    public List<AdminDTO> getAll() {
        List<Admin> admins = adminRepository.findAll();

        return fromAdmins(admins);
    }

    public AdminDTO get(Long id) throws EntityNotFoundException {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);

        return optionalAdmin.map(AdminDTO::new)
            .orElseThrow(() ->  new EntityNotFoundException(id, EntityType.USER));
    }

    public AdminDTO create(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture
    ) {
        String hashedPassword = getHashedNewUserPassword(password);
        Admin admin = adminRepository.save(new Admin(
            email,
            hashedPassword,
            name,
            surname,
            phoneNumber,
            city,
            getProfilePicture(profilePicture),
            new Role(ROLE_ADMIN)
        ));

        return new AdminDTO(admin);
    }

}
