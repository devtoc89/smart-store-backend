package com.smartstore.api.v1.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
  protected BaseException(String message) {
    super(message);
  }

  public abstract HttpStatus getStatus();
}
