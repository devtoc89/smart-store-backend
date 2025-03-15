package com.smartstore.api.v1.domain.product.query;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.utils.SQLUtils;
import com.smartstore.api.v1.domain.product.entity.Product;

public class ProductSpecification {

  // Private constructor to prevent instantiation
  private ProductSpecification() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static Specification<Product> hasCategoryId(UUID categoryId) {
    return (root, query, criteriaBuilder) -> categoryId == null ? null
        : criteriaBuilder.equal(root.get("categoryId"), categoryId);
  }

  public static Specification<Product> hasName(String name) {
    return (root, query, criteriaBuilder) -> name == null ? null
        : criteriaBuilder.like(
            root.get("name"),
            "%" + SQLUtils.escapeLike(name) + "%");
  }
}
