package com.smartstore.api.v1.common.validation.date;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateTimeValidator implements ConstraintValidator<ValidDateTime, String> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return true; // null 또는 빈 값은 허용
    }
    try {
      FORMATTER.parse(value);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
