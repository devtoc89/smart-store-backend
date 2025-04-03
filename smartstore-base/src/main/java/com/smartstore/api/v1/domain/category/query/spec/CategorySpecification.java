package com.smartstore.api.v1.domain.category.query.spec;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.utils.sql.SQLUtil;
import com.smartstore.api.v1.domain.category.entity.Category;

public class CategorySpecification {

  private CategorySpecification() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static Specification<Category> hasName(String name) {
    return (root, query, criteriaBuilder) -> (name == null) ? criteriaBuilder.conjunction()
        : criteriaBuilder.like(root.get("name"), "%" + SQLUtil.escapeLike(name) +
            "%");
    // return (root, query, cb) -> {
    // if (name == null || name.isBlank())
    // return null;

    // Expression<String> nameExpr = root.get("name");

    // Expression<Boolean> ilikeExpr = cb.(
    // "ilike", Boolean.class,
    // nameExpr,
    // cb.literal("%" + name + "%"));

    // return cb.isTrue(ilikeExpr);
    // };
  }

  public static Specification<Category> hasLevel(Integer level) {
    return (root, query, criteriaBuilder) -> (level == null) ? criteriaBuilder.conjunction()
        : criteriaBuilder.equal(root.get("level"), level);
  }

  public static Specification<Category> hasParentId(UUID parentId) {
    return (root, query, criteriaBuilder) -> parentId == null ? criteriaBuilder.conjunction()
        : criteriaBuilder.equal(root.get("parentId"), parentId);
  }

}
