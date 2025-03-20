package com.smartstore.api.v1.application.admin.product.dto.base;

import com.smartstore.api.v1.common.utils.StringUtils;
import com.smartstore.api.v1.domain.product.vo.ProductVO;

public interface AdminProductUpsertRequestDTOIF {
  String getName();

  Integer getPrice();

  String getCategoryId();

  default ProductVO toVO() {
    return ProductVO.builder()
        .name(getName())
        .price(getPrice())
        .categoryId(StringUtils.stringToUUID(getCategoryId()))
        .build();
  }
}
