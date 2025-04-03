package com.smartstore.api.v1.domain.category.vo;

import java.util.List;

import com.smartstore.api.v1.domain.category.entity.Category;
import com.smartstore.api.v1.domain.common.wrapper.DataWithChild;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CategoryNestVO {
  private final List<CategoryL1Nest> categoryL1Nest;

  public static CategoryNestVO fromEntityWithList(
      List<DataWithChild<Category, DataWithChild<Category, Category>>> entities) {
    return new CategoryNestVO(entities.stream().map(CategoryL1Nest::fromEntity).toList());
  }

  @Getter
  @RequiredArgsConstructor
  public static class CategoryL1Nest {
    private final CategoryVO category;
    private final List<CategoryL2Nest> children;

    public static CategoryL1Nest fromEntity(DataWithChild<Category, DataWithChild<Category, Category>> entity) {
      return new CategoryL1Nest(CategoryVO.fromEntity(entity.getData()),
          entity.getChildren().stream()
              .map(CategoryL2Nest::fromEntity)
              .toList());
    }

    @Getter
    @RequiredArgsConstructor
    public static class CategoryL2Nest {
      private final CategoryVO category;
      private final List<CategoryL3Nest> children;

      public static CategoryL2Nest fromEntity(DataWithChild<Category, Category> entity) {
        return new CategoryL2Nest(
            CategoryVO.fromEntity(entity.getData()),
            entity.getChildren().stream()
                .map(CategoryL3Nest::fromEntity)
                .toList());
      }

      @Getter
      @RequiredArgsConstructor
      public static class CategoryL3Nest {
        private final CategoryVO category;

        public static CategoryL3Nest fromEntity(Category entity) {
          return new CategoryL3Nest(CategoryVO.fromEntity(entity));
        }
      }
    }

  }
}
