package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.AdminDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.user.Admin;
import com.example.serbUber.model.user.LoginUserInfo;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.repository.user.AdminRepository;
import com.example.serbUber.repository.user.LoginUserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.user.AdminDTO.fromAdmins;
import static com.example.serbUber.util.Constants.getProfilePicture;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final LoginUserInfoRepository loginUserInfoRepository;

    public AdminService(
        final AdminRepository adminRepository,
        final LoginUserInfoRepository loginUserInfoRepository
    ) {
        this.adminRepository = adminRepository;
        this.loginUserInfoRepository = loginUserInfoRepository;
    }

    public List<AdminDTO> getAll() {
        List<Admin> admins = adminRepository.findAll();

        return fromAdmins(admins);
    }

    public AdminDTO get(String email) throws EntityNotFoundException {
        Optional<Admin> optionalAdmin = adminRepository.getAdminByEmail(email);

        return optionalAdmin.map(AdminDTO::new)
            .orElseThrow(() ->  new EntityNotFoundException(email, EntityType.USER));
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
            getProfilePicture(profilePicture)
        ));
        loginUserInfoRepository.save(new LoginUserInfo(email, hashedPassword, new Role("ROLE_ADMIN")));

        return new AdminDTO(admin);
    }

}
