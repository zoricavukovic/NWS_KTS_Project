package com.example.serbUber.service;

import com.example.serbUber.dto.UserDTO;
import com.example.serbUber.model.User;
import com.example.serbUber.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.serbUber.dto.UserDTO.fromUsers;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();

        return fromUsers(users);
    }
}
