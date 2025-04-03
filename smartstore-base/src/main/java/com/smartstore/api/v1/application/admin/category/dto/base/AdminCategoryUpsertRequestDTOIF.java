package com.smartstore.api.v1.application.admin.category.dto.base;

import java.util.Optional;
import java.util.UUID;

import com.smartstore.api.v1.domain.category.vo.CategoryVO;

public interface AdminCategoryUpsertRequestDTOIF {
  String getName();

  Integer getOrderBy();

  // Integer getLevel();

  String getParentId();

  default CategoryVO toVO() {
    return CategoryVO.builder()
        .name(getName())
        .orderBy(getOrderBy())
        // .level(getLevel())
        .parentId(Optional.ofNullable(getParentId()).map(UUID::fromString).orElse(null))
        .build();
  }
}
