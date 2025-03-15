package com.smartstore.api.v1.domain.product.vo;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.common.domain.vo.BaseEntityVO;
import com.smartstore.api.v1.domain.product.entity.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductVO {
  private final BaseEntityVO base;
  private final String name;
  private final Integer price;
  private final UUID categoryId;

  public static ProductVO fromEntity(Product product) {
    return ProductVO.builder()
        .base(BaseEntityVO.fromEntity(product))
        .name(product.getName())
        .price(product.getPrice())
        .categoryId(product.getCategoryId())
        .build();
  }

  public static Page<ProductVO> fromEntityWithPage(Page<Product> products) {
    return products.map(v -> fromEntity(v));
  }
}
