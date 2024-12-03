package com.nghiant.identityservice.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nghiant.identityservice.dto.request.UserCreationRequest;
import com.nghiant.identityservice.dto.response.UserResponse;
import com.nghiant.identityservice.entity.User;
import com.nghiant.identityservice.mapper.UserMapper;
import com.nghiant.identityservice.repository.UserRepository;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  private UserCreationRequest userCreationRequest;
  private UserResponse userResponse;
  private User user;

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

    user = User.builder()
        .userId("cf0600f538b3")
        .username("john1")
        .firstName("John")
        .lastName("Doe")
        .dob(dob)
        .build();
  }

  @Test
  void createUser_success() {

    when(userRepository.existsByUsername(any())).thenReturn(false);
    when(userRepository.save(any())).thenReturn(user);

    var response =  userService.createUser(userCreationRequest);

        // THEN
    Assertions.assertThat(response.getUserId()).isEqualTo("cf0600f538b3");
    Assertions.assertThat(response.getUsername()).isEqualTo("john1");

  }
}
