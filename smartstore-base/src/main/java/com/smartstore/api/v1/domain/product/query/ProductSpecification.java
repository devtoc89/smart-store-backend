package com.smartstore.api.v1.domain.product.query;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.utils.sql.SQLUtil;
import com.smartstore.api.v1.domain.product.entity.Product;

public class ProductSpecification {

  // Private constructor to prevent instantiation
  private ProductSpecification() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static Specification<Product> hasCategoryIdList(List<UUID> categoryIdList) {
    return (root, query, criteriaBuilder) -> ObjectUtils.isEmpty(categoryIdList) ? null
        : root.get("categoryId").in(categoryIdList);
  }

  public static Specification<Product> hasName(String name) {
    return (root, query, criteriaBuilder) -> name == null ? null
        : criteriaBuilder.like(
            root.get("name"),
            "%" + SQLUtil.escapeLike(name) + "%");
  }
}
