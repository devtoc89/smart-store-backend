package com.smartstore.api.v1.gateway.admin.categorynode.dto;

import org.hibernate.validator.constraints.UUID;

import com.smartstore.api.v1.domain.category.vo.CategoryNodeVO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminCategoryNodePatchRequestDTO {
  @Schema(description = "카테고리명 (2글자 이상 입력)", example = "전자제품")
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  private String name;

  @UUID
  @Schema(description = "카테고리(중) ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryL1Id;

  public CategoryNodeVO toVO() {
    return CategoryNodeVO.builder()
        .name(name)
        .categoryL2Id(java.util.UUID.fromString(categoryL1Id))
        .build();
  }
}
