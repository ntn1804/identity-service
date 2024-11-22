package com.nghiant.identityservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException{

  public AppException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  private ErrorCode errorCode;
}
