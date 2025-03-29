package com.smartstore.api.v1.domain.category.vo;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.domain.vo.BaseFilterConditionVO;
import com.smartstore.api.v1.domain.category.entity.CategoryL2;
import com.smartstore.api.v1.domain.category.query.CategoryL2Specification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryL2FilterConditionVO {
  private final BaseFilterConditionVO base;
  private final String name;
  private final UUID categoryL1Id;

  public Specification<CategoryL2> toSpecification() {
    Specification<CategoryL2> cond = CategoryL2Specification
        .hasName(name)
        .and(CategoryL2Specification.hasCategoryL1Id(categoryL1Id));

    if (base != null) {
      cond = cond.and(base.<CategoryL2>toSubSpecification());
    }

    return Specification.where(cond);
  }
}
