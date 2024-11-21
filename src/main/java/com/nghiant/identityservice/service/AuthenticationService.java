package com.nghiant.identityservice.service;

import com.nghiant.identityservice.dto.request.AuthenticationRequest;

public interface AuthenticationService {

  boolean authenticated(AuthenticationRequest request);
}
