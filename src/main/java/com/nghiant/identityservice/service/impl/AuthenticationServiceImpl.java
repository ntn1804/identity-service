package com.nghiant.identityservice.service.impl;

import com.nghiant.identityservice.dto.request.AuthenticationRequest;
import com.nghiant.identityservice.dto.request.IntrospectRequest;
import com.nghiant.identityservice.dto.request.LogoutRequest;
import com.nghiant.identityservice.dto.request.RefreshRequest;
import com.nghiant.identityservice.dto.response.AuthenticationResponse;
import com.nghiant.identityservice.dto.response.IntrospectResponse;
import com.nghiant.identityservice.entity.InvalidatedToken;
import com.nghiant.identityservice.entity.User;
import com.nghiant.identityservice.exception.AppException;
import com.nghiant.identityservice.exception.ErrorCode;
import com.nghiant.identityservice.repository.InvalidatedTokenRepository;
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
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

  private final InvalidatedTokenRepository invalidatedTokenRepository;
  private final UserRepository userRepository;

  @Value("${jwt.signerKey}")
  protected String signerKey;

  @Value("${jwt.valid-duration}")
  protected long VALID_DURATION;

  @Value("${jwt.refreshable-duration}")
  protected long REFRESHABLE_DURATION;


  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    User user = userRepository.findUserByUsername(request.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    authenticationResponse.setAuthenticated(
        passwordEncoder.matches(request.getPassword(), user.getPassword()));

    String token = generateToken(user);
    authenticationResponse.setToken(token);

    return authenticationResponse;
  }

  @Override
  public IntrospectResponse introspectToken(IntrospectRequest request)
      throws JOSEException, ParseException {

    String token = request.getToken();
    boolean isValid = true;

    try {
      verifyToken(token, false);
    } catch (AppException exception) {
      isValid = false;
    }

    return IntrospectResponse.builder()
        .valid(isValid)
        .build();
  }

  @Override
  public void logout(LogoutRequest request) throws ParseException, JOSEException {
    try {
      SignedJWT signedJWT = verifyToken(request.getToken(), true);

      Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();
      String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

      InvalidatedToken invalidatedToken = InvalidatedToken.builder()
          .jwtId(jwtId)
          .expiredTime(expiredTime)
          .build();

      invalidatedTokenRepository.save(invalidatedToken);
    } catch (AppException exception) {
      log.warn("Token already expired");
    }
  }

  @Override
  public AuthenticationResponse refreshToken(RefreshRequest request)
      throws ParseException, JOSEException {

    // Parse information from old token
    SignedJWT signedJWT = verifyToken(request.getToken(), true);

    String username = signedJWT.getJWTClaimsSet().getSubject();
    Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();

    User user = userRepository.findUserByUsername(username)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    // Create new token
    String token = generateToken(user);

    // Delete old token
    String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

    InvalidatedToken invalidatedToken = InvalidatedToken.builder()
        .jwtId(jwtId)
        .expiredTime(expiredTime)
        .build();

    invalidatedTokenRepository.save(invalidatedToken);

    return AuthenticationResponse.builder()
        .token(token)
        .authenticated(true)
        .build();
  }

  private SignedJWT verifyToken(String token, boolean isRefresh)
      throws JOSEException, ParseException {
    // Create jwsVerifier instance based on signerKey byte[]
    JWSVerifier jwsVerifier = new MACVerifier(signerKey.getBytes());

    // Parse data from token
    SignedJWT signedJWT = SignedJWT.parse(token);

    Date expiredTime = (isRefresh)
        ? new Date(
        signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plusSeconds(REFRESHABLE_DURATION)
            .toEpochMilli())
        : signedJWT.getJWTClaimsSet().getExpirationTime();

    boolean verified = signedJWT.verify(jwsVerifier);

    if (!(expiredTime.after(new Date()) && verified)) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    return signedJWT;
  }

  private String generateToken(User user) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(user.getUsername())
        .issuer("nghiant.com").issueTime(new Date())
        .expirationTime(
            new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
        .claim("scope", buildScope(user))
        .jwtID(UUID.randomUUID().toString()).build();

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

    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles().forEach(role -> {
        stringJoiner.add("ROLE_" + role.getName());
        if (!CollectionUtils.isEmpty(role.getPermissions())) {
          role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
        }
      });
    }
    return stringJoiner.toString();
  }
}
