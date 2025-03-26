package com.smartstore.api.v1.application.admin.product.dto;

import java.util.List;

import com.smartstore.api.v1.application.admin.product.dto.base.AdminProductUpsertRequestDTOIF;

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
@Schema(description = "상품 일부 수정 DTO")
public class AdminProductPatchRequestDTO implements AdminProductUpsertRequestDTOIF {
  @Schema(description = "상품명 (2글자 이상 입력)", example = "사과")
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  private String name;

  @Schema(description = "상품 가격", example = "1000")
  private Integer price;

  @Schema(description = "카테고리 말단 ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryId;

  @Schema(description = "첨부 이미지 ", example = "")
  private List<AdminProductWithImageDTO> images;

}
