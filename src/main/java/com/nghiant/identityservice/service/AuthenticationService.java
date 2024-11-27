package com.nghiant.identityservice.service;

import com.nghiant.identityservice.dto.request.AuthenticationRequest;
import com.nghiant.identityservice.dto.request.IntrospectRequest;
import com.nghiant.identityservice.dto.request.LogoutRequest;
import com.nghiant.identityservice.dto.response.AuthenticationResponse;
import com.nghiant.identityservice.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;

public interface AuthenticationService {

  AuthenticationResponse authenticate(AuthenticationRequest request);
  IntrospectResponse introspectToken(IntrospectRequest request) throws JOSEException, ParseException;
  void logout(LogoutRequest request) throws ParseException, JOSEException;
}
