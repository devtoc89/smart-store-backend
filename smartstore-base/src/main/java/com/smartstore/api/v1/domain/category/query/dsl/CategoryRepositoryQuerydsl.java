package com.smartstore.api.v1.domain.category.query.dsl;

import java.util.List;

import com.smartstore.api.v1.domain.category.entity.Category;
import com.smartstore.api.v1.domain.common.wrapper.DataWithChild;

public interface CategoryRepositoryQuerydsl {
  List<DataWithChild<Category, DataWithChild<Category, Category>>> fetchCategoryTree(boolean needSort);

  List<DataWithChild<Category, DataWithChild<Category, Category>>> fetchCategoryTree(boolean needSort,
      boolean needDeleted);
}
