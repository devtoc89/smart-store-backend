package com.smartstore.api.v1.application.admin.product.dto.base;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.smartstore.api.v1.application.admin.product.dto.AdminProductWithImageDTO;
import com.smartstore.api.v1.common.domain.vo.BaseEntityVO;
import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.domain.product.vo.ProductImageVO;
import com.smartstore.api.v1.domain.product.vo.ProductVO;
import com.smartstore.api.v1.domain.storedfile.vo.StoredFileVO;

public interface AdminProductUpsertRequestDTOIF {
  String getName();

  Integer getPrice();

  String getCategoryId();

  List<AdminProductWithImageDTO> getImages();

  default List<ProductImageVO> makeImageVOList() {
    return makeImageVOList(null);
  }

  default List<ProductImageVO> makeImageVOList(UUID productId) {
    return Optional.ofNullable(getImages()).map(images -> images.stream().map(v -> ProductImageVO.builder()
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
        .categoryId(StringUtil.stringToUUID(getCategoryId()))
        .images((makeImageVOList()))
        .build();
  }

}
