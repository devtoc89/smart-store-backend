package com.smartstore.api.v1.domain.category.vo;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.domain.vo.BaseFilterConditionVO;
import com.smartstore.api.v1.domain.category.entity.CategoryNode;
import com.smartstore.api.v1.domain.category.query.CategoryNodeSpecification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryNodeFilterConditionVO {
  private final BaseFilterConditionVO base;
  private final String name;

  public Specification<CategoryNode> toSpecification() {
    Specification<CategoryNode> cond = CategoryNodeSpecification.hasName(name);

    if (base != null) {
      cond = cond.and(base.<CategoryNode>toSubSpecification());
    }

    return Specification.where(cond);
  }
}
