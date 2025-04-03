package com.smartstore.api.v1.application.admin.category.dto.tree;

import java.util.List;

import com.smartstore.api.v1.domain.category.validator.CategoryNameValid;
import com.smartstore.api.v1.domain.common.validator.OptionalIdValid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
@Schema(description = "2차 카테고리 트리 변경 요청 DTO")
public class AdminCategoryL2TreePutRequestDTO {

  @OptionalIdValid
  @Schema(description = "카테고리 ID(신규 등록 시 빈값)", example = "660e8400-e29b-41d4-a716-446655440001")
  private String id;

  @NotBlank(message = "카테고리명은 필수 입력값입니다.")
  @CategoryNameValid
  @Schema(description = "카테고리명", example = "전자제품")
  private String name;

  @Builder.Default
  @Schema(description = "카테고리 정렬 순서", example = "2")
  private Integer orderBy = -1;

  @Valid
  @Schema(description = "말단 카테고리 리스트")
  private List<AdminCategoryL3TreePutRequestDTO> children;

}