package com.smartstore.api.v1.common.utils.validation;

import java.util.Set;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.smartstore.api.v1.common.constants.message.CommonMessage;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidationUtil {
  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = factory.getValidator();

  private ValidationUtil() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
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

  public static BindException createBindException(Object target, String field, String message) {
    BindingResult bindingResult = new BeanPropertyBindingResult(target, target.getClass().getSimpleName());
    bindingResult.addError(new FieldError(target.getClass().getSimpleName(), field, message));
    return new BindException(bindingResult);
  }
}