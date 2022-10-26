package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.LoginUserInfoDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.serbUber.dto.user.UserDTO.fromUsers;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoginUserInfoService loginUserInfoService;
    private final AdminService adminService;

    public UserService(
        final UserRepository userRepository,
        final LoginUserInfoService loginUserInfoService,
        final AdminService adminService
    ) {
        this.userRepository = userRepository;
        this.loginUserInfoService = loginUserInfoService;
        this.adminService = adminService;
    }

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();

        return fromUsers(users);
    }

    public UserDTO get(String email) throws EntityNotFoundException {
        LoginUserInfoDTO loginUserInfoDTO = loginUserInfoService.get(email);
        if (loginUserInfoDTO.getRole().getName().equals("ROLE_ADMIN")){
            return adminService.get(email);
        }
        return null;
    }
}
