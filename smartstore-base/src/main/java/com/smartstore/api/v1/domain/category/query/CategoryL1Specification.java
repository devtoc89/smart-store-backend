package com.smartstore.api.v1.domain.category.query;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.utils.sql.SQLUtil;
import com.smartstore.api.v1.domain.category.entity.CategoryL1;

public class CategoryL1Specification {

  private CategoryL1Specification() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static Specification<CategoryL1> hasName(String name) {
    return (root, query, criteriaBuilder) -> (name == null) ? criteriaBuilder.conjunction()
        : criteriaBuilder.like(root.get("name"), "%" + SQLUtil.escapeLike(name) + "%");
  }

}
