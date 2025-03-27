package com.smartstore.api.v1.application.admin.categorynode.dto.base;

import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.domain.category.vo.CategoryNodeVO;

public interface AdminCategoryNodeUpsertRequestDTOIF {
  String getName();

  Integer getOrderBy();

  String getCategoryL2Id();

  default CategoryNodeVO toVO() {
    return CategoryNodeVO.builder()
        .name(getName())
        .orderBy(getOrderBy())
        .categoryL2Id(StringUtil.stringToUUID(getCategoryL2Id()))
        .build();
  }
}
