package com.smartstore.api.v1.domain.category.vo;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.domain.category.entity.Category;
import com.smartstore.api.v1.domain.category.query.spec.CategorySpecification;
import com.smartstore.api.v1.domain.common.vo.BaseFilterConditionVO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryFilterConditionVO {
  private final BaseFilterConditionVO base;
  private final String name;
  private final Integer level;
  private final UUID parentId;

  public Specification<Category> toSpecification() {
    Specification<Category> cond = CategorySpecification
        .hasName(name)
        .and(CategorySpecification.hasParentId(parentId))
        .and(CategorySpecification.hasLevel(level));

    if (base != null) {
      cond = cond.and(base.<Category>toSubSpecification());
    }

    return Specification.where(cond);
  }
}
