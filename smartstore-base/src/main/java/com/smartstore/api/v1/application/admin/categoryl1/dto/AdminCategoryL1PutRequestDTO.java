package com.smartstore.api.v1.application.admin.categoryl1.dto;

import com.smartstore.api.v1.application.admin.categoryl1.dto.base.AdminCategoryL1UpsertRequestDTOIF;
import com.smartstore.api.v1.domain.category.validator.CategoryNameValid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Schema(description = "1차 카테고리 수정 DTO")
public class AdminCategoryL1PutRequestDTO implements AdminCategoryL1UpsertRequestDTOIF {
  @NotBlank(message = "카테고리명은 필수 입력값입니다.")
  @CategoryNameValid
  @Schema(description = "카테고리명", example = "전자제품")
  private String name;

  @Builder.Default
  @Schema(description = "1차 카테고리 정렬 순서서", example = "2")
  private Integer orderBy = -1;
}
