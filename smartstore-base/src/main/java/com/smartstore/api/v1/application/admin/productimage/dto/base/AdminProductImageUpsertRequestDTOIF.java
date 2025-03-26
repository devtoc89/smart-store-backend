package com.smartstore.api.v1.application.admin.productimage.dto.base;

import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;

import com.smartstore.api.v1.common.domain.vo.BaseEntityVO;
import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.domain.product.vo.ProductImageVO;
import com.smartstore.api.v1.domain.storedfile.vo.StoredFileVO;

public interface AdminProductImageUpsertRequestDTOIF {
  String getFileId();

  // String getProductId();

  Boolean getIsMain();

  Integer getOrderBy();

  default ProductImageVO toVO(String productId) {
    var file = ObjectUtils.isEmpty(getFileId()) ? null
        : StoredFileVO.builder().base(BaseEntityVO.builder().id(UUID.fromString(getFileId())).build())
            .build();

    return ProductImageVO.builder()
        .file(file)
        .productId(StringUtil.stringToUUID(productId))
        .isMain(getIsMain())
        .orderBy(getOrderBy())
        .build();
  }
}
