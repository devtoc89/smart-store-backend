package com.smartstore.api.v1.common.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.smartstore.api.v1.application.admin.product.dto.AdminProductFilterRequestDTO;
import com.smartstore.api.v1.common.constants.enums.ActiveStatus;
import com.smartstore.api.v1.common.utils.date.DateUtil;
import com.smartstore.api.v1.domain.common.vo.BaseFilterConditionVO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class AdminFilterRequestBaseDTOTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  AdminProductFilterRequestDTO makeBuilderWithBase() {
    return AdminProductFilterRequestDTO.builder()
        .id(List.of("550e8400-e29b-41d4-a716-446655440000", "660e8400-e29b-41d4-a716-446655440001"))
        .from("2025-03-01 00:00:00")
        .to("2025-03-15 23:59:59")
        .activeStatus(ActiveStatus.ACTIVE)
        .build();
  }

  @Test
  void shouldMakeDTOProperly() {
    // Given
    AdminFilterRequestBaseDTO requestDTO = AdminFilterRequestBaseDTO.builder()
        .id(List.of("550e8400-e29b-41d4-a716-446655440000", "660e8400-e29b-41d4-a716-446655440001"))
        .from("2025-03-01 00:00:00")
        .to("2025-03-15 23:59:59")
        .activeStatus(ActiveStatus.ACTIVE)
        .build();

    // When
    Set<ConstraintViolation<AdminFilterRequestBaseDTO>> violations = validator.validate(requestDTO);

    // Then
    assertThat(violations).isEmpty();
  }

  @Test
  void toBaseFilterConditionVO_shouldConvertProperly() {
    // Given
    AdminFilterRequestBaseDTO requestDTO = AdminFilterRequestBaseDTO.builder()
        .id(List.of("550e8400-e29b-41d4-a716-446655440000", "660e8400-e29b-41d4-a716-446655440001"))
        .from("2025-03-01 00:00:00")
        .to("2025-03-15 23:59:59")
        .activeStatus(ActiveStatus.ACTIVE)
        .build();

    // When
    BaseFilterConditionVO result = requestDTO.toBaseFilterConditionVO();

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getFromDate()).isEqualTo(DateUtil.parseWithDefaultZone("2025-03-01 00:00:00"));
    assertThat(result.getToDate()).isEqualTo(DateUtil.parseWithDefaultZone("2025-03-15 23:59:59"));
    assertThat(result.getIsDeleted()).isFalse();
    assertThat(result.getIds()).containsExactly(
        UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        UUID.fromString("660e8400-e29b-41d4-a716-446655440001"));
  }

  @Test
  void toBaseFilterConditionVO_shouldHandleNullIds() {
    // Given
    AdminFilterRequestBaseDTO requestDTO = AdminFilterRequestBaseDTO.builder()
        .from("2025-03-01 00:00:00")
        .to("2025-03-15 23:59:59")
        .activeStatus(ActiveStatus.ACTIVE)
        .build();

    // When
    BaseFilterConditionVO result = requestDTO.toBaseFilterConditionVO();

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getFromDate()).isEqualTo(DateUtil.parseWithDefaultZone("2025-03-01 00:00:00"));
    assertThat(result.getToDate()).isEqualTo(DateUtil.parseWithDefaultZone("2025-03-15 23:59:59"));
    assertThat(result.getIsDeleted()).isFalse();
    assertThat(result.getIds()).isNull();
  }

  @Test
  void toBaseFilterConditionVO_shouldIgnoreInvalidUUIDs() {
    // Given
    AdminFilterRequestBaseDTO requestDTO = AdminFilterRequestBaseDTO.builder()
        .id(List.of("550e8400-e29b-41d4-a716-446655440000", "INVALID_UUID"))
        .from("2025-03-01 00:00:00")
        .to("2025-03-15 23:59:59")
        .activeStatus(ActiveStatus.ACTIVE)
        .build();

    // When
    BaseFilterConditionVO result = requestDTO.toBaseFilterConditionVO();

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getIds()).containsExactly(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
  }

  @Test
  void validDateTime_shouldPassValidation() {
    // Given
    AdminFilterRequestBaseDTO dto = AdminFilterRequestBaseDTO.builder()
        .from("2025-03-01 00:00:00")
        .to("2025-03-15 23:59:59")
        .id(List.of("550e8400-e29b-41d4-a716-446655440000"))
        .activeStatus(ActiveStatus.ACTIVE)
        .build();

    // Whens
    Set<ConstraintViolation<AdminFilterRequestBaseDTO>> violations = validator.validate(dto);

    // Then
    assertTrue(violations.isEmpty());
  }

  @Test
  void invalidDateTime_shouldFailValidation() {
    // Given
    AdminFilterRequestBaseDTO dto = AdminFilterRequestBaseDTO.builder()
        .from("INVALID_DATE")
        .to("2025-03-15 23:59:59")
        .build();

    // When
    Set<ConstraintViolation<AdminFilterRequestBaseDTO>> violations = validator.validate(dto);

    // Then
    assertFalse(violations.isEmpty());
    var fromViolation = violations.stream().filter(v -> String.valueOf(v.getPropertyPath()).equals("from"))
        .findFirst().get();
    assertThat(fromViolation.getMessage())
        .isEqualTo("시작 날짜 형식이 올바르지 않습니다. (yyyy-MM-dd HH:mm:ss)");
  }
}
