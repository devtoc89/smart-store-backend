package com.smartstore.api.v1.domain.category.vo;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.domain.category.entity.CategoryNode;
import com.smartstore.api.v1.domain.category.query.CategoryNodeSpecification;
import com.smartstore.api.v1.domain.common.vo.BaseFilterConditionVO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryNodeFilterConditionVO {
  private final BaseFilterConditionVO base;
  private final String name;
  private final UUID categoryL2Id;

  public Specification<CategoryNode> toSpecification() {
    Specification<CategoryNode> cond = CategoryNodeSpecification
        .hasName(name)
        .and(CategoryNodeSpecification.hasCategoryL2Id(categoryL2Id));

    if (base != null) {
      cond = cond.and(base.<CategoryNode>toSubSpecification());
    }

    return Specification.where(cond);
  }
}
