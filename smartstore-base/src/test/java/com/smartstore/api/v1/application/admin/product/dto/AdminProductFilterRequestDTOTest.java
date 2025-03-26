package com.smartstore.api.v1.application.admin.product.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.smartstore.api.v1.common.constants.enums.ActiveStatus;
import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.domain.product.vo.ProductFilterConditionVO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class AdminProductFilterRequestDTOTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  AdminProductFilterRequestDTO makeBaseDTO() {
    return AdminProductFilterRequestDTO.builder()
        .id(List.of("550e8400-e29b-41d4-a716-446655440000", "660e8400-e29b-41d4-a716-446655440001"))
        .from("2025-03-01 00:00:00")
        .to("2025-03-15 23:59:59")
        .activeStatus(ActiveStatus.ACTIVE)
        .build();
  }

  @Test
  void creation_shouldPass() {
    // Given

    AdminProductFilterRequestDTO dto = makeBaseDTO();
    dto.setKeyword(UUID.randomUUID().toString());
    dto.setKeyword("사과");

    // When
    Set<ConstraintViolation<AdminProductFilterRequestDTO>> violations = validator.validate(dto);

    // Then
    assertThat(violations).isEmpty();
  }

  @Test
  void validation_invalidateKeyword_shouldFail() {
    // Given: 1글자 키워드는 유효성 검사에서 실패해야 함
    AdminProductFilterRequestDTO dto = makeBaseDTO();
    dto.setKeyword("A");
    dto.setCategoryId(UUID.randomUUID().toString());

    // When
    Set<ConstraintViolation<AdminProductFilterRequestDTO>> violations = validator.validate(dto);

    // Then
    assertThat(violations).isNotEmpty();
    assertThat(violations.iterator().next().getMessage()).contains("검색어는 2글자 이상 입력해야 합니다.");
  }

  @Test
  void validation_shouldPass() {
    // Given: 올바른 UUID
    var dto = makeBaseDTO();
    dto.setKeyword("사과");
    dto.setCategoryId(UUID.randomUUID().toString());

    // When
    Set<ConstraintViolation<AdminProductFilterRequestDTO>> violations = validator.validate(dto);

    // Then
    assertThat(violations).isEmpty();
  }

  @Test
  void validation_invalidateCategoryId_shouldFail() {
    // Given: 잘못된 UUID 형식
    var dto = makeBaseDTO();
    dto.setKeyword("사과");
    dto.setCategoryId("invalid-uuid");

    // When
    Set<ConstraintViolation<AdminProductFilterRequestDTO>> violations = validator.validate(dto);

    assertTrue(violations.stream().map(v -> String.valueOf(v.getPropertyPath())).filter(v -> v.equals("categoryId"))
        .findFirst()
        .isPresent());
  }

  @Test
  void toSearchConditionVO_shouldPass() {
    // Given
    var dto = makeBaseDTO();
    dto.setKeyword("사과");
    dto.setCategoryId(UUID.randomUUID().toString());

    // When
    ProductFilterConditionVO vo = dto.toSearchConditionVO();

    // Then
    assertThat(vo).isNotNull();
    assertThat(vo.getName()).isEqualTo(dto.getKeyword());
    assertThat(vo.getCategoryId()).isEqualTo(StringUtil.stringToUUID(dto.getCategoryId()));
  }

  @Test
  void toSearchConditionVO_categoryIdIsNull_shouldPass() {
    // Given
    var dto = makeBaseDTO();
    dto.setKeyword("사과");
    dto.setCategoryId(null);

    // When
    ProductFilterConditionVO vo = dto.toSearchConditionVO();

    // Then
    assertThat(vo).isNotNull();
    assertThat(vo.getCategoryId()).isNull();
  }
}
