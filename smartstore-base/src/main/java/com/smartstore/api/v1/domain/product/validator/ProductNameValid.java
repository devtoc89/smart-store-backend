package com.smartstore.api.v1.domain.product.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ProductNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ProductNameValid {
  String message() default "상품명은 1자 이상 40자 이하여야 합니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}