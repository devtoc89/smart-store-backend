package com.smartstore.api.v1.common.validation.uuid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UUIDListValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUIDList {
  String message() default "유효하지 않은 UUID 값이 포함되어 있습니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
