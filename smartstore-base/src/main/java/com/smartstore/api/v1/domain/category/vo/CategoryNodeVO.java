package com.smartstore.api.v1.domain.category.vo;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.common.domain.vo.BaseEntityVO;
import com.smartstore.api.v1.domain.category.entity.CategoryNode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryNodeVO {
  private final BaseEntityVO base;
  private final String name;
  private final UUID categoryL2Id;
  private final Integer orderBy;

  public static CategoryNodeVO fromEntity(CategoryNode categoryNode) {
    return CategoryNodeVO.builder()
        .base(BaseEntityVO.fromEntity(categoryNode))
        .name(categoryNode.getName())
        .categoryL2Id(categoryNode.getCategoryL2().getId())
        .orderBy(categoryNode.getOrderBy())
        .build();
  }

  public static Page<CategoryNodeVO> fromEntityWithPage(Page<CategoryNode> categoryNode) {
    return categoryNode.map(v -> fromEntity(v));
  }
}
