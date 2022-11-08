package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.PasswordsDoNotMatchException;
import com.example.serbUber.exception.UsersUpdateException;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.user.UserRepository;
import com.example.serbUber.service.DriverUpdateApprovalService;
import com.example.serbUber.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.user.UserDTO.fromUsers;
import static com.example.serbUber.model.user.User.passwordsDontMatch;
import static com.example.serbUber.util.EmailConstants.FRONT_RESET_PASSWORD_URL;
import static com.example.serbUber.util.EmailConstants.RESET_PASSWORD_SUBJECT;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;
import static com.example.serbUber.util.JwtProperties.oldPasswordsMatch;
import static com.example.serbUber.util.PictureHandler.checkPictureValidity;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DriverUpdateApprovalService driverUpdateApprovalService;
    private final EmailService emailService;

    public UserService(
        final UserRepository userRepository,
        final DriverUpdateApprovalService driverUpdateApprovalService,
        final EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.driverUpdateApprovalService = driverUpdateApprovalService;
        this.emailService = emailService;
    }

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();

        return fromUsers(users);
    }

    public User getUserByEmail(String email) throws EntityNotFoundException {
        Optional<User> optionalInfo = userRepository.getUserByEmail(email);

        if (optionalInfo.isPresent()){
            return optionalInfo.get();
        }

        throw new EntityNotFoundException(email, EntityType.USER);
    }

    public UserDTO getUserDTOByEmail(String email) throws EntityNotFoundException {
        return new UserDTO(getUserByEmail(email));
    }

    public UserDTO get(String email) throws EntityNotFoundException {

        return new UserDTO(getUserByEmail(email));
    }

    public UserDTO updateDriver(
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city
    ) throws EntityNotFoundException {
        User user = getUserByEmail(email);
        driverUpdateApprovalService.save(email, name, surname, phoneNumber, city);

        return new UserDTO(user);
    }

    public UserDTO updateRegularOrAdmin(
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city
    ) throws EntityNotFoundException {
        User user = getUserByEmail(email);
        user.setName(name);
        user.setSurname(surname);
        user.setPhoneNumber(phoneNumber);
        user.setCity(city);
        userRepository.save(user);

        return new UserDTO(user);
    }

    public UserDTO update(
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city
    )
            throws EntityNotFoundException, UsersUpdateException
    {
        try {
            User user = getUserByEmail(email);

            return user.getRole().isDriver() ?
                    updateDriver(email, name, surname, phoneNumber, city) :
                    updateRegularOrAdmin(email, name, surname, phoneNumber, city);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(email, EntityType.USER);
        } catch (Exception e) {
            throw new UsersUpdateException();
        }
    }

    public UserDTO updateProfilePicture(final String email, final String profilePicture)
            throws UsersUpdateException, EntityNotFoundException
    {
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
            final String confirmPassword
    ) throws EntityNotFoundException, PasswordsDoNotMatchException {
        User user = getUserByEmail(email);
        checkPasswordCondition(currentPassword, newPassword, confirmPassword, user);

        updateAndSavePassword(newPassword, user);

        return new UserDTO(user);
    }

    public boolean sendEmailForResetPassword(String email) throws EntityNotFoundException {
        User user = getUserByEmail(email);

        emailService.sendMail(user.getEmail(), RESET_PASSWORD_SUBJECT,
            String.format("Click here to reset your password: %s%s",
                FRONT_RESET_PASSWORD_URL, email));
        return true;
    }

    public UserDTO resetPassword(String email, String newPassword, String confirmPassword)
        throws EntityNotFoundException, PasswordsDoNotMatchException
    {
        User user = getUserByEmail(email);
        if (passwordsDontMatch(newPassword, confirmPassword) ) {
            throw new PasswordsDoNotMatchException("New password and confirm password are not the same.");
        }

        updateAndSavePassword(newPassword, user);

        return new UserDTO(user);
    }

    private void updateAndSavePassword(String newPassword, User user) {
        user.setPassword(getHashedNewUserPassword(newPassword));
        userRepository.save(user);
    }

    private void checkPasswordCondition(
        String currentPassword,
        String newPassword,
        String confirmPassword,
        User user
    ) throws PasswordsDoNotMatchException {
        if (passwordsDontMatch(newPassword, confirmPassword)) {
            throw new PasswordsDoNotMatchException("New and confirm password aren't same.");
        }
        if (!oldPasswordsMatch(currentPassword, user.getPassword())) {
            throw new PasswordsDoNotMatchException("Your old password is not correct.");
        }
    }
}
