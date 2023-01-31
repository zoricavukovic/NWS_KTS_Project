package com.example.serbUber.service.user;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.repository.user.RoleRepository;
import com.example.serbUber.service.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Qualifier("roleServiceConfiguration")
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;

    public RoleService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role get(String name) throws EntityNotFoundException {

        return roleRepository.getRoleByName(name)
            .orElseThrow(() -> new EntityNotFoundException(name, EntityType.ROLE));
    }
}
