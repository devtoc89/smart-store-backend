package com.smartstore.api.v1.domain.product.vo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.domain.common.vo.BaseEntityVO;
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
  private final List<ProductImageVO> images;
  private final List<UUID> deleteFileIds;

  public static ProductVO fromEntity(Product product, String cdnUrl) {
    return ProductVO.builder()
        .base(BaseEntityVO.fromEntity(product))
        .name(product.getName())
        .price(product.getPrice())
        .categoryId(product.getCategoryId())
        .images(Optional.ofNullable(product.getImages())
            .map(images -> images.stream().map(v -> ProductImageVO.fromEntity(v, cdnUrl)).toList()).orElse(null))
        .build();
  }

  public static Page<ProductVO> fromEntityWithPage(Page<Product> products, String cdnUrl) {
    return products.map(v -> fromEntity(v, cdnUrl));
  }
}
