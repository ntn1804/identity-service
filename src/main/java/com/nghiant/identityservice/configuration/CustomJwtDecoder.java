package com.nghiant.identityservice.configuration;

import com.nghiant.identityservice.dto.request.IntrospectRequest;
import com.nghiant.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomJwtDecoder implements JwtDecoder {

  private final AuthenticationService authenticationService;
  private NimbusJwtDecoder nimbusJwtDecoder = null;

  @Value("${jwt.signerKey}")
  private String signerKey;

  @Override
  public Jwt decode(String token) throws JwtException {
    try {
      var response = authenticationService.introspectToken(
          IntrospectRequest.builder().token(token).build());
      if (!response.isValid()) {
        throw new JwtException("Invalid Token");
      }
    } catch (ParseException | JOSEException exception) {
      throw new JwtException(exception.getMessage());
    }

    if (Objects.isNull(nimbusJwtDecoder)) {
      SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
      nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
          .macAlgorithm(MacAlgorithm.HS512)
          .build();
    }

    return nimbusJwtDecoder.decode(token);
  }
}
