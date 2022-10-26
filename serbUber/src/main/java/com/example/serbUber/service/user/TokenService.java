package com.example.serbUber.service.user;

import com.auth0.jwt.JWT;
import com.example.serbUber.dto.user.JwtLogin;
import com.example.serbUber.dto.user.LoginDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.model.user.UserPrinciple;
import com.example.serbUber.util.JwtProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Service
public class TokenService {
    private final AuthenticationManager authenticationManager;

    public TokenService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private String generateToken(final Authentication authResult) {

        UserPrinciple principal = (UserPrinciple) authResult.getPrincipal();

        return JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .sign(HMAC512(JwtProperties.SECRET.getBytes()));
    }

    public LoginDTO login(final JwtLogin jwtLogin) {
        Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(jwtLogin.email(), jwtLogin.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        final String token = generateToken(authenticate);
        UserPrinciple userPrinciple = (UserPrinciple) authenticate.getPrincipal();
        UserDTO userDTO = userPrinciple.getUser();
        userDTO.setRole(userPrinciple.getUser().getRole());

        return new LoginDTO(token, userDTO);
    }

}
