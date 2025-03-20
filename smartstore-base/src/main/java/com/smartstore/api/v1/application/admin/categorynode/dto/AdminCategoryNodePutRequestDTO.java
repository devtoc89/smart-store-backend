package com.smartstore.api.v1.application.admin.categorynode.dto;

import org.hibernate.validator.constraints.UUID;

import com.smartstore.api.v1.application.admin.categorynode.dto.base.AdminCategoryNodeUpsertRequestDTOIF;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "카테고리(말단) 수정 DTO")
public class AdminCategoryNodePutRequestDTO implements AdminCategoryNodeUpsertRequestDTOIF {
  @Schema(description = "카테고리명 (2글자 이상 입력)", example = "전자제품")
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  @NotBlank(message = "카테고리명은 필수 입력값입니다.")
  private String name;

  @UUID
  @NotBlank(message = "카테고리(중) ID는 필수 입력값입니다.")
  @Schema(description = "카테고리(중) ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryL2Id;

}
