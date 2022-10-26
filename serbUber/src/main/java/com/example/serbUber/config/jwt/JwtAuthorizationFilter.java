package com.example.serbUber.config.jwt;

import com.auth0.jwt.JWT;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;
import com.example.serbUber.model.user.UserPrinciple;
import com.example.serbUber.service.user.UserService;
import com.example.serbUber.util.JwtProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.serbUber.util.JwtProperties.SECRET;
import static com.example.serbUber.util.JwtProperties.TOKEN_PREFIX;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

    private final UserService userService;

    public JwtAuthorizationFilter(
        AuthenticationManager authenticationManager,
        UserService userService
    ) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        if (headerIsInvalid(header)) {
            chain.doFilter(request, response);
        }
        else {
            Authentication authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        Authentication authentication = null;
        try {
            authentication = getUsernamePasswordAuthentication(request);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return authentication;
    }

    private boolean headerIsInvalid(String header){

        return header == null || !header.startsWith(TOKEN_PREFIX);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request)
        throws EntityNotFoundException {
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(TOKEN_PREFIX,"");

        String email = JWT.require(HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

        return emailIsNotNull(email) ? getSpringAuthToken(email) : null;
    }

    private UsernamePasswordAuthenticationToken getSpringAuthToken(String email)
        throws EntityNotFoundException
    {
        UserDTO userDTO = userService.get(email);
        return getUsernamePasswordAuthenticationToken(userDTO);
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(UserDTO userDTO) {
        UserPrinciple principal = new UserPrinciple(userDTO);

        return new UsernamePasswordAuthenticationToken(
            userDTO.getEmail(),
            principal.getPassword(),
            principal.getAuthorities()
        );
    }

    private boolean emailIsNotNull(String email){
        return email != null;
    }
}
