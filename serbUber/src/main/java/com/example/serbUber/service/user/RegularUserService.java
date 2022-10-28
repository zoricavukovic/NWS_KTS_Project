package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.PasswordsDoNotMatchException;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.user.LoginUserInfo;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.repository.user.LoginUserInfoRepository;
import com.example.serbUber.repository.user.RegularUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.user.RegularUserDTO.fromRegularUsers;
import static com.example.serbUber.model.user.User.passwordsMatch;

@Service
public class RegularUserService {

    private final RegularUserRepository regularUserRepository;
    private final LoginUserInfoRepository loginUserInfoRepository;

    public RegularUserService(RegularUserRepository regularUserRepository, LoginUserInfoRepository loginUserInfoRepository) {
        this.regularUserRepository = regularUserRepository;
        this.loginUserInfoRepository = loginUserInfoRepository;
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

    public RegularUserDTO create(
        final String email,
        final String password,
        final String confirmationPassword,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture
    ) throws PasswordsDoNotMatchException {

        if (passwordsMatch(password, confirmationPassword)) {
            RegularUser regularUser = regularUserRepository.save(new RegularUser(
                email,
                password,
                name,
                surname,
                phoneNumber,
                city,
                profilePicture
            ));

            LoginUserInfo loginUserInfo = new LoginUserInfo(email, password, new Role("regularUser"));
            loginUserInfoRepository.save(loginUserInfo);

            return new RegularUserDTO(regularUser);
        } else {
            throw new PasswordsDoNotMatchException();
        }
    }
}
