package com.smartstore.api.v1.domain.category.query;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.domain.query.BaseSpecification;
import com.smartstore.api.v1.common.utils.sql.SQLUtil;
import com.smartstore.api.v1.domain.category.entity.CategoryL2;

public class CategoryL2Specification {

  private CategoryL2Specification() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static Specification<CategoryL2> hasName(String name) {
    return (root, query, criteriaBuilder) -> (name == null) ? criteriaBuilder.conjunction()
        : criteriaBuilder.like(root.get("name"), "%" + SQLUtil.escapeLike(name) + "%");
  }

  public static Specification<CategoryL2> withFilters(String name, Boolean isDeleted) {
    return Specification
        .where(BaseSpecification.<CategoryL2>isDeleted(isDeleted))
        .and(hasName(name));
  }
}
