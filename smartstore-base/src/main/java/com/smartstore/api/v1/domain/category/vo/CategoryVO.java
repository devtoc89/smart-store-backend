package com.smartstore.api.v1.domain.category.vo;

import java.util.List;

import com.smartstore.api.v1.domain.category.entity.CategoryL1;
import com.smartstore.api.v1.domain.category.entity.CategoryL2;
import com.smartstore.api.v1.domain.category.entity.CategoryNode;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CategoryVO {
  private final List<CategoryL1Nest> categoryL1Nest;

  public static CategoryVO fromEntityWithList(List<CategoryL1> entities) {
    return new CategoryVO(entities.stream().map(CategoryL1Nest::fromEntity).toList());
  }

  @Getter
  @RequiredArgsConstructor
  public static class CategoryL1Nest {
    private final CategoryL1VO categoryL1VO;
    private final List<CategoryL2Nest> children;

    public static CategoryL1Nest fromEntity(CategoryL1 entity) {
      return new CategoryL1Nest(CategoryL1VO.fromEntity(entity),
          entity.getSubCategories().stream()
              .map(CategoryL2Nest::fromEntity)
              .toList());
    }

    @Getter
    @RequiredArgsConstructor
    public static class CategoryL2Nest {
      private final CategoryL2VO categoryL2VO;
      private final List<CategoryNodeNest> children;

      public static CategoryL2Nest fromEntity(CategoryL2 entity) {
        return new CategoryL2Nest(
            CategoryL2VO.fromEntity(entity),
            entity.getSubCategories().stream()
                .map(CategoryNodeNest::fromEntity)
                .toList());
      }

      @Getter
      @RequiredArgsConstructor
      public static class CategoryNodeNest {
        private final CategoryNodeVO categoryNodeVO;

        public static CategoryNodeNest fromEntity(CategoryNode entity) {
          return new CategoryNodeNest(CategoryNodeVO.fromEntity(entity));
        }
      }
    }

  }
}
