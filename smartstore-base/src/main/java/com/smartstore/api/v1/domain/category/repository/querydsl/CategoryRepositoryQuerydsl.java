package com.smartstore.api.v1.domain.category.repository.querydsl;

import java.util.List;

import com.smartstore.api.v1.domain.category.entity.CategoryL1;

public interface CategoryRepositoryQuerydsl {
  List<CategoryL1> fetchCategoryTree();
}
