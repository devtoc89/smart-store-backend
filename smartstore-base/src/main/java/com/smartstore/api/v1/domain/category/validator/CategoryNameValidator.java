package com.smartstore.api.v1.domain.category.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryNameValidator implements ConstraintValidator<CategoryNameValid, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || (value.length() >= 1 && value.length() <= 20);
  }
}