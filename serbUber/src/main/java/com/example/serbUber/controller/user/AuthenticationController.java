package com.example.serbUber.controller.user;

import com.example.serbUber.dto.user.JwtLogin;
import com.example.serbUber.dto.user.LoginDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.user.LoginRequest;
import com.example.serbUber.request.user.TokenRequest;
import com.example.serbUber.service.user.TokenService;
import com.example.serbUber.service.user.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.Collections;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final TokenService tokenService;

    private final UserService userService;

    @Value("${google.id}")
    private String idClient;

    public AuthenticationController(
        final TokenService tokenService,
        final UserService userService
    ) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping(path="/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginDTO login(@Valid @RequestBody final LoginRequest loginRequest)
            throws EntityNotFoundException
    {

        return getLoginDTO(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping(path="/login/google", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public LoginDTO loginWithGoogle(@Valid @RequestBody final TokenRequest tokenRequest) throws Exception {
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

        UserDTO userDTO = new UserDTO(userService.getUserByEmail(payload.getEmail()));
        LoginDTO loginDTO = tokenService.googleLogin(userDTO);
        userService.setOnlineStatus(loginDTO.getUserDTO().getEmail());

        return loginDTO;
    }

    @PostMapping(path="/login/facebook", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    private LoginDTO loginWithFacebook(@Valid @RequestBody final TokenRequest tokenRequest) throws EntityNotFoundException {
        Facebook facebook = new FacebookTemplate(tokenRequest.getToken());
        String [] data = {"email"};
        User user = facebook.fetchObject("me", User.class,data);

        UserDTO userDTO = new UserDTO(userService.getUserByEmail(user.getEmail()));
        LoginDTO loginDTO = tokenService.googleLogin(userDTO);
        userService.setOnlineStatus(loginDTO.getUserDTO().getEmail());

        return loginDTO;
    }

    private LoginDTO getLoginDTO(final String email, final String password)
            throws EntityNotFoundException
    {
        JwtLogin jwtLogin = new JwtLogin(email, password);
        LoginDTO loginDTO = tokenService.login(jwtLogin);
        userService.setOnlineStatus(loginDTO.getUserDTO().getEmail());

        return loginDTO;
    }

    @PostMapping(path="/logout")
    @ResponseStatus(HttpStatus.OK)
    private UserDTO logout(@Valid @Email(message = WRONG_EMAIL) @RequestBody final String email)
            throws EntityNotFoundException
    {

        return userService.setOfflineStatus(email);
    }
}
