package com.smartstore.api.v1.domain.product.vo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.domain.common.vo.BaseFilterConditionVO;
import com.smartstore.api.v1.domain.product.entity.Product;
import com.smartstore.api.v1.domain.product.query.ProductSpecification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductFilterConditionVO {
  private final BaseFilterConditionVO base;
  private final String name;

  private final List<UUID> categoryIdList;

  public Specification<Product> toSpecification() {

    Specification<Product> cond = ProductSpecification.hasName(name)
        .and(ProductSpecification.hasCategoryIdList(categoryIdList));

    if (base != null) {
      cond = cond.and(base.<Product>toSubSpecification());
    }

    return Specification.where(cond);
  }
}
