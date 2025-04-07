package com.smartstore.api.v1.application.admin.product.dto.base;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.smartstore.api.v1.application.admin.product.dto.AdminProductWithImageDTO;
import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.domain.common.vo.BaseEntityVO;
import com.smartstore.api.v1.domain.product.vo.ProductImageVO;
import com.smartstore.api.v1.domain.product.vo.ProductVO;
import com.smartstore.api.v1.domain.storedfile.vo.StoredFileVO;

public interface AdminProductUpsertRequestDTOIF {
  String getName();

  Integer getPrice();

  Integer getStock();

  String getCategoryId();

  // List<AdminProductWithImageDTO> getImages();

  // default List<ProductImageVO> makeImageVOList() {
  // return makeImageVOList(null);
  // }

  default List<ProductImageVO> makeImageVOList(UUID productId, List<AdminProductWithImageDTO> images) {
    return Optional.ofNullable(images).map(v1 -> v1.stream().map(v -> ProductImageVO.builder()
        .file(StoredFileVO.builder()
            .base(BaseEntityVO.builder()
                .id(UUID.fromString(v.getFileId()))
                .build())
            .build())
        .productId(productId)
        .isMain(v.getIsMain())
        .orderBy(v.getOrderBy()).build()).toList())
        .orElse(null);
  }

  default ProductVO toVO() {
    return ProductVO.builder()
        .name(getName())
        .price(getPrice())
        .stock(getStock())
        .categoryId(StringUtil.stringToUUID(getCategoryId()))
        .build();
  }

  default ProductVO toVO(UUID id, List<AdminProductWithImageDTO> images) {
    return ProductVO.builder()
        .name(getName())
        .price(getPrice())
        .stock(getStock())
        .categoryId(StringUtil.stringToUUID(getCategoryId()))
        .images((makeImageVOList(id, images)))
        .build();
  }
}
