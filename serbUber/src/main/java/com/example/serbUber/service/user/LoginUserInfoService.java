package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.LoginUserInfoDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.user.LoginUserInfo;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.user.LoginUserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginUserInfoService {

    private final LoginUserInfoRepository loginUserInfoRepository;

    public LoginUserInfoService(final LoginUserInfoRepository loginUserInfoRepository) {
        this.loginUserInfoRepository = loginUserInfoRepository;
    }

    public LoginUserInfoDTO get(String email) throws EntityNotFoundException {
        Optional<LoginUserInfo> optionalInfo = loginUserInfoRepository.findByEmail(email);

        return optionalInfo.map(LoginUserInfoDTO::new)
            .orElseThrow(() ->  new EntityNotFoundException(email, EntityType.USER));
    }
}
