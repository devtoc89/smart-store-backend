package com.smartstore.api.v1.application.admin.categoryl1.dto;

import com.smartstore.api.v1.application.admin.categoryl1.dto.base.AdminCategoryL1UpsertRequestDTOIF;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminCategoryL1PatchRequestDTO implements AdminCategoryL1UpsertRequestDTOIF {
  @Schema(description = "카테고리명 (2글자 이상 입력)", example = "전자제품")
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  private String name;
}
