package com.smartstore.api.v1.application.admin.category.dto;

import com.smartstore.api.v1.application.admin.category.dto.base.AdminCategoryUpsertRequestDTOIF;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "카테고리 일부 수정 DTO")
public class AdminCategoryPatchRequestDTO implements AdminCategoryUpsertRequestDTOIF {
  @Schema(description = "카테고리명", example = "전자제품")
  private String name;

  @Schema(description = "카테고리 정렬 순서서", example = "2")
  private Integer orderBy;

  // @Schema(description = "카테고리 레벨", example = "2")
  // private Integer level;

  @Schema(description = "카테고리 부모 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private String parentId;
}
