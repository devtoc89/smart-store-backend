package com.smartstore.api.v1.application.admin.categoryl2.dto.base;

import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.domain.category.vo.CategoryL2VO;

public interface AdminCategoryL2UpsertRequestDTOIF {
  String getName();

  String getCategoryL1Id();

  default CategoryL2VO toVO() {
    return CategoryL2VO.builder()
        .name(getName())
        .categoryL1Id(StringUtil.stringToUUID(getCategoryL1Id()))
        .build();
  }
}
