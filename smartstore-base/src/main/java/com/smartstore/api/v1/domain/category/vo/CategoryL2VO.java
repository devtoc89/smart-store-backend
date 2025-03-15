package com.smartstore.api.v1.domain.category.vo;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.common.domain.vo.BaseEntityVO;
import com.smartstore.api.v1.domain.category.entity.CategoryL2;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryL2VO {
  private final BaseEntityVO base;
  private final String name;
  private final UUID categoryL1Id;

  public static CategoryL2VO fromEntity(CategoryL2 categoryL2) {
    return CategoryL2VO.builder()
        .base(BaseEntityVO.fromEntity(categoryL2))
        .name(categoryL2.getName())
        .categoryL1Id(categoryL2.getCategoryL1().getId())
        .build();
  }

  public static Page<CategoryL2VO> fromEntityWithPage(Page<CategoryL2> categoryL2) {
    return categoryL2.map(v -> fromEntity(v));
  }
}
