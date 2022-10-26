package com.example.serbUber.service.user;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.user.RegularUser;
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
        try {
            UserDTO userDTO = userService.get(email);

            return new UserPrinciple(userDTO);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
