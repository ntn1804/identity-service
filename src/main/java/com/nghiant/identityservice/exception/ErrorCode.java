package com.nghiant.identityservice.exception;

public enum ErrorCode {
  USER_EXISTED(4000, "User existed"),
  USER_NOT_FOUND(4000, "User not found"),
  UNCATEGORIZED_ERROR(9999, "Uncategorized error"),
  INVALID_USERNAME(4000, "Username must be at least 5 characters"),
  INVALID_PASSWORD(4000, "Password must be at least 3 characters"),
  INVALID_MESSAGE_KEY(4000, "Invalid message key");

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  private int code;
  private String message;

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
