package com.nghiant.identityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  USER_EXISTED(400, "User existed", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
  UNCATEGORIZED_ERROR(999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_USERNAME(400, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
  INVALID_PASSWORD(400, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
  INVALID_MESSAGE_KEY(400, "Invalid message key", HttpStatus.BAD_REQUEST),
  UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),
  INVALID_DOB(400, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
  ;

  private final int code;
  private final String message;
  private final HttpStatusCode statusCode;

}
