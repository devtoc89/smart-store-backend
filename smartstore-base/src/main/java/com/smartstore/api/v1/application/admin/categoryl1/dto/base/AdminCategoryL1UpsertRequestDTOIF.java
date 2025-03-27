package com.smartstore.api.v1.application.admin.categoryl1.dto.base;

import com.smartstore.api.v1.domain.category.vo.CategoryL1VO;

public interface AdminCategoryL1UpsertRequestDTOIF {
  String getName();

  Integer getOrderBy();

  default CategoryL1VO toVO() {
    return CategoryL1VO.builder()
        .name(getName())
        .orderBy(getOrderBy())
        .build();
  }
}
