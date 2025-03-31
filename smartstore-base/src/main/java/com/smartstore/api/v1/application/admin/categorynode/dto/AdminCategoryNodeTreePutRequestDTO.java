package com.smartstore.api.v1.application.admin.categorynode.dto;

import org.hibernate.validator.constraints.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@Schema(description = "말단 카테고리 트리 변경 요청 DTO")
public class AdminCategoryNodeTreePutRequestDTO {

  @UUID
  @Schema(description = "카테고리 ID(신규 등록 시 빈값)", example = "660e8400-e29b-41d4-a716-446655440001")
  private String id;

  @NotBlank(message = "상품명은 필수 입력값입니다.")
  @Schema(description = "카테고리명", example = "전자제품")
  private String name;

  @Builder.Default
  @Schema(description = "말단 카테고리 정렬 순서", example = "2")
  private Integer orderBy = -1;

}