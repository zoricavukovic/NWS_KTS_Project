package com.example.serbUber.service.interfaces;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.user.Role;
import org.springframework.stereotype.Service;

@Service
public interface IRoleService {
    Role get(String name) throws EntityNotFoundException;
}
