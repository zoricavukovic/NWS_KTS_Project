package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.LoginUserInfoDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.PasswordsDoNotMatchException;
import com.example.serbUber.exception.UsersUpdateException;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.user.UserDTO.fromUsers;
import static com.example.serbUber.model.user.User.passwordsMatch;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;
import static com.example.serbUber.util.JwtProperties.oldPasswordsMatch;
import static com.example.serbUber.util.PictureHandler.checkPictureValidity;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoginUserInfoService loginUserInfoService;
    private final AdminService adminService;
    private final DriverService driverService;
    private final RegularUserService regularUserService;

    public UserService(
        final UserRepository userRepository,
        final LoginUserInfoService loginUserInfoService,
        final AdminService adminService,
        final DriverService driverService,
        final RegularUserService regularUserService
    ) {
        this.userRepository = userRepository;
        this.loginUserInfoService = loginUserInfoService;
        this.adminService = adminService;
        this.driverService = driverService;
        this.regularUserService = regularUserService;
    }

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();

        return fromUsers(users);
    }

    public User getUserByEmail(String email) throws EntityNotFoundException {
        Optional<User> optionalInfo = userRepository.getUserByEmail(email);

        if (optionalInfo.isPresent()){
            return optionalInfo.get();
        } else {
            throw new EntityNotFoundException(email, EntityType.USER);
        }
    }

    public UserDTO get(String email) throws EntityNotFoundException {
        LoginUserInfoDTO loginUserInfoDTO = loginUserInfoService.get(email);

        return switch (loginUserInfoDTO.getRole().getName()) {
            case "ROLE_ADMIN" -> adminService.get(email);
            case "ROLE_DRIVER" -> driverService.get(email);
            default -> regularUserService.get(email);
        };

    }

    public UserDTO update(
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city
    ) throws EntityNotFoundException, UsersUpdateException {
        try {
            User user = getUserByEmail(email);
            user.setName(name);
            user.setSurname(surname);
            user.setPhoneNumber(phoneNumber);
            user.setCity(city);
            userRepository.save(user);

            return new UserDTO(user);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(email, EntityType.USER);
        } catch (Exception e) {
            throw new UsersUpdateException();
        }
    }

    public UserDTO updateProfilePicture(final String email, final String profilePicture)
            throws UsersUpdateException, EntityNotFoundException {
        try {
            User user = getUserByEmail(email);
            String newPictureName = checkPictureValidity(profilePicture, user.getId());
            user.setProfilePicture(newPictureName);
            userRepository.save(user);

            return new UserDTO(user);
        }  catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(email, EntityType.USER);
        } catch (Exception e) {
            throw new UsersUpdateException(e.getMessage());
        }
    }

    public UserDTO updatePassword(
            final String email,
            final String currentPassword,
            final String newPassword,
            final String confirmPassword)
            throws EntityNotFoundException, PasswordsDoNotMatchException
    {
        User user = getUserByEmail(email);
        if (!passwordsMatch(newPassword, confirmPassword) ) {
            throw new PasswordsDoNotMatchException("New password and confirm password are not the same.");
        } else if (!oldPasswordsMatch(currentPassword, user.getPassword())) {
            throw new PasswordsDoNotMatchException("Your old password is not correct.");
        }
        user.setPassword(getHashedNewUserPassword(newPassword));
        userRepository.save(user);

        return new UserDTO(user);
    }
}
