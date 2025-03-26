package com.smartstore.api.v1.domain.product.vo;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.domain.vo.BaseFilterConditionVO;
import com.smartstore.api.v1.domain.product.entity.ProductImage;
import com.smartstore.api.v1.domain.product.query.ProductImageSpecification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductImageFilterConditionVO {
  private final BaseFilterConditionVO base;
  private final UUID productId;
  private final String filename;

  public Specification<ProductImage> toSpecification() {

    Specification<ProductImage> cond = ProductImageSpecification.hasProductId(productId)
        .and(ProductImageSpecification.hasFilename(filename));

    if (base != null) {
      cond = cond.and(base.<ProductImage>toSubSpecification());
    }

    return Specification.where(cond);
  }
}
