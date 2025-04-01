package com.smartstore.api.v1.application.admin.categoryl2.dto;

import org.hibernate.validator.constraints.UUID;

import com.smartstore.api.v1.application.admin.categoryl2.dto.base.AdminCategoryL2UpsertRequestDTOIF;
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
@Schema(description = "2차 카테고리 등록 DTO")
public class AdminCategoryL2PostRequestDTO implements AdminCategoryL2UpsertRequestDTOIF {

  @NotBlank(message = "카테고리명은 필수 입력값입니다.")
  @CategoryNameValid
  @Schema(description = "카테고리명", example = "전자제품")
  private String name;

  @Builder.Default
  @Schema(description = "2차 카테고리 정렬 순서", example = "2")
  private Integer orderBy = -1;

  @UUID
  @NotBlank(message = "1차 카테고리 ID는 필수 입력값입니다.")
  @Schema(description = "1차 카테고리 ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryL1Id;

}
