package com.example.serbUber.service.user;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.repository.user.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    public RoleService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role get(String name) throws EntityNotFoundException {
        Optional<Role> optionalRole = roleRepository.getRoleByName(name);

        if (optionalRole.isPresent()) {
            return optionalRole.get();
        }
        throw new EntityNotFoundException(name, EntityType.ROLE);
    }


}
