package com.example.serbUber.server.service;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.user.RegularUserRepository;
import com.example.serbUber.service.*;
import com.example.serbUber.service.payment.TokenBankService;
import com.example.serbUber.service.user.RegularUserService;
import com.example.serbUber.service.user.RoleService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static com.example.serbUber.server.service.helper.RegularUserConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegularUserServiceTest {
    @Mock
    private RegularUserRepository regularUserRepository;

    @Mock
    private VerifyService verifyService;

    @Mock
    private RoleService roleService;

    @Mock
    private RouteService routeService;

    @Mock
    private WebSocketService webSocketService;

    @Mock
    private TokenBankService tokenBankService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private RegularUserService regularUserService;

    @Test
    @DisplayName("T1-Should throw entity not found exception when calls getRegularByEmail")
    public void shouldThrowEntityNotFoundException() {
        when(regularUserRepository.getRegularUserByEmail(NOT_EXIST_USER_EMAIL))
            .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
            () -> regularUserService.getRegularByEmail(NOT_EXIST_USER_EMAIL));
    }

    @Test
    @DisplayName("T2-Should successfully return regular user when calls getRegularByEmail")
    public void shouldSuccessfullyReturnRegularUser() throws EntityNotFoundException {

        when(regularUserRepository.getRegularUserByEmail(FIRST_USER_EMAIL))
            .thenReturn(Optional.of(FIRST_USER));
        RegularUser regularUser = regularUserService.getRegularByEmail(FIRST_USER_EMAIL);
        Assertions.assertEquals(FIRST_USER_EMAIL, regularUser.getEmail());
        Assertions.assertEquals(FIRST_USER_NAME, regularUser.getName());
        Assertions.assertEquals(FIRST_USER_SURNAME, regularUser.getSurname());
    }
}
