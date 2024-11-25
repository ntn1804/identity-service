package com.nghiant.identityservice.service.impl;

import com.nghiant.identityservice.dto.request.AuthenticationRequest;
import com.nghiant.identityservice.dto.request.IntrospectRequest;
import com.nghiant.identityservice.dto.response.AuthenticationResponse;
import com.nghiant.identityservice.dto.response.IntrospectResponse;
import com.nghiant.identityservice.entity.User;
import com.nghiant.identityservice.exception.AppException;
import com.nghiant.identityservice.exception.ErrorCode;
import com.nghiant.identityservice.repository.UserRepository;
import com.nghiant.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;

  @Value("${jwt.signerKey}")
  protected String signerKey;

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    User user = userRepository.findUserByUsername(request.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    authenticationResponse.setAuthenticated(
        passwordEncoder.matches(request.getPassword(), user.getPassword()));

    String token = generateToken(user);
    authenticationResponse.setToken(token);

    return authenticationResponse;
  }

  @Override
  public IntrospectResponse verifyToken(IntrospectRequest request) throws JOSEException, ParseException {
    String token = request.getToken();

    // Create jwsVerifier instance based on signerKey byte[]
    JWSVerifier jwsVerifier = new MACVerifier(signerKey.getBytes());

    // Parse data from token
    SignedJWT signedJWT = SignedJWT.parse(token);

    Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();

    boolean verified = signedJWT.verify(jwsVerifier);

    return IntrospectResponse.builder()
        .valid(verified && expiredTime.after(new Date()))
        .build();
  }

  private String generateToken(User user) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(user.getUsername())
        .issuer("nghiant.com").issueTime(new Date())
        .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
        .claim("scope", buildScope(user)).build();

    Payload payload = new Payload(claimsSet.toJSONObject());

    JWSObject jwsObject = new JWSObject(header, payload);

    try {
      MACSigner macSigner = new MACSigner(signerKey.getBytes());
      jwsObject.sign(macSigner);
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private String buildScope(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");
//    user.getRoles().forEach(stringJoiner::add);
    return stringJoiner.toString();
  }
}
