package com.smartstore.api.v1.domain.product.vo;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.domain.vo.BaseFilterConditionVO;
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
  private final UUID categoryId;
  private final String name;

  public Specification<Product> toSpecification() {

    Specification<Product> cond = ProductSpecification.hasName(name)
        .and(ProductSpecification.hasCategoryId(categoryId));

    if (base != null) {
      cond = cond.and(base.<Product>toSubSpecification());
    }

    return Specification.where(cond);
  }
}
