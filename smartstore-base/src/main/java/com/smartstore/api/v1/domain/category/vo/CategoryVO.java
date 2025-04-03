package com.smartstore.api.v1.domain.category.vo;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.domain.category.entity.Category;
import com.smartstore.api.v1.domain.common.vo.BaseEntityVO;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CategoryVO {
  private final BaseEntityVO base;
  private final String name;
  private final UUID parentId;
  private final Integer orderBy;
  private final Integer level;

  public static CategoryVO fromEntity(Category entity) {
    return CategoryVO.builder()
        .base(BaseEntityVO.fromEntity(entity))
        .name(entity.getName())
        .orderBy(entity.getOrderBy())
        .parentId(entity.getParentId())
        .level(entity.getLevel())
        .build();
  }

  public static Page<CategoryVO> fromEntityWithPage(Page<Category> entity) {
    return entity.map(v -> fromEntity(v));
  }
}
