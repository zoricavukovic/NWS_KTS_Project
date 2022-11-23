package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.user.JwtLogin;
import com.example.serbUber.dto.user.LoginDTO;
import com.example.serbUber.dto.user.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface ITokenService {
    LoginDTO googleLogin(UserDTO userDTO);
    LoginDTO login(final JwtLogin jwtLogin);
}
