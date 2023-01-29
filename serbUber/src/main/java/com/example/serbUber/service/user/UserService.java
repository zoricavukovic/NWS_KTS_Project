package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.RegistrationDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.Verify;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.user.UserRepository;
import com.example.serbUber.service.DriverUpdateApprovalService;
import com.example.serbUber.service.EmailService;
import com.example.serbUber.service.VerifyService;
import com.example.serbUber.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.user.UserDTO.fromUsers;
import static com.example.serbUber.model.user.User.passwordsDontMatch;
import static com.example.serbUber.util.Constants.ROLE_ADMIN;
import static com.example.serbUber.util.EmailConstants.FRONT_RESET_PASSWORD_URL;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;
import static com.example.serbUber.util.JwtProperties.oldPasswordsMatch;
import static com.example.serbUber.util.PictureHandler.checkPictureValidity;
import static com.example.serbUber.util.PictureHandler.convertPictureToBase64ByName;

@Component
@Qualifier("userServiceConfiguration")
public class UserService implements IUserService {

    private UserRepository userRepository;
    private DriverUpdateApprovalService driverUpdateApprovalService;
    private EmailService emailService;
    private DriverService driverService;
    private RegularUserService regularUserService;
    private VerifyService verifyService;

    @Autowired
    public UserService(
        final UserRepository userRepository,
        final DriverUpdateApprovalService driverUpdateApprovalService,
        final EmailService emailService,
        final DriverService driverService,
        final RegularUserService regularUserService,
        final VerifyService verifyService
    ) {
        this.userRepository = userRepository;
        this.driverUpdateApprovalService = driverUpdateApprovalService;
        this.emailService = emailService;
        this.driverService = driverService;
        this.regularUserService = regularUserService;
        this.verifyService = verifyService;
    }

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();

