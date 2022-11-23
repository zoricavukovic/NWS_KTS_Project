package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.user.AdminDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAdminService {

    List<AdminDTO> getAll();
    AdminDTO get(Long id) throws EntityNotFoundException;
    AdminDTO create(
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture
    );
}
