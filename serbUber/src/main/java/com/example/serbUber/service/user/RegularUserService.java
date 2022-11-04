package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.Verify;
import com.example.serbUber.model.user.LoginUserInfo;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.repository.user.LoginUserInfoRepository;
import com.example.serbUber.repository.user.RegularUserRepository;
import com.example.serbUber.service.VerifyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.user.RegularUserDTO.fromRegularUsers;
import static com.example.serbUber.model.user.User.passwordsMatch;
import static com.example.serbUber.util.Constants.getProfilePicture;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;

@Service
public class RegularUserService {

    private final RegularUserRepository regularUserRepository;

    private final LoginUserInfoRepository loginUserInfoRepository;

    private final VerifyService verifyService;

    public RegularUserService(
            final RegularUserRepository regularUserRepository,
            final LoginUserInfoRepository loginUserInfoRepository,
            final VerifyService verifyService
    ) {
        this.regularUserRepository = regularUserRepository;
        this.loginUserInfoRepository = loginUserInfoRepository;
        this.verifyService = verifyService;
    }

    public List<RegularUserDTO> getAll() {
        List<RegularUser> regularUsers = regularUserRepository.findAll();

        return fromRegularUsers(regularUsers);
    }

    public RegularUserDTO get(String email) throws EntityNotFoundException {
        Optional<RegularUser> optionalRegularUser = regularUserRepository.getRegularUserByEmail(email);

        return optionalRegularUser.map(RegularUserDTO::new)
            .orElseThrow(() ->  new EntityNotFoundException(email, EntityType.USER));
    }

    public RegularUser getRegularById(Long id) throws EntityNotFoundException {
        Optional<RegularUser> optionalRegularUser = regularUserRepository.findById(id);

        return optionalRegularUser.map(RegularUser::new)
                .orElseThrow(() ->  new EntityNotFoundException(id, EntityType.USER));
    }

    public RegularUser getRegularByEmail(String email) throws EntityNotFoundException {
        Optional<RegularUser> optionalRegularUser = regularUserRepository.getRegularUserByEmail(email);

        return optionalRegularUser.map(RegularUser::new)
             .orElseThrow(() ->  new EntityNotFoundException(email, EntityType.USER));
    }

    public RegularUserDTO create(
        final String email,
        final String password,
        final String confirmationPassword,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture
    ) throws PasswordsDoNotMatchException, EntityAlreadyExistsException, MailCannotBeSentException {

        if (!passwordsMatch(password, confirmationPassword)) {
            throw new PasswordsDoNotMatchException();
        }
        RegularUser regularUser = saveRegularUser(email, password, name, surname, phoneNumber, city, profilePicture);

        return new RegularUserDTO(regularUser);
    }

    private RegularUser saveRegularUser(
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture
    ) throws MailCannotBeSentException, EntityAlreadyExistsException {
        try {
            String hashedPassword = getHashedNewUserPassword(password);
            RegularUser regularUser = regularUserRepository.save(new RegularUser(
                    email,
                    hashedPassword,
                    name,
                    surname,
                    phoneNumber,
                    city,
                    profilePicture
            ));
            verifyService.sendEmail(regularUser.getId(), regularUser.getEmail());
            LoginUserInfo loginUserInfo = new LoginUserInfo(email, hashedPassword, new Role("ROLE_REGULAR_USER"));
            loginUserInfoRepository.save(loginUserInfo);

            return regularUser;
        } catch (MailCannotBeSentException e) {
            throw new MailCannotBeSentException(e.getMessage());
        } catch (Exception e) {
            throw new EntityAlreadyExistsException(String.format("User with %s already exists.", email));
        }
    }

    public void activate(final Long verifyId, final int securityCode)
            throws EntityNotFoundException, WrongVerifyTryException
    {
        try {
            Verify verify = verifyService.update(verifyId, securityCode);
            RegularUser regularUser = getRegularById(verify.getUserId());
            regularUser.setVerified(true);
            regularUserRepository.save(regularUser);
        } catch (WrongVerifyTryException e) {
            throw new WrongVerifyTryException(e.getMessage());
        }
    }

}
