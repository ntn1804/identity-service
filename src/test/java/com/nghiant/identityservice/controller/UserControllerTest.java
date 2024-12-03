package com.nghiant.identityservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nghiant.identityservice.dto.request.UserCreationRequest;
import com.nghiant.identityservice.dto.response.UserResponse;
import com.nghiant.identityservice.service.UserService;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest
// Tao Mock request toi controller
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  private UserCreationRequest userCreationRequest;
  private UserResponse userResponse;

  @BeforeEach
  void initData() {
    LocalDate dob = LocalDate.of(1996, 4, 18);

    userCreationRequest = UserCreationRequest.builder()
        .username("john1")
        .firstName("John")
        .lastName("Doe")
        .password("12345678")
        .dob(dob)
        .build();

    userResponse = UserResponse.builder()
        .userId("cf0600f538b3")
        .username("john1")
        .firstName("John")
        .lastName("Doe")
        .dob(dob)
        .build();
  }

  @Test
  void createUser_validRequest_success() throws Exception {
    // GIVEN
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String content = objectMapper.writeValueAsString(userCreationRequest);

    // Mock the response. Do not let it call the service layer
    Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

    // WHEN
    mockMvc.perform(MockMvcRequestBuilders
        .post("/users")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(content))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));

    // THEN (andExpect() method upper)
  }

  @Test
  void createUser_invalidUsername_failed() throws Exception {
    // GIVEN
    userCreationRequest.setUsername("john");
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String content = objectMapper.writeValueAsString(userCreationRequest);

    // Mock the response. Do not let it call the service layer
    Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

    // WHEN
    mockMvc.perform(MockMvcRequestBuilders
            .post("/users")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(content))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("code").value(400))
        .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 5 characters"));

    // THEN (andExpect() method upper)
  }
}
