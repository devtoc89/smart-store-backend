package com.smartstore.api.v1.application.admin.category.mapper;

import java.util.List;

import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1TreeResponseDTO;
import com.smartstore.api.v1.application.admin.categoryl2.dto.AdminCategoryL2TreeResponseDTO;
import com.smartstore.api.v1.application.admin.categorynode.dto.AdminCategoryNodeResponseDTO;
import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.domain.category.vo.CategoryVO;

public class AdminCategoryListResponseAssembler {

  private AdminCategoryListResponseAssembler() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static List<AdminCategoryL1TreeResponseDTO> fromVO(CategoryVO vo) {
    return vo.getCategoryL1Nest().stream()
        .map(l1 -> new AdminCategoryL1TreeResponseDTO(l1.getCategoryL1VO(), l1.getChildren().stream()
            .map(l2 -> new AdminCategoryL2TreeResponseDTO(l2.getCategoryL2VO(), l2.getChildren().stream()
                .map(node -> new AdminCategoryNodeResponseDTO(node.getCategoryNodeVO()))
                .toList()))
            .toList()))
        .toList();
  }
}
