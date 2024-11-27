package com.nghiant.identityservice.controller;

import com.nghiant.identityservice.dto.request.AuthenticationRequest;
import com.nghiant.identityservice.dto.request.IntrospectRequest;
import com.nghiant.identityservice.dto.request.LogoutRequest;
import com.nghiant.identityservice.dto.response.ApiResponse;
import com.nghiant.identityservice.dto.response.AuthenticationResponse;
import com.nghiant.identityservice.dto.response.IntrospectResponse;
import com.nghiant.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
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

  @PostMapping("/login")
  ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
    ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(authenticationService.authenticate(request));
    return apiResponse;
  }

  @PostMapping("/verify-token")
  ApiResponse<IntrospectResponse> introspectToken(@RequestBody IntrospectRequest request)
      throws ParseException, JOSEException {
    ApiResponse<IntrospectResponse> apiResponse = new ApiResponse<>();
    apiResponse.setResult(authenticationService.introspectToken(request));
    return apiResponse;
  }

  @PostMapping("/logout")
  ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    authenticationService.logout(request);
    return apiResponse;
  }
}
