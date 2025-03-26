package com.smartstore.api.v1.domain.category.query;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.utils.sql.SQLUtil;
import com.smartstore.api.v1.domain.category.entity.CategoryNode;

public class CategoryNodeSpecification {

  private CategoryNodeSpecification() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static Specification<CategoryNode> hasName(String name) {
    return (root, query, criteriaBuilder) -> (name == null) ? criteriaBuilder.conjunction()
        : criteriaBuilder.like(root.get("name"), "%" + SQLUtil.escapeLike(name) + "%");
  }

  public static Specification<CategoryNode> hasCategoryL2Id(UUID categoryL2Id) {
    return (root, query, criteriaBuilder) -> categoryL2Id == null ? criteriaBuilder.conjunction()
        : criteriaBuilder.equal(root.get("categoryL2").get("id"), categoryL2Id);
  }
}
