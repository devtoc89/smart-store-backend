package com.smartstore.api.v1.application.admin.categorynode.dto.base;

import com.smartstore.api.v1.common.utils.StringUtils;
import com.smartstore.api.v1.domain.category.vo.CategoryNodeVO;

public interface AdminCategoryNodeUpsertRequestDTOIF {
  String getName();

  String getCategoryL2Id();

  default CategoryNodeVO toVO() {
    return CategoryNodeVO.builder()
        .name(getName())
        .categoryL2Id(StringUtils.stringToUUID(getCategoryL2Id()))
        .build();
  }
}
