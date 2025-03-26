package com.smartstore.api.v1.domain.product.query;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.utils.sql.SQLUtil;
import com.smartstore.api.v1.domain.product.entity.ProductImage;

import jakarta.persistence.criteria.JoinType;

public class ProductImageSpecification {

  // Private constructor to prevent instantiation
  private ProductImageSpecification() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static Specification<ProductImage> hasProductId(UUID productId) {
    return (root, query, criteriaBuilder) -> productId == null ? null
        : criteriaBuilder.equal(root.get("productId"), productId);
  }

  public static Specification<ProductImage> hasFilename(String filename) {
    return (root, query, criteriaBuilder) -> filename == null ? null
        : criteriaBuilder.like(
            root.join("file", JoinType.LEFT).get("originalFilename"),
            "%" + SQLUtil.escapeLike(filename) + "%");
  }
}
