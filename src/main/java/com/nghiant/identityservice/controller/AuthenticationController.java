package com.nghiant.identityservice.controller;

import com.nghiant.identityservice.dto.request.AuthenticationRequest;
import com.nghiant.identityservice.dto.response.ApiResponse;
import com.nghiant.identityservice.dto.response.AuthenticationResponse;
import com.nghiant.identityservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping
  ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
    ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(AuthenticationResponse.builder()
        .authenticated(authenticationService.authenticated(request))
        .build());
    return apiResponse;
  }
}
