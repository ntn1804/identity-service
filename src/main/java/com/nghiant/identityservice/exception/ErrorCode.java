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
  INVALID_USERNAME(400, "Username must be at least 5 characters", HttpStatus.BAD_REQUEST),
  INVALID_PASSWORD(400, "Password must be at least 3 characters", HttpStatus.BAD_REQUEST),
  INVALID_MESSAGE_KEY(400, "Invalid message key", HttpStatus.BAD_REQUEST),
  UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),
  ;

//  ErrorCode(int code, String message, HttpStatusCode statusCode) {
//    this.code = code;
//    this.message = message;
//    this.statusCode = statusCode;
//  }

  private final int code;
  private final String message;
  private final HttpStatusCode statusCode;

}
