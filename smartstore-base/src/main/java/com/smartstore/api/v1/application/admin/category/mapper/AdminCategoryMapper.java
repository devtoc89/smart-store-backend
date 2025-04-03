package com.smartstore.api.v1.application.admin.category.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.util.ObjectUtils;

import com.smartstore.api.v1.application.admin.category.dto.tree.AdminCategoryL1TreePutRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.tree.AdminCategoryL2TreePutRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.tree.AdminCategoryL3TreePutRequestDTO;
import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.domain.category.vo.CategoryNestVO;
import com.smartstore.api.v1.domain.category.vo.CategoryNestVO.CategoryL1Nest;
import com.smartstore.api.v1.domain.category.vo.CategoryNestVO.CategoryL1Nest.CategoryL2Nest;
import com.smartstore.api.v1.domain.category.vo.CategoryNestVO.CategoryL1Nest.CategoryL2Nest.CategoryL3Nest;
import com.smartstore.api.v1.domain.category.vo.CategoryVO;
import com.smartstore.api.v1.domain.common.vo.BaseEntityVO;

public class AdminCategoryMapper {

  private AdminCategoryMapper() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static CategoryNestVO fromPutDTO(List<AdminCategoryL1TreePutRequestDTO> list) {
    return new CategoryNestVO(toCategoryL1NestList(list));
  }

  private static CategoryL1Nest toCategoryL1Nest(AdminCategoryL1TreePutRequestDTO l1) {
    var id = StringUtil.stringToUUIDOrNew(l1.getId());
    return new CategoryL1Nest(
        toCategoryVO(id, l1.getName(), l1.getOrderBy(), 1, null),
        toCategoryL2NestList(l1.getChildren(), id));
  }

  private static List<CategoryL1Nest> toCategoryL1NestList(List<AdminCategoryL1TreePutRequestDTO> list) {
    if (ObjectUtils.isEmpty(list))
      return List.of();
    return list.stream()
        .map(AdminCategoryMapper::toCategoryL1Nest)
        .toList();
  }

  private static CategoryL2Nest toCategoryL2Nest(AdminCategoryL2TreePutRequestDTO l2, UUID parentId) {
    var id = StringUtil.stringToUUIDOrNew(l2.getId());
    return new CategoryL2Nest(
        toCategoryVO(id, l2.getName(), l2.getOrderBy(), 2, parentId),
        toCategoryL3NestList(l2.getChildren(), id));
  }

  private static List<CategoryL2Nest> toCategoryL2NestList(List<AdminCategoryL2TreePutRequestDTO> list, UUID parentId) {
    if (ObjectUtils.isEmpty(list))
      return List.of();
    return list.stream()
        .map(l2 -> toCategoryL2Nest(l2, parentId))
        .toList();
  }

  private static CategoryL3Nest toCategoryL3Nest(AdminCategoryL3TreePutRequestDTO l3, UUID parentId) {
    return new CategoryL3Nest(
        toCategoryVO(StringUtil.stringToUUIDOrNew(l3.getId()), l3.getName(), l3.getOrderBy(), 3, parentId));
  }

  private static List<CategoryL3Nest> toCategoryL3NestList(List<AdminCategoryL3TreePutRequestDTO> list,
      UUID parentId) {
    if (ObjectUtils.isEmpty(list))
      return List.of();
    return list.stream()
        .map(l3 -> toCategoryL3Nest(l3, parentId))
        .toList();
  }

  private static CategoryVO toCategoryVO(UUID id, String name, Integer orderBy, int level, UUID parentId) {
    return CategoryVO.builder()
        .name(name)
        .orderBy(orderBy)
        .level(level)
        .parentId(parentId)
        .base(BaseEntityVO.builder().id(id).build())
        .build();
  }
}
