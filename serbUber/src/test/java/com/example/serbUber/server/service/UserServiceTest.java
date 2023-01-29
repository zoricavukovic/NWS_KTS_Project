package com.example.serbUber.server.service;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.user.UserRepository;
import com.example.serbUber.service.DriverUpdateApprovalService;
import com.example.serbUber.service.EmailService;
import com.example.serbUber.service.VerifyService;
import com.example.serbUber.service.user.DriverService;
import com.example.serbUber.service.user.RegularUserService;
import com.example.serbUber.service.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.serbUber.server.helper.Constants.*;
import static com.example.serbUber.server.helper.UserConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private DriverUpdateApprovalService driverUpdateApprovalService;
    @Mock
    private EmailService emailService;
    @Mock
    private DriverService driverService;
    @Mock
    private RegularUserService regularUserService;
    @Mock
    private VerifyService verifyService;
    @InjectMocks
    private UserService userService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //greska: User: user1@gmail.com is not found
    @Test
    @DisplayName("T1 - Should return user")
    public void getUserByEmail_ReturnUser() throws EntityNotFoundException {
        when(userRepository.getUserByEmail(USER_EMAIL_1)).thenReturn(Optional.of(createUser(USER_ID_1, USER_EMAIL_1)));
        assertEquals(USER_ID_1, userService.getUserByEmail(USER_EMAIL_1).getId());
    }

    @Test
    @DisplayName("T2 - Should return driver")
    public void getDriverById_ReturnDriver() throws EntityNotFoundException {
        when(userRepository.getDriverById(DRIVER_ID)).thenReturn(Optional.of(EXIST_DRIVER));
        assertEquals(EXIST_DRIVER_EMAIL, userService.getDriverById(DRIVER_ID).getEmail());
    }

    @Test
    @DisplayName("T3 - Should return driver")
    public void getDriverById_throwEntityNotFoundException() {
        when(userRepository.getDriverById(DRIVER_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getDriverById(DRIVER_ID));
    }
}