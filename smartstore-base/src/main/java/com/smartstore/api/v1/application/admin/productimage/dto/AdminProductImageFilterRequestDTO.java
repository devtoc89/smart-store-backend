package com.smartstore.api.v1.application.admin.productimage.dto;

import com.smartstore.api.v1.common.dto.AdminFilterRequestBaseDTO;
import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.domain.product.vo.ProductImageFilterConditionVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "상품 이미지 조건 조회 DTO")
public class AdminProductImageFilterRequestDTO extends AdminFilterRequestBaseDTO {

  public ProductImageFilterConditionVO toSearchConditionVO(String productId) {
    return ProductImageFilterConditionVO.builder()
        .base(toBaseFilterConditionVO())
        .productId(StringUtil.stringToUUID(productId))
        .build();
  }
}
