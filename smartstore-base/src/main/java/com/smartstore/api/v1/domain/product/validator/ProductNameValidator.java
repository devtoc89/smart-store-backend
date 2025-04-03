package com.smartstore.api.v1.domain.product.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductNameValidator implements ConstraintValidator<ProductNameValid, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || (value.length() >= 1 && value.length() <= 40);
  }
}