package com.smartstore.api.v1.domain.category.vo;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.domain.category.entity.MvCategoryHierarchy;
import com.smartstore.api.v1.domain.category.query.spec.MvCategoryHierarchySpecification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvCategoryHierarchyConditionVO {
  private final UUID categoryL1Id;
  private final UUID categoryL2Id;
  private final UUID categoryL3Id;

  public Specification<MvCategoryHierarchy> toSpecification() {
    Specification<MvCategoryHierarchy> cond = MvCategoryHierarchySpecification
        .hasLvl1Id(categoryL1Id)
        .and(MvCategoryHierarchySpecification.hasLvl2Id(categoryL2Id))
        .and(MvCategoryHierarchySpecification.hasLvl3Id(categoryL3Id));

    return Specification.where(cond);
  }
}
