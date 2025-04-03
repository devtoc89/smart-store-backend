package com.smartstore.api.v1.application.admin.product.dto;

import java.util.List;

import com.smartstore.api.v1.common.dto.AdminFilterRequestBaseDTO;
import com.smartstore.api.v1.domain.common.validator.OptionalIdValid;
import com.smartstore.api.v1.domain.product.vo.ProductFilterConditionVO;

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
@EqualsAndHashCode(callSuper = true)
@Schema(description = "상품 조건 조회 DTO")
public class AdminProductFilterRequestDTO extends AdminFilterRequestBaseDTO {

  @Schema(description = "검색어", example = "사과")
  private String keyword;

  @OptionalIdValid
  @Schema(description = "1차 카테고리 ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryL1Id;

  @OptionalIdValid
  @Schema(description = "2차 카테고리 ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryL2Id;

  @OptionalIdValid
  @Schema(description = "3차 카테고리 ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryL3Id;

  public ProductFilterConditionVO toSearchConditionVO(List<java.util.UUID> categoryIdList) {
    return ProductFilterConditionVO.builder()
        .base(toBaseFilterConditionVO())
        .name(keyword)
        .categoryIdList(categoryIdList)
        .build();
  }
}
