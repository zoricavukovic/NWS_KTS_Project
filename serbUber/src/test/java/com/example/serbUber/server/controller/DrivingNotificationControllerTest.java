package com.example.serbUber.server.controller;

import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static com.example.serbUber.exception.EntityType.getEntityErrorMessage;
import static com.example.serbUber.server.controller.helper.ControllerConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@Transactional
public class DrivingNotificationControllerTest {
    private static final String DRIVING_NOTIFICATION_URL_PREFIX = "/driving-notifications";

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
    @DisplayName("T1-Should throw entity not found (not found driving) when making PUT request to endpoint - /update-status/{id}/{accepted}/{email}")
    @WithMockUser(roles="REGULAR_USER")
    @Rollback(true)
    public void acceptDriving_throwEntityNotFoundException() throws Exception {

        String errorMessage = getEntityErrorMessage(NOT_EXIST_ID.toString(), EntityType.DRIVING_NOTIFICATION);
        this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/update-status/%d/%s/%s", DRIVING_NOTIFICATION_URL_PREFIX, NOT_EXIST_ID, "true", USER_EMAIL_DRIVING))
                        .contentType(contentType).content("")).andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof EntityNotFoundException)
                )
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
