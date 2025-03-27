package com.smartstore.api.v1.application.admin.categoryl1.dto;

import com.smartstore.api.v1.application.admin.categoryl1.dto.base.AdminCategoryL1UpsertRequestDTOIF;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@Schema(description = "1차 카테고리 일부 수정 DTO")
public class AdminCategoryL1PatchRequestDTO implements AdminCategoryL1UpsertRequestDTOIF {
  @Schema(description = "카테고리명 (2글자 이상 입력)", example = "전자제품")
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  private String name;

  @Schema(description = "1차 카테고리 정렬 순서서", example = "2")
  private Integer orderBy;
}
