package com.example.serbUber.server.controller;

import com.example.serbUber.exception.DriverAlreadyHasStartedDrivingException;
import com.example.serbUber.exception.DrivingShouldNotStartYetException;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.request.LinkedPassengersRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class DrivingControllerTest {

    private static final String DRIVING_URL_PREFIX = "/drivings";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("T1-Should successfully reject driving when making PUT request to endpoint - /drivings/reject/{drivingId}")
    @WithMockUser(roles = "DRIVER")
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
    @WithMockUser(roles = "DRIVER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundRejectDriving() throws Exception {

        String reason = "reason";
        String errorMessage = getEntityErrorMessage(NOT_EXIST_ID.toString(), EntityType.DRIVING);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/reject/%d", DRIVING_URL_PREFIX, NOT_EXIST_ID))
                        .contentType(contentType).content(reason)).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T3-Should throw validation error when reason is too long when making PUT request to endpoint - /drivings/reject/{drivingId}")
    @WithMockUser(roles = "DRIVER")
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
    @WithMockUser(roles = "DRIVER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundStartDriving() throws Exception {

        String errorMessage = getEntityErrorMessage(NOT_EXIST_ID.toString(), EntityType.DRIVING);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/start/%d", DRIVING_URL_PREFIX, NOT_EXIST_ID))
                        .contentType(contentType).content("")).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    @DisplayName("T5-Should throw driver has already started driving exception when making PUT request to endpoint - /drivings/start/{drivingId}")
    @WithMockUser(roles = "DRIVER")
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
    @WithMockUser(roles = "DRIVER")
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
    @WithMockUser(roles = "DRIVER")
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
    @WithMockUser(roles = "DRIVER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundFinishDriving() throws Exception {

        String errorMessage = getEntityErrorMessage(NOT_EXIST_ID.toString(), EntityType.DRIVING);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/finish-driving/%d", DRIVING_URL_PREFIX, NOT_EXIST_ID))
                        .contentType(contentType).content("")).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T9-Should successfully finish driving when making PUT request to endpoint - /drivings/finish-driving/{drivingId}")
    @WithMockUser(roles = "DRIVER")
    @Rollback(true)
    public void shouldSuccessfullyFinishDriving() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/finish-driving/%d", DRIVING_URL_PREFIX, ACTIVE_DRIVING_ID))
                        .contentType(contentType).content("")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ACTIVE_DRIVING_ID))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.drivingStatus").value(FINISHED.toString()));
    }

    @Test
    @DisplayName("T10 - Should get driving when making GET request to endpoint - /drivings/{id}")
    @WithMockUser(roles = {"DRIVER", "REGULAR_USER", "ADMIN"})
    @Rollback(true)
    public void getDriving_returnDrivingForId() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/%d", DRIVING_URL_PREFIX, ACTIVE_DRIVING_ID))
                        .contentType(contentType).content("")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ACTIVE_DRIVING_ID));
    }

    @Test
    @DisplayName("T11 - Should throw EntityNotFoundException when making GET request to endpoint - /drivings/{id}")
    @WithMockUser(roles = {"DRIVER", "REGULAR_USER", "ADMIN"})
    @Rollback(true)
    public void getDriving_throwEntityNotFoundException() throws Exception {

        String errorMessage = getEntityErrorMessage(NOT_EXIST_ID.toString(), EntityType.DRIVING);
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/%d", DRIVING_URL_PREFIX, NOT_EXIST_ID))
                        .contentType(contentType).content("")).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T12 - Should get vehicle current location when making GET request to endpoint - /vehicle-current-location/{drivingId}")
    @Rollback(true)
    public void getVehicleCurrentLocation_returnVehicleCurrentLocationDTOForDrivingId() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/vehicle-current-location/%d", DRIVING_URL_PREFIX, ACTIVE_DRIVING_ID))
                        .contentType(contentType).content("")).andExpect(status().isOk())
                .andExpect(jsonPath("$.driverId").value(DRIVER_ID_FOR_ACTIVE_DRIVING));
    }

    @Test
    @DisplayName("T13 - Should throw EntityNotFoundException when making GET request to endpoint - /vehicle-current-location/{drivingId}")
    @Rollback(true)
    public void getVehicleCurrentLocation_throwEntityNotFoundException() throws Exception {

        String errorMessage = getEntityErrorMessage(NOT_EXIST_ID.toString(), EntityType.DRIVING);
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/vehicle-current-location/%d", DRIVING_URL_PREFIX, NOT_EXIST_ID))
                        .contentType(contentType).content("")).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T14 - Should throw EntityNotFoundException when making POST request to endpoint - /busy-passengers")
    @WithMockUser(roles = "REGULAR_USER")
    @Rollback(true)
    public void isPassengersAlreadyHaveRide_throwEntityNotFoundException() throws Exception {
        List<String> linkedPassengers = new ArrayList<>();
        linkedPassengers.add(NOT_EXIST_USER_EMAIL);
        LinkedPassengersRequest request = new LinkedPassengersRequest(linkedPassengers, LocalDateTime.now());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); //izdvojiti u posebnu metodu

        String requestJSON = mapper.writeValueAsString(request);
        String errorMessage = getEntityErrorMessage(NOT_EXIST_USER_EMAIL, EntityType.USER);
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("%s/busy-passengers", DRIVING_URL_PREFIX))
                        .contentType(contentType).content(requestJSON)).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T15 - Should return true when making POST request to endpoint - /busy-passengers")
    @WithMockUser(roles = "REGULAR_USER")
    @Rollback(true)
    public void isPassengersAlreadyHaveRide_returnTrue() throws Exception {
        List<String> linkedPassengers = new ArrayList<>();
        linkedPassengers.add(USER_ACTIVE_DRIVING_EMAIL);
        LinkedPassengersRequest request = new LinkedPassengersRequest(linkedPassengers, LocalDateTime.now());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); //izdvojiti u posebnu metodu

        String requestJSON = mapper.writeValueAsString(request);
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("%s/busy-passengers", DRIVING_URL_PREFIX))
                        .contentType(contentType).content(requestJSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @DisplayName("T16 - Should return false when making POST request to endpoint - /busy-passengers")
    @WithMockUser(roles = "REGULAR_USER")
    @Rollback(true)
    public void isPassengersAlreadyHaveRide_returnFalse() throws Exception {
        List<String> linkedPassengers = new ArrayList<>();
        linkedPassengers.add(USER_DRIVING_EMAIL);
        LinkedPassengersRequest request = new LinkedPassengersRequest(linkedPassengers, LocalDateTime.now());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); //izdvojiti u posebnu metodu

        String requestJSON = mapper.writeValueAsString(request);
        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("%s/busy-passengers", DRIVING_URL_PREFIX))
                        .contentType(contentType).content(requestJSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    @DisplayName("T17 - Should throw EntityNotFoundException when making GET request to endpoint - /time-for-driving/{drivingId}")
    @WithMockUser(roles = "REGULAR_USER")
    @Rollback(true)
    public void getTimeForDriving_throwEntityNotFoundException() throws Exception {

        String errorMessage = getEntityErrorMessage(NOT_EXIST_ID.toString(), EntityType.DRIVING);
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/time-for-driving/%d", DRIVING_URL_PREFIX, NOT_EXIST_ID))
                        .contentType(contentType).content("")).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T18 - Should return LocalDateTime for driving with drivingId when making GET request to endpoint - /time-for-driving/{drivingId}")
    @WithMockUser(roles = "REGULAR_USER")
    @Rollback(true)
    public void getTimeForDriving_returnStartedDateTimeForDriving() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/time-for-driving/%d", DRIVING_URL_PREFIX, NOT_ACTIVE_DRIVING_ID))
                        .contentType(contentType).content("")).andExpect(status().isOk())
                .andExpect(jsonPath("$").value(STARTED_DRIVING_THREE_ID));
    }

    @ParameterizedTest
    @CsvSource(value = {"14,2", "18,1"})
    @DisplayName("T19 - Should successfully return all now and future drivings for driver when making GET request to endpoint - /drivings/now-and-future/{id}")
    @WithMockUser(roles = "DRIVER")
    @Rollback(true)
    public void shouldSuccessfullyReturnAllNowAndFutureDrivingsForDriver(Long driverId, int numOfDrivings) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/now-and-future/%d", DRIVING_URL_PREFIX, driverId))
                        .contentType(contentType)).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(numOfDrivings))
                .andExpect(jsonPath("$.[0].driverId").value(driverId));
    }

    @Test
    @DisplayName("T20 - Should return empty list of now and future drivings for driver when making GET request to endpoint - /drivings/now-and-future/{id}")
    @WithMockUser(roles = "DRIVER")
    @Rollback(true)
    public void shouldSuccessfullyReturnAllNowAndFutureDrivingsForDriver() throws Exception {
        Long driverId = 50L;
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/now-and-future/%d", DRIVING_URL_PREFIX, driverId))
                        .contentType(contentType)).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("T21 - Should not return active driving for user when making GET request to endpoint - /drivings/has-active/user/{id}")
    @WithMockUser(roles = "REGULAR_USER")
    @Rollback(true)
    public void shouldNotReturnActiveDrivingForUser() throws Exception {
        Long userId = 13L;
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/has-active/user/%d", DRIVING_URL_PREFIX, userId))
                        .contentType(contentType)).andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertEquals("", response);
    }

    @ParameterizedTest
    @CsvSource(value = {"8,1,ACCEPTED", "12,9,ON_WAY_TO_DEPARTURE"})
    @DisplayName("T22 - Should successfully return active driving for user when making GET request to endpoint - /drivings/has-active/user/{id}")
    @WithMockUser(roles = "REGULAR_USER")
    @Rollback(true)
    public void shouldReturnActiveDrivingForUser(Long userId, Long drivingId, String drivingStatus) throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/has-active/user/%d", DRIVING_URL_PREFIX, userId))
                        .contentType(contentType)).andExpect(status().isOk())
                .andExpect(jsonPath("$.drivingId").value(drivingId))
                .andExpect(jsonPath("$.drivingStatus").value(drivingStatus))
                .andExpect(jsonPath("$.active").value(true));
    }
}