        return fromUsers(users);
    }

    public List<UserDTO> getAllVerified() {
        List<User> users = userRepository.getAllVerified();

        return fromUsers(users);
    }

    public User getUserByEmail(final String email) throws EntityNotFoundException {

        return userRepository.getUserByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException(email, EntityType.USER));
    }

    public User getVerifiedUser(final String email) throws EntityNotFoundException {

        return userRepository.getVerifiedUser(email)
            .orElseThrow(() -> new EntityNotFoundException(email, EntityType.USER));
    }

    public Driver getDriverById(final long id) throws EntityNotFoundException {

        Object user = userRepository.getDriverById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.USER));

        return (Driver) user;

    }

    public UserDTO getUserDTOByEmail(String email) throws EntityNotFoundException {

        return new UserDTO(getUserByEmail(email));
    }

    public boolean checkIfUserAlreadyExists(String email) {
        Optional<User> user = userRepository.getUserByEmail(email);

        return user.isPresent();
    }

    public User getUserById(Long id) throws EntityNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, EntityType.USER));
    }

    public UserDTO get(Long id) throws EntityNotFoundException {
        UserDTO userDTO = new UserDTO(getUserById(id));
        userDTO.setProfilePicture(convertPictureToBase64ByName(userDTO.getProfilePicture()));

        return userDTO;
    }

    public UserDTO updateDriver(
            final User user,
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final VehicleType vehicleType,
            final boolean petFriendly,
            final boolean babySeat
    ) throws EntityUpdateException {
        if (!checkDriverApprovalData(vehicleType)) {
            throw new EntityUpdateException("Vehicle type is not valid.");
        }

        driverUpdateApprovalService.save(email, name, surname, phoneNumber, city, vehicleType, petFriendly, babySeat);
        UserDTO userDTO = new UserDTO(user);
        userDTO.setProfilePicture(userDTO.getProfilePicture());

        return userDTO;
    }

    public UserDTO updateRegularOrAdmin(
            final User user,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city
    ){
        user.setName(name);
        user.setSurname(surname);
        user.setPhoneNumber(phoneNumber);
        user.setCity(city);
        userRepository.save(user);
        UserDTO userDTO = new UserDTO(user);
        userDTO.setProfilePicture(userDTO.getProfilePicture());

        return userDTO;
    }

    public UserDTO update(
            final String email,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final VehicleType vehicleType,
            final boolean petFriendly,
            final boolean babySeat
    ) throws EntityNotFoundException, EntityUpdateException {
        User user = getUserByEmail(email);

        return user.getRole().isDriver() ?
                updateDriver(user, email, name, surname, phoneNumber, city, vehicleType, petFriendly, babySeat) :
                updateRegularOrAdmin(user, name, surname, phoneNumber, city);
    }

    public UserDTO updateProfilePicture(final String email, final String profilePicture)
        throws EntityUpdateException
    {
        try{
            User user = getUserByEmail(email);
            String newPictureName = checkPictureValidity(profilePicture, user.getId());
            user.setProfilePicture(newPictureName);
            userRepository.save(user);

            return new UserDTO(user);
        } catch (Exception e) {
            throw new EntityUpdateException(e.getMessage());
        }
    }

    public UserDTO updatePassword(
            final String email,
            final String currentPassword,
            final String newPassword,
            final String confirmPassword
    ) throws PasswordsDoNotMatchException, EntityNotFoundException {
        User user = getUserByEmail(email);
        checkPasswordCondition(currentPassword, newPassword, confirmPassword, user);

        updateAndSavePassword(newPassword, user);

        return new UserDTO(user);
    }

    public boolean sendEmailForResetPassword(String email) throws EntityNotFoundException {
        User user = getUserByEmail(email);
        emailService.sendResetPasswordMail(user.getEmail(), String.format("%s%s", FRONT_RESET_PASSWORD_URL, email));

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



    public UserDTO setOnlineStatus(final String email) throws EntityNotFoundException {
        User user = getUserByEmail(email);
        user.setOnline(true);

        return new UserDTO((user.getRole().isDriver()) ? driverService.onDriverLogin(user.getId()) :
                userRepository.save(user));
    }

    public UserDTO setOfflineStatus(final String email)
            throws EntityNotFoundException, ActivityStatusCannotBeChangedException
    {
        User user = getUserByEmail(email);
        user.setOnline(false);

        return new UserDTO(user.getRole().isDriver() ? driverService.onDriverLogout(user.getId()) :
                userRepository.save(user));
    }

    public User findOnlineAdmin() throws NoAvailableAdminException {
        List<User> admins = userRepository.findOnlineAdmin();
        if (admins.size() == 0) {
            throw  new NoAvailableAdminException();
        }

        return admins.get(0);
    }

    public User findAdminForReportHandling() {
        List<User> admins = this.userRepository.findOnlineAdmin();
        if (admins.size() == 0) {
            admins = this.userRepository.getAdmin();
        }

        return admins.size() > 0 ? admins.get(0) : null;
    }

    public UserDTO saveUser(User user) {

        return new UserDTO(this.userRepository.save(user));
    }

    public boolean activate(final Long verifyId, final int securityCode)
            throws EntityNotFoundException, WrongVerifyTryException {
        Verify verify = verifyService.update(verifyId, securityCode);
        User user = this.getUserById(verify.getUserId());
        user.setVerified(true);
        this.saveUser(user);

        return true;
    }

    public RegistrationDTO createRegularUser(
        final String email,
        final String password,
        final String confirmationPassword,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture
    ) throws PasswordsDoNotMatchException, EntityAlreadyExistsException, MailCannotBeSentException, EntityNotFoundException {
        if (passwordsDontMatch(password, confirmationPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if (checkIfUserAlreadyExists(email)) {
            throw new EntityAlreadyExistsException(String.format("User with %s already exists.", email));
        }

        return regularUserService.registerRegularUser(email, password, name, surname, phoneNumber, city, profilePicture);
    }

    public UserDTO createDriver(
            final String email,
            final String password,
            final String confirmPassword,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture,
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleType vehicleType
    ) throws PasswordsDoNotMatchException, EntityNotFoundException, EntityAlreadyExistsException, MailCannotBeSentException {
        if (passwordsDontMatch(password, confirmPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if (this.checkIfUserAlreadyExists(email)) {
            throw new EntityAlreadyExistsException(String.format("User with %s already exists.", email));
        }

        return driverService.create(email, password, confirmPassword, name, surname, phoneNumber,
                city, profilePicture, petFriendly, babySeat, vehicleType);
    }

    public boolean block(final Long id, final String reason)
            throws EntityNotFoundException, EntityUpdateException
    {
        User user = getUserById(id);
        if (user.getRole().isAdmin()) {
            throw new EntityUpdateException("Admin cannot be blocked.");
        }

        return (user.getRole().isDriver()) ? driverService.blockDriver(id, reason)
                : regularUserService.blockRegular(id, reason);
    }

    private boolean checkDriverApprovalData(final VehicleType vehicleType) {

        return vehicleType != null;
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
