package com.example.serbUber.dto.user;

import com.example.serbUber.model.Location;
import com.example.serbUber.model.user.Admin;

import java.util.LinkedList;
import java.util.List;

public class AdminDTO {
    private final String email;
    private final String name;
    private final String surname;
    private final String phoneNumber;
    private final Location address;
    private final String profilePicture;

    public AdminDTO(final Admin admin) {
        this.email = admin.getEmail();
        this.name = admin.getName();
        this.surname = admin.getSurname();
        this.phoneNumber = admin.getPhoneNumber();
        this.address = admin.getAddress();
        this.profilePicture = admin.getProfilePicture();
    }

    public static List<AdminDTO> fromAdmins(final List<Admin> admins){
        List<AdminDTO> adminDTOs = new LinkedList<>();
        admins.forEach(admin ->
            adminDTOs.add(new AdminDTO(admin))
        );

        return adminDTOs;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Location getAddress() {
        return address;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
