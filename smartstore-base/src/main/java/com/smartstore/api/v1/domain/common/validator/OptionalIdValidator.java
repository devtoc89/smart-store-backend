package com.smartstore.api.v1.domain.common.validator;

import java.util.UUID;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalIdValidator implements ConstraintValidator<OptionalIdValid, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // null 또는 빈 문자열이면 통과
    if (value == null || value.trim().isEmpty()) {
      return true;
    }

    // UUID 형식인지 확인
    try {
      UUID.fromString(value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}