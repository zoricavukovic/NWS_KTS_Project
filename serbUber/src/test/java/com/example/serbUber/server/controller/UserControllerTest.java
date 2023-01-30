package com.example.serbUber.server.controller;

import com.example.serbUber.request.LinkedPassengersRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.serbUber.server.controller.helper.ControllerConstants.USER_EMAIL_DRIVING;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@Transactional
public class UserControllerTest {

    private static final String USER_URL_PREFIX = "/users";

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
    @DisplayName("T1 - Should return user by email - /byEmail/{email}")
    @WithMockUser(roles={"REGULAR_USER", "DRIVER", "ADMIN"})
    @Rollback(true)
    public void getUserByEmail_returnUser() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/byEmail/%s", USER_URL_PREFIX, USER_EMAIL_DRIVING))
                        .contentType(contentType).content("")).andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(USER_EMAIL_DRIVING))
                .andExpect(jsonPath("$.name").value("Srdjan"));
    }

}
