package com.example.serbUber.controller.user;

import com.example.serbUber.dto.user.JwtLogin;
import com.example.serbUber.dto.user.LoginDTO;
import com.example.serbUber.dto.user.LoginUserInfoDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.user.LoginRequest;
import com.example.serbUber.request.user.TokenRequest;
import com.example.serbUber.service.user.LoginUserInfoService;
import com.example.serbUber.service.user.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.example.serbUber.service.user.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    private final LoginUserInfoService loginUserInfoService;

    private final TokenService tokenService;

    //@Value("${google.id}")
    private String idClient;

    //@Value("${client.secret}")
    private String password;

    public AuthenticationController(UserService userService, TokenService tokenService, LoginUserInfoService loginUserInfoService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.loginUserInfoService = loginUserInfoService;
    }

    @PostMapping(path="/login", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LoginDTO login(@RequestBody final LoginRequest loginRequest) throws Exception {

        return getLoginDTO(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping(path="/google", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LoginDTO loginWithGoogle(@RequestBody final TokenRequest tokenRequest) throws Exception {
        NetHttpTransport transport = new NetHttpTransport();
        GsonFactory factory = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder tokenVerifier =
            new GoogleIdTokenVerifier.Builder(transport,factory)
                .setAudience(Collections.singleton(idClient));

        GoogleIdToken googleIdToken = GoogleIdToken.parse(
            tokenVerifier.getJsonFactory(),
            tokenRequest.getToken()
        );
        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        return getLoginDTO(payload.getEmail(), password);
    }

    private LoginDTO getLoginDTO(final String email, final String password)
        throws EntityNotFoundException
    {
        LoginUserInfoDTO userDTO = loginUserInfoService.get(email);
        JwtLogin jwtLogin = new JwtLogin(userDTO.getEmail(), password);

        return tokenService.login(jwtLogin);
    }
}
