package com.smartstore.api.v1.gateway.admin.categoryl1.dto;

import com.smartstore.api.v1.domain.category.vo.CategoryL1VO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminCategoryL1PatchRequestDTO {
  @Schema(description = "카테고리명 (2글자 이상 입력)", example = "전자제품")
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  private String name;

  public CategoryL1VO toVO() {
    return CategoryL1VO.builder()
        .name(name)
        .build();
  }
}
