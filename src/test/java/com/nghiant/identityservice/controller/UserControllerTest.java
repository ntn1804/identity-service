package com.nghiant.identityservice.controller;

import com.nghiant.identityservice.dto.request.UserCreationRequest;
import com.nghiant.identityservice.dto.response.UserResponse;
import com.nghiant.identityservice.service.UserService;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
  private LocalDate dob;

  @BeforeEach
  void initData() {
    dob = LocalDate.of(1996, 4, 18);

    userCreationRequest = UserCreationRequest.builder()
        .username("john")
        .firstName("John")
        .lastName("Doe")
        .password("12345678")
        .dob(dob)
        .build();

    userResponse = UserResponse.builder()
        .userId("cf0600f538b3")
        .username("john")
        .firstName("John")
        .lastName("Doe")
        .dob(dob)
        .build();
  }

  @Test
  void creatUser() {
    // GIVEN

    // WHEN


    // THEN
  }
}
