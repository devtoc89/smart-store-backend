package com.smartstore.api.v1.common.validation.date;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateTimeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateTime {
  String message() default "날짜 형식이 올바르지 않습니다. (yyyy-MM-dd HH:mm:ss)";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
