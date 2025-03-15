package com.smartstore.api.v1.domain.category.query;

import org.springframework.data.jpa.domain.Specification;
import com.smartstore.api.v1.common.domain.query.BaseSpecification;
import com.smartstore.api.v1.common.utils.SQLUtils;
import com.smartstore.api.v1.domain.category.entity.CategoryL1;

public class CategoryL1Specification {

  private CategoryL1Specification() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static Specification<CategoryL1> hasName(String name) {
    return (root, query, criteriaBuilder) -> (name == null) ? criteriaBuilder.conjunction()
        : criteriaBuilder.like(root.get("name"), "%" + SQLUtils.escapeLike(name) + "%");
  }

  public static Specification<CategoryL1> withFilters(String name, Boolean isDeleted) {
    return Specification
        .where(BaseSpecification.<CategoryL1>isDeleted(isDeleted))
        .and(hasName(name));
  }
}
