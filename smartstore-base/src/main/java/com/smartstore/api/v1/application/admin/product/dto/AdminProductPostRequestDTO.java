package com.smartstore.api.v1.application.admin.product.dto;

import org.hibernate.validator.constraints.UUID;

import com.smartstore.api.v1.application.admin.product.dto.base.AdminProductUpsertRequestDTOIF;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "상품 등록 DTO")
public class AdminProductPostRequestDTO implements AdminProductUpsertRequestDTOIF {
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  @Schema(description = "상품명 (2글자 이상 입력)", example = "사과")
  @NotBlank(message = "상품명은 필수 입력값입니다.")
  private String name;

  @NotNull(message = "가격은 필수 입력값입니다.")
  @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
  @Schema(description = "상품 가격", example = "1000")
  private Integer price;

  @UUID
  @NotBlank(message = "카테고리는 필수항목입니다.")
  @Schema(description = "카테고리 말단 ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryId;

}
