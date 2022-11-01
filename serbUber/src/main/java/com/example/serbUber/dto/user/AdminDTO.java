package com.example.serbUber.dto.user;

import com.example.serbUber.model.user.Admin;

import java.util.LinkedList;
import java.util.List;

public class AdminDTO extends UserDTO{

    public AdminDTO(final Admin admin) {
        super(
            admin.getEmail(),
            admin.getName(),
            admin.getSurname(),
            admin.getPhoneNumber(),
            admin.getCity(),
            admin.getProfilePicture(),
            admin.getRole(),
            admin.getPassword()
        );
    }

    public static List<AdminDTO> fromAdmins(final List<Admin> admins){
        List<AdminDTO> adminDTOs = new LinkedList<>();
        admins.forEach(admin ->
            adminDTOs.add(new AdminDTO(admin))
        );

        return adminDTOs;
    }
}
