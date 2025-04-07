package com.smartstore.api.v1.application.admin.productimage.dto;

import org.hibernate.validator.constraints.UUID;

import com.smartstore.api.v1.application.admin.productimage.dto.base.AdminProductImageUpsertRequestDTOIF;

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
@Schema(description = "상품 이미지 수정 DTO")
public class AdminProductImagePatchRequestDTO
    implements AdminProductImageUpsertRequestDTOIF {
  @UUID
  @NotBlank(message = "상품 이미지 ID는 필수항목입니다.")
  @Schema(description = "상품 이미지 ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String id;

  @UUID
  @Schema(description = "파일 ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String fileId;

  @Schema(description = "메인 여부", example = "true")
  private Boolean isMain;

  @Builder.Default
  @Schema(description = "순서", example = "1")
  private Integer orderBy = -1;
}
