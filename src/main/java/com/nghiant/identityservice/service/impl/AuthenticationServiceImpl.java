package com.nghiant.identityservice.service.impl;

import com.nghiant.identityservice.dto.request.AuthenticationRequest;
import com.nghiant.identityservice.dto.response.AuthenticationResponse;
import com.nghiant.identityservice.entity.User;
import com.nghiant.identityservice.exception.AppException;
import com.nghiant.identityservice.exception.ErrorCode;
import com.nghiant.identityservice.repository.UserRepository;
import com.nghiant.identityservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;

  @Override
  public boolean authenticated(AuthenticationRequest request) {
    User user = userRepository.findUserByUsername(request.getUsername())
        .orElseThrow(() -> new AppException(
            ErrorCode.USER_NOT_FOUND));

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    authenticationResponse.setAuthenticated(passwordEncoder.matches(request.getPassword(),
        user.getPassword()));

    return authenticationResponse.isAuthenticated();
  }
}
