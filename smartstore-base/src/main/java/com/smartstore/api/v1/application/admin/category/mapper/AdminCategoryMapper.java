package com.smartstore.api.v1.application.admin.category.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.util.ObjectUtils;

import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1TreePutRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl2.dto.AdminCategoryL2TreePutRequestDTO;
import com.smartstore.api.v1.application.admin.categorynode.dto.AdminCategoryNodeTreePutRequestDTO;
import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.domain.category.vo.CategoryL1VO;
import com.smartstore.api.v1.domain.category.vo.CategoryL2VO;
import com.smartstore.api.v1.domain.category.vo.CategoryNodeVO;
import com.smartstore.api.v1.domain.category.vo.CategoryVO;
import com.smartstore.api.v1.domain.category.vo.CategoryVO.CategoryL1Nest;
import com.smartstore.api.v1.domain.category.vo.CategoryVO.CategoryL1Nest.CategoryL2Nest;
import com.smartstore.api.v1.domain.category.vo.CategoryVO.CategoryL1Nest.CategoryL2Nest.CategoryNodeNest;
import com.smartstore.api.v1.domain.common.vo.BaseEntityVO;

public class AdminCategoryMapper {

  private AdminCategoryMapper() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static List<CategoryL1Nest> makeCategoryL1VONestListWhenPut(List<AdminCategoryL1TreePutRequestDTO> list) {
    if (ObjectUtils.isEmpty(list))
      return List.of();
    return list.stream().map(l1 -> {
      var id = StringUtil.stringToUUIDOrNew(l1.getId());
      return new CategoryL1Nest(
          CategoryL1VO.builder()
              .name(l1.getName())
              .orderBy(l1.getOrderBy())
              .base(BaseEntityVO.builder()
                  .id(id)
                  .build())
              .build(),
          makeCategoryL2VONestListWhenPut(id, l1.getChildren()));
    })
        .toList();
  }

  public static List<CategoryL2Nest> makeCategoryL2VONestListWhenPut(UUID parentId,
      List<AdminCategoryL2TreePutRequestDTO> list) {
    if (ObjectUtils.isEmpty(list))
      return List.of();

    var categoryL1Id = parentId;

    return list.stream().map(l2 -> {
      var id = StringUtil.stringToUUIDOrNew(l2.getId());
      return new CategoryL2Nest(CategoryL2VO.builder()
          .categoryL1Id(categoryL1Id)
          .name(l2.getName())
          .orderBy(l2.getOrderBy())
          .base(BaseEntityVO.builder()
              .id(id)
              .build())
          .build(),
          makeCategoryNodeNestVOListWhenPut(id, l2
              .getChildren()));
    })
        .toList();
  }

  public static List<CategoryNodeNest> makeCategoryNodeNestVOListWhenPut(
      UUID parentId,
      List<AdminCategoryNodeTreePutRequestDTO> list) {
    if (ObjectUtils.isEmpty(list))
      return List.of();

    return list.stream().map(node -> new CategoryNodeNest(
        CategoryNodeVO.builder()
            .categoryL2Id(parentId)
            .name(node.getName())
            .orderBy(node.getOrderBy())
            .base(BaseEntityVO.builder()
                .id(StringUtil.stringToUUIDOrNew(node.getId()))
                .build())
            .build()))
        .toList();
  }

  public static CategoryVO fromPutDTO(List<AdminCategoryL1TreePutRequestDTO> list) {
    return new CategoryVO(makeCategoryL1VONestListWhenPut(list));
  }

}
