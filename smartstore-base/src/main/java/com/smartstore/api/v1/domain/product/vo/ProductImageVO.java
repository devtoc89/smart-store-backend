package com.smartstore.api.v1.domain.product.vo;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.domain.common.vo.BaseEntityVO;
import com.smartstore.api.v1.domain.product.entity.ProductImage;
import com.smartstore.api.v1.domain.storedfile.vo.StoredFileVO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductImageVO {
  private final BaseEntityVO base;
  private final StoredFileVO file;
  private final UUID productId;
  private final Integer orderBy;
  private final Boolean isMain;

  private final String url; // CloudFront 경로 포함된 URL

  // public static ProductImageVO fromEntity(ProductImage entity) {
  // return fromEntity(entity, null);
  // }

  public static ProductImageVO fromEntity(ProductImage entity) {
    return ProductImageVO.builder()
        .base(BaseEntityVO.fromEntity(entity))
        .file(StoredFileVO.fromEntity(entity.getFile()))
        .productId(entity.getProductId())
        .orderBy(entity.getOrderBy())
        .isMain(entity.isMain())
        .build();
  }

  public static Page<ProductImageVO> fromEntityWithPage(Page<ProductImage> products) {
    return products.map(v -> fromEntity(v));
  }

  // public static Page<ProductImageVO> fromEntityWithPage(Page<ProductImage>
  // products, String cloudFrontUrl) {
  // return products.map(v -> fromEntity(v, cloudFrontUrl));
  // }
}
