package com.smartstore.api.v1.application.admin.categorynode.dto;

import org.hibernate.validator.constraints.UUID;

import com.smartstore.api.v1.application.admin.categorynode.dto.base.AdminCategoryNodeUpsertRequestDTOIF;

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
@Schema(description = "말단 카테고리 일부 수정 DTO")
public class AdminCategoryNodePatchRequestDTO implements AdminCategoryNodeUpsertRequestDTOIF {
  @Schema(description = "카테고리명 (2글자 이상 입력)", example = "전자제품")
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  private String name;

  @Schema(description = "말단 카테고리 정렬 순서서", example = "2")
  private Integer orderBy;

  @UUID
  @Schema(description = "2차 카테고리 ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryL2Id;

}
