package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.user.UserPrinciple;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) {

        UserDTO userDTO;
        try {
            userDTO = new UserDTO(userService.getUserByEmail(email));
        } catch (EntityNotFoundException e) {

            return null;
        }

        return new UserPrinciple(userDTO);

    }
}
