package com.smartstore.api.v1.gateway.admin.product.dto;

import org.hibernate.validator.constraints.UUID;

import com.smartstore.api.v1.domain.product.vo.ProductVO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminProductPatchRequestDTO {

  @Schema(description = "상품명 (2글자 이상 입력)", example = "사과")
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  private String name;

  @Schema(description = "상품 가격", example = "1000")
  private Integer price;

  @UUID
  @Schema(description = "카테고리 말단 ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryId;

  public ProductVO toVO() {
    return ProductVO.builder()
        .name(name)
        .price(price)
        .categoryId(java.util.UUID.fromString(categoryId))
        .build();
  }
}
