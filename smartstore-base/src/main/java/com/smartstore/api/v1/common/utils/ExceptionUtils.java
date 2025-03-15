package com.smartstore.api.v1.common.utils;

import org.springframework.validation.FieldError;

import jakarta.validation.ConstraintViolation;

public class ExceptionUtils {
  static final String FIELD_ERROR_FORMAT = "%s: %s (입력값: %s)";

  private ExceptionUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  // 필드 에러 메시지 포맷
  public static String formatFieldError(FieldError fieldError) {
    return String.format(FIELD_ERROR_FORMAT,
        fieldError.getField(),
        fieldError.getDefaultMessage(),
        fieldError.getRejectedValue());
  }

  // ConstraintViolation 메시지 포맷
  public static String formatConstraintViolation(ConstraintViolation<?> violation) {
    return String.format(FIELD_ERROR_FORMAT,
        violation.getPropertyPath().toString(),
        violation.getMessage(),
        violation.getInvalidValue());
  }

}
