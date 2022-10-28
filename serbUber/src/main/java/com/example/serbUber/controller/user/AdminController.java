package com.example.serbUber.controller.user;

import com.example.serbUber.dto.user.AdminDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.user.AdminRequest;
import com.example.serbUber.request.user.UserEmailRequest;
import com.example.serbUber.service.user.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<AdminDTO> getAll() {

        return adminService.getAll();
    }

    @GetMapping("/byEmail")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public UserDTO get(
        @Valid @RequestBody UserEmailRequest emailRequest
    ) throws EntityNotFoundException {

        return adminService.get(emailRequest.getEmail());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public AdminDTO create(@Valid @RequestBody AdminRequest adminRequest) {

        return adminService.create(
            adminRequest.getEmail(),
            adminRequest.getPassword(),
            adminRequest.getName(),
            adminRequest.getSurname(),
            adminRequest.getPhoneNumber(),
            adminRequest.getAddress(),
            adminRequest.getProfilePicture()
        );
    }
}
