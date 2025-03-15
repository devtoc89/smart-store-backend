package com.smartstore.api.v1.common.validation.uuid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.UUID;

public class UUIDListValidator implements ConstraintValidator<ValidUUIDList, List<String>> {

  @Override
  public boolean isValid(List<String> ids, ConstraintValidatorContext context) {
    if (ids == null || ids.isEmpty()) {
      return true; // `null` 또는 빈 리스트 허용
    }

    for (String id : ids) {
      try {
        UUID.fromString(id); // UUID 변환 가능 여부 체크
      } catch (IllegalArgumentException e) {
        return false; // 변환 실패 시 유효하지 않음
      }
    }
    return true;
  }
}
