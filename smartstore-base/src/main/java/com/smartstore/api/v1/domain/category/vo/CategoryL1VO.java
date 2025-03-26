package com.smartstore.api.v1.domain.category.vo;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.common.domain.vo.BaseEntityVO;
import com.smartstore.api.v1.domain.category.entity.CategoryL1;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryL1VO {
  private final BaseEntityVO base;
  private final String name;
  private final Integer orderBy;

  public static CategoryL1VO fromEntity(CategoryL1 categoryL1) {
    return CategoryL1VO.builder()
        .base(BaseEntityVO.fromEntity(categoryL1))
        .name(categoryL1.getName())
        .orderBy(categoryL1.getOrderBy())
        .build();
  }

  public static Page<CategoryL1VO> fromEntityWithPage(Page<CategoryL1> categoryL1) {
    return categoryL1.map(v -> fromEntity(v));
  }
}
