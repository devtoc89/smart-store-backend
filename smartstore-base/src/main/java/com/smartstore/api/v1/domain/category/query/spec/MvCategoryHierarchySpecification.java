package com.smartstore.api.v1.domain.category.query.spec;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.domain.category.entity.MvCategoryHierarchy;

public class MvCategoryHierarchySpecification {

  private MvCategoryHierarchySpecification() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static Specification<MvCategoryHierarchy> hasLvl1Id(UUID id) {
    return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction()
        : criteriaBuilder.equal(root.get("lv1Id"), id);
  }

  public static Specification<MvCategoryHierarchy> hasLvl2Id(UUID id) {
    return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction()
        : criteriaBuilder.equal(root.get("lv2Id"), id);
  }

  public static Specification<MvCategoryHierarchy> hasLvl3Id(UUID id) {
    return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction()
        : criteriaBuilder.equal(root.get("lv3Id"), id);
  }

}
