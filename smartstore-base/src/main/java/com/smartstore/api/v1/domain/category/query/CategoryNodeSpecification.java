package com.smartstore.api.v1.domain.category.query;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

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

  public static Specification<CategoryNode> hasCategoryL2Id(UUID categoryL2Id) {
    return (root, query, criteriaBuilder) -> categoryL2Id == null ? criteriaBuilder.conjunction()
        : criteriaBuilder.equal(root.get("categoryL2").get("id"), categoryL2Id);
  }
}
