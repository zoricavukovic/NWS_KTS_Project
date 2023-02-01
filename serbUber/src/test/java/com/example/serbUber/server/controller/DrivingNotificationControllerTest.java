package com.example.serbUber.server.controller;

import com.example.serbUber.exception.*;
import com.example.serbUber.request.DrivingNotificationRequest;
import com.example.serbUber.server.controller.helper.TestUtil;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.serbUber.exception.EntityType.getEntityErrorMessage;
import static com.example.serbUber.exception.ErrorMessagesConstants.INVALID_CHOSEN_TIME_AFTER_FOR_RESERVATION_MESSAGE;
import static com.example.serbUber.exception.ErrorMessagesConstants.INVALID_CHOSEN_TIME_BEFORE_FOR_RESERVATION_MESSAGE;
import static com.example.serbUber.server.controller.helper.ControllerConstants.*;
import static com.example.serbUber.server.controller.helper.DrivingNotificationConstants.*;
import static com.example.serbUber.util.Constants.UNSUCCESSFUL_PAYMENT_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class DrivingNotificationControllerTest {

    private static final String DRIVING_NOTIFICATION_URL_PREFIX = "/driving-notifications";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("T1-Should throw entity not found (not found driving) when making PUT request to endpoint - /update-status/{id}/{accepted}/{email}")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void acceptDriving_throwEntityNotFoundException() throws Exception {

        String errorMessage = getEntityErrorMessage(NOT_EXIST_ID.toString(), EntityType.DRIVING_NOTIFICATION);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/update-status/%d/%s/%s", DRIVING_NOTIFICATION_URL_PREFIX, NOT_EXIST_ID, "true", USER_DRIVING_EMAIL))
                        .contentType(contentType).content("")).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T2-Should throw entity bad request exception, email is not valid when making PUT request to endpoint - /update-status/{id}/{accepted}/{email}")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void acceptDriving_throwBadRequestException_emailInvalid() throws Exception {

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put(
                String.format("%s/update-status/%d/%s/%s", DRIVING_NOTIFICATION_URL_PREFIX, EXIST_ID, "true", INVALID_EMAIL))
                .contentType(contentType).content("")).andExpect(status().isBadRequest()).andReturn();
        Assertions.assertEquals("acceptDriving.email: Email is not in the right format.", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("T3-Should return updated driving notification when making PUT request to endpoint - /update-status/{id}/{accepted}/{email}")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void acceptDriving_returnUpdatedDrivingNotification() throws Exception {

        String userEmail = "miki@gmail.com";
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/update-status/%d/%s/%s", DRIVING_NOTIFICATION_URL_PREFIX, 2L, "true", userEmail))
                        .contentType(contentType)).andExpect(status().isOk())
                .andExpect(jsonPath("$.passengers.size()").value(2));
    }

    @Test
    @DisplayName("T4-Should successfully get driving notification when making GET request to endpoint - /driving-notifications/{id}")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void shouldSuccessfullyGetDrivingNotification() throws Exception {
        Long drivingNotificationId = 1L;
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/%d", DRIVING_NOTIFICATION_URL_PREFIX, drivingNotificationId))
                .contentType(contentType)).andExpect(status().isOk())
            .andExpect(jsonPath("$.price").value(10))
            .andExpect(jsonPath("$.duration").value(5))
            .andExpect(jsonPath("$.passengers.size()").value(1));
    }

    @Test
    @DisplayName("T5-Should throw entity not found (not found driving notification) when making GET request to endpoint - /driving-notifications/{id}")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundGetDrivingNotification() throws Exception {

        String errorMessage = getEntityErrorMessage(NOT_EXIST_ID.toString(), EntityType.DRIVING_NOTIFICATION);
        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/%d", DRIVING_NOTIFICATION_URL_PREFIX, NOT_EXIST_ID))
                .contentType(contentType)).andExpect(status().isNotFound())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
            )
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T6-Should throw entity not found (not found sender/creator of driving notification) when making POST request to endpoint - /driving-notifications")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundSenderNotFoundCreatingDrivingNotification() throws Exception {
        String json = TestUtil.json(DRIVING_NOTIFICATION_REQUEST_SENDER_NOT_FOUND);
        String errorMessage = getEntityErrorMessage(DRIVING_NOTIFICATION_REQUEST_SENDER_NOT_FOUND.getSenderEmail(), EntityType.USER);
        this.mockMvc.perform(MockMvcRequestBuilders.post(DRIVING_NOTIFICATION_URL_PREFIX)
                .contentType(contentType).content(json)).andExpect(status().isNotFound())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
            )
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T7-Should throw entity not found (not found passenger) when making POST request to endpoint - /driving-notifications")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundOneOfPassengerNotFoundCreatingDrivingNotification() throws Exception {
        String json = TestUtil.json(DRIVING_NOTIFICATION_REQUEST_PASSENGER_NOT_FOUND);
        String errorMessage = getEntityErrorMessage(DRIVING_NOTIFICATION_REQUEST_PASSENGER_NOT_FOUND.getPassengers().get(0), EntityType.USER);
        this.mockMvc.perform(MockMvcRequestBuilders.post(DRIVING_NOTIFICATION_URL_PREFIX)
                .contentType(contentType).content(json)).andExpect(status().isNotFound())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
            )
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T8-Should throw entity not found (not found vehicle type) when making POST request to endpoint - /driving-notifications")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundVehicleTypeNotFoundCreatingDrivingNotification() throws Exception {
        String json = TestUtil.json(DRIVING_NOTIFICATION_REQUEST_VEHICLE_TYPE_NOT_FOUND);
        String errorMessage = getEntityErrorMessage("NOT_FOUND", EntityType.VEHICLE_TYPE_INFO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(DRIVING_NOTIFICATION_URL_PREFIX)
                .contentType(contentType).content(json)).andExpect(status().isNotFound())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
            )
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @ParameterizedTest
    @DisplayName("T9-Should throw ExcessiveNumOfPassengersException when making POST request to endpoint - /driving-notifications")
    @WithMockUser(roles="REGULAR_USER")
    @MethodSource("getDrivingNotificationRequestWithExcessiveNumberOfPassengers")
    @Rollback(true)
    public void shouldThrowExcessiveNumOfPassengersExceptionCreatingDrivingNotification(DrivingNotificationRequest drivingNotificationRequest) throws Exception {
        String json = TestUtil.json(drivingNotificationRequest);
        String errorMessage = String.format("Excessive number of passengers for %s", drivingNotificationRequest.getVehicleType());
        this.mockMvc.perform(MockMvcRequestBuilders.post(DRIVING_NOTIFICATION_URL_PREFIX)
                .contentType(contentType).content(json)).andExpect(status().isBadRequest())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof ExcessiveNumOfPassengersException)
            )
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @ParameterizedTest
    @DisplayName("T10-Should throw InvalidChosenTimeForReservationException when making POST request to endpoint - /driving-notifications")
    @WithMockUser(roles="REGULAR_USER")
    @MethodSource("getDrivingNotificationRequestInvalidTime")
    @Rollback(true)
    public void shouldThrowInvalidChosenTimeForReservationExceptionCreatingDrivingNotification(
        DrivingNotificationRequest drivingNotificationRequest, String errorMessage
    ) throws Exception {
        String json = TestUtil.json(drivingNotificationRequest);

        this.mockMvc.perform(MockMvcRequestBuilders.post(DRIVING_NOTIFICATION_URL_PREFIX)
                .contentType(contentType).content(json)).andExpect(status().isBadRequest())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof InvalidChosenTimeForReservationException)
            )
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T11-Should throw PassengerNotHaveTokensException when making POST request to endpoint - /driving-notifications")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void shouldThrowPassengerNotHaveTokensExceptionCreatingDrivingNotification() throws Exception {
        String json = TestUtil.json(DRIVING_NOTIFICATION_REQUEST_USER_DOESNOT_HAVE_ENOUGH_MONEY);
        this.mockMvc.perform(MockMvcRequestBuilders.post(DRIVING_NOTIFICATION_URL_PREFIX)
                .contentType(contentType).content(json)).andExpect(status().isBadRequest())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof PassengerNotHaveTokensException)
            )
            .andExpect(result -> assertEquals(UNSUCCESSFUL_PAYMENT_MESSAGE, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @ParameterizedTest
    @DisplayName("T12-Should successfully create driving notification when num of passengers is greater than zero when making POST request to endpoint - /driving-notifications")
    @WithMockUser(roles="REGULAR_USER")
    @MethodSource("getDrivingNotificationsRequestWithAndWithoutPassengersSuccessfullyCreation")
    @Rollback(true)
    public void shouldSuccessfullyCreateDrivingNotificationWhenNumOfPassengersGraterThanZero(
        DrivingNotificationRequest drivingNotificationRequest
    ) throws Exception {
        String json = TestUtil.json(drivingNotificationRequest);
        this.mockMvc.perform(MockMvcRequestBuilders.post(DRIVING_NOTIFICATION_URL_PREFIX)
                .contentType(contentType).content(json)).andExpect(status().isCreated())
            .andExpect(jsonPath("$.price").value(drivingNotificationRequest.getPrice()))
            .andExpect(jsonPath("$.passengers.size()").value(drivingNotificationRequest.getPassengers().size() + 1));
    }

    @Test
    @DisplayName("T13-Should throw EntityNotFoundException for token bank when making POST request to endpoint - /driving-notifications")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void shouldThrowEntityNotFoundExceptionForTokenBankCreatingDrivingNotification() throws Exception {
        String json = TestUtil.json(DRIVING_NOTIFICATION_REQUEST_WITHOUT_TOKEN_BANK);
        String errorMessage = getEntityErrorMessage("20", EntityType.USER);

        this.mockMvc.perform(MockMvcRequestBuilders.post(DRIVING_NOTIFICATION_URL_PREFIX)
                .contentType(contentType).content(json)).andExpect(status().isNotFound())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
            )
            .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DisplayName("T14-Should throw BadRequestException when price is less than zero when making POST request to endpoint - /driving-notifications")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void shouldThrowBadRequestExceptionPriceIsLessThanZeroCreatingDrivingNotification() throws Exception {
        String json = TestUtil.json(DRIVING_NOTIFICATION_REQUEST_PRICE_LESS_THAN_ZERO);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post(DRIVING_NOTIFICATION_URL_PREFIX)
                .contentType(contentType).content(json)).andExpect(status().isBadRequest()).andReturn();
        Assertions.assertEquals("Value must be positive.", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("T15-Should throw BadRequestException when sender email is not valid when making POST request to endpoint - /driving-notifications")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void shouldThrowBadRequestExceptionEmailIsInvalidCreatingDrivingNotification() throws Exception {
        String json = TestUtil.json(DRIVING_NOTIFICATION_REQUEST_SENDER_EMAIL_IS_NOT_VALID);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post(DRIVING_NOTIFICATION_URL_PREFIX)
            .contentType(contentType).content(json)).andExpect(status().isBadRequest()).andReturn();
        Assertions.assertEquals("Email is not in the right format.", result.getResponse().getContentAsString());
    }

    private List<Arguments> getDrivingNotificationRequestInvalidTime() {

        return Arrays.asList(
            Arguments.arguments(DRIVING_NOTIFICATION_REQUEST_INVALID_TIME_30_MIN, INVALID_CHOSEN_TIME_BEFORE_FOR_RESERVATION_MESSAGE),
            Arguments.arguments(DRIVING_NOTIFICATION_REQUEST_INVALID_TIME_5_HOURS, INVALID_CHOSEN_TIME_AFTER_FOR_RESERVATION_MESSAGE)
        );
    }

    private List<Arguments> getDrivingNotificationsRequestWithAndWithoutPassengersSuccessfullyCreation() {

        return Arrays.asList(
            Arguments.arguments(DRIVING_NOTIFICATION_REQUEST_NUM_OF_PASSENGERS_IS_GRATER_THAN_ZERO),
            Arguments.arguments(DRIVING_NOTIFICATION_REQUEST_WITHOUT_PASSENGERS_AND_IS_RESERVATION)
        );
    }

    private List<Arguments> getDrivingNotificationRequestWithExcessiveNumberOfPassengers() {

        return Arrays.asList(
            Arguments.arguments(DRIVING_NOTIFICATION_REQUEST_EXCESSIVE_NUM_OF_PASSENGERS_ON_EDGE),
            Arguments.arguments(DRIVING_NOTIFICATION_REQUEST_EXCESSIVE_NUM_OF_PASSENGERS)
        );
    }
}
