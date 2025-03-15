package com.smartstore.api.v1.common.utils;

import java.util.Set;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidationUtil {
  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = factory.getValidator();

  private ValidationUtil() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static Validator getValidator() {
    return validator;
  }

  public static <T> void validate(T object) throws BindException {
    Set<ConstraintViolation<T>> violations = validator.validate(object);

    if (!violations.isEmpty()) {
      BindingResult bindingResult = new BeanPropertyBindingResult(object, object.getClass().getSimpleName());

      for (ConstraintViolation<T> violation : violations) {
        String field = violation.getPropertyPath().toString();
        String message = violation.getMessage();
        bindingResult.addError(new FieldError(object.getClass().getSimpleName(), field, message));
      }

      throw new BindException(bindingResult);
    }
  }
}