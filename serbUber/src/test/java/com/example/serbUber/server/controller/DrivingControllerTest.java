package com.example.serbUber.server.controller;

import com.example.serbUber.exception.DriverAlreadyHasStartedDrivingException;
import com.example.serbUber.exception.DrivingShouldNotStartYetException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static com.example.serbUber.exception.EntityType.getEntityErrorMessage;
import static com.example.serbUber.exception.ErrorMessagesConstants.DRIVER_ALREADY_HAS_STARTED_DRIVING_EXCEPTION;
import static com.example.serbUber.exception.ErrorMessagesConstants.DRIVING_SHOULD_NOT_START_YET;
import static com.example.serbUber.model.DrivingStatus.*;
import static com.example.serbUber.server.controller.helper.ControllerConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@Transactional
public class DrivingControllerTest {

    private static final String DRIVING_URL_PREFIX = "/drivings";

    private MockMvc mockMvc;
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype());

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("T1-Should successfully reject driving when making PUT request to endpoint - /drivings/reject/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldSuccessfullyRejectDriving() throws Exception {

        String reason = "reason";
        Long drivingId = 1L;
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/reject/%d", DRIVING_URL_PREFIX, drivingId))
            .contentType(contentType).content(reason)).andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(drivingId))
            .andExpect(jsonPath("$.drivingStatus").value(REJECTED.toString()));
    }

    @Test
    @DisplayName("T2-Should throw entity not found (not found driving) when making PUT request to endpoint - /drivings/reject/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundRejectDriving() throws Exception {

        String reason = "reason";
        String errorMessage = getEntityErrorMessage(NOT_EXIST_ENTITY.toString(), EntityType.DRIVING);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/reject/%d", DRIVING_URL_PREFIX, NOT_EXIST_ENTITY))
                .contentType(contentType).content(reason)).andExpect(status().isNotFound())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
            )
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T3-Should throw validation error when reason is too long when making PUT request to endpoint - /drivings/reject/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldThrowValidationErrorWhenMissingReasonRejectDriving() throws Exception {
        Long drivingId = 1L;
        String tooLongReason = "AKAKKSAKSDAJDJWDHUJASDASKJDKASLJDKJAKDJKSAJDKASJDKA";
        String errorMessage = "rejectDriving.reason: Entered reason must contain less than 50 letters.";
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/reject/%d", DRIVING_URL_PREFIX, drivingId))
            .contentType(contentType).content(tooLongReason)).andExpect(status().isBadRequest())
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T4-Should throw entity not found (not found driving) when making PUT request to endpoint - /drivings/start/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundStartDriving() throws Exception {

        String errorMessage = getEntityErrorMessage(NOT_EXIST_ENTITY.toString(), EntityType.DRIVING);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/start/%d", DRIVING_URL_PREFIX, NOT_EXIST_ENTITY))
                        .contentType(contentType).content("")).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    @DisplayName("T5-Should throw driver has already started driving exception when making PUT request to endpoint - /drivings/start/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldThrowDriverHasAlreadyStartedDrivingExceptionStartDriving() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/start/%d", DRIVING_URL_PREFIX, DRIVING_CANNOT_BE_STARTED_ID))
                        .contentType(contentType).content("")).andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof DriverAlreadyHasStartedDrivingException)
                )
                .andExpect(result -> assertEquals(DRIVER_ALREADY_HAS_STARTED_DRIVING_EXCEPTION, Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    @DisplayName("T6-Should throw driving should not start yet exception when making PUT request to endpoint - /drivings/start/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldThrowDrivingShouldNotStartYetExceptionStartDriving() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/start/%d", DRIVING_URL_PREFIX, DRIVING_SHOULD_NOT_START_YET_ID))
                        .contentType(contentType).content("")).andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof DrivingShouldNotStartYetException)
                )
                .andExpect(result -> assertEquals(DRIVING_SHOULD_NOT_START_YET, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T7-Should successfully start driving when making PUT request to endpoint - /drivings/start/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldSuccessfullyStartDriving() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/start/%d", DRIVING_URL_PREFIX, DRIVING_CAN_BE_STARTED))
                        .contentType(contentType).content("")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DRIVING_CAN_BE_STARTED))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.drivingStatus").value(ACCEPTED.toString()));
    }

    @Test
    @DisplayName("T8-Should throw entity not found (not found driving) when making PUT request to endpoint - /drivings/finish-driving/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundFinishDriving() throws Exception {

        String errorMessage = getEntityErrorMessage(NOT_EXIST_ENTITY.toString(), EntityType.DRIVING);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/finish-driving/%d", DRIVING_URL_PREFIX, NOT_EXIST_ENTITY))
                        .contentType(contentType).content("")).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T9-Should successfully finish driving when making PUT request to endpoint - /drivings/finish-driving/{drivingId}")
    @WithMockUser(roles="DRIVER")
    @Rollback(true)
    public void shouldSuccessfullyFinishDriving() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/finish-driving/%d", DRIVING_URL_PREFIX, ACTIVE_DRIVING_ID))
                        .contentType(contentType).content("")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ACTIVE_DRIVING_ID))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.drivingStatus").value(FINISHED.toString()));
    }

}
