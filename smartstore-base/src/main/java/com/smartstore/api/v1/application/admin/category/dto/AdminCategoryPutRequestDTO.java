package com.smartstore.api.v1.application.admin.category.dto;

import com.smartstore.api.v1.application.admin.category.dto.base.AdminCategoryUpsertRequestDTOIF;
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
public class AdminCategoryPutRequestDTO implements AdminCategoryUpsertRequestDTOIF {
  @NotBlank(message = "카테고리명은 필수 입력값입니다.")
  @CategoryNameValid
  @Schema(description = "카테고리명", example = "전자제품")
  private String name;

  @Builder.Default
  @Schema(description = "1차 카테고리 정렬 순서서", example = "2")
  private Integer orderBy = -1;

  // @Schema(description = "카테고리 레벨", example = "2")
  // private Integer level;

  @Schema(description = "카테고리 부모 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private String parentId;
}
