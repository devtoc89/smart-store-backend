package com.smartstore.api.v1.domain.category.vo;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.domain.vo.BaseFilterConditionVO;
import com.smartstore.api.v1.domain.category.entity.CategoryL1;
import com.smartstore.api.v1.domain.category.query.CategoryL1Specification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryL1FilterConditionVO {
  private final BaseFilterConditionVO base;
  private final String name;

  public Specification<CategoryL1> toSpecification() {
    Specification<CategoryL1> cond = CategoryL1Specification.hasName(name);

    if (base != null) {
      cond = cond.and(base.<CategoryL1>toSubSpecification());
    }

    return Specification.where(cond);
  }
}
