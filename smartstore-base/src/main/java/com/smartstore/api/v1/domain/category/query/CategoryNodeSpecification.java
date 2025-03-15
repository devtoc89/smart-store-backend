package com.smartstore.api.v1.domain.category.query;

import org.springframework.data.jpa.domain.Specification;
import com.smartstore.api.v1.common.domain.query.BaseSpecification;
import com.smartstore.api.v1.common.utils.SQLUtils;
import com.smartstore.api.v1.domain.category.entity.CategoryNode;

public class CategoryNodeSpecification {

  private CategoryNodeSpecification() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static Specification<CategoryNode> hasName(String name) {
    return (root, query, criteriaBuilder) -> (name == null) ? criteriaBuilder.conjunction()
        : criteriaBuilder.like(root.get("name"), "%" + SQLUtils.escapeLike(name) + "%");
  }

  public static Specification<CategoryNode> withFilters(String name, Boolean isDeleted) {
    return Specification
        .where(BaseSpecification.<CategoryNode>isDeleted(isDeleted))
        .and(hasName(name));
  }
}
