package com.smartstore.api.v1.gateway.admin.categoryl2.dto;

import org.hibernate.validator.constraints.UUID;

import com.smartstore.api.v1.domain.category.vo.CategoryL2VO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminCategoryL2PostRequestDTO {
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  @NotBlank(message = "상품명은 필수 입력값입니다.")
  @Schema(description = "카테고리명 (2글자 이상 입력)", example = "전자제품")
  private String name;

  @UUID
  @NotBlank(message = "카테고리(대) ID는 필수 입력값입니다.")
  @Schema(description = "카테고리(대) ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryL1Id;

  public CategoryL2VO toVO() {
    return CategoryL2VO.builder()
        .name(name)
        .categoryL1Id(java.util.UUID.fromString(categoryL1Id))
        .build();
  }
}
