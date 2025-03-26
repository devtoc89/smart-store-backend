package com.smartstore.api.v1.common.utils.exception;

import org.springframework.validation.FieldError;

import com.smartstore.api.v1.common.constants.message.CommonMessage;

import jakarta.validation.ConstraintViolation;

public class ExceptionUtil {
  static final String FIELD_ERROR_FORMAT = "%s: %s (입력값: %s)";

  private ExceptionUtil() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
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
