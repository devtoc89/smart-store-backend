package com.smartstore.api.v1.application.admin.product.service;

import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import com.smartstore.api.v1.application.admin.product.dto.AdminProductFilterRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductPatchRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductPostRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductPutRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductResponseDTO;
import com.smartstore.api.v1.common.config.CloudFrontProperties;
import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.common.utils.validation.ValidationUtil;
import com.smartstore.api.v1.domain.category.service.CategoryService;
import com.smartstore.api.v1.domain.category.service.MvCategoryHierarchyService;
import com.smartstore.api.v1.domain.category.vo.MvCategoryHierarchyConditionVO;
import com.smartstore.api.v1.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductAppService {

  private final ProductService productService;
  private final CategoryService categoryService;
  private final MvCategoryHierarchyService mvCategoryHierarchyService;

  private final CloudFrontProperties cloudFrontProperties;

  private void validateCategory(String categoryId) throws BindException {
    if (ObjectUtils.isEmpty(categoryId) || !categoryService.isExists(StringUtil.stringToUUID(categoryId))) {
      throw ValidationUtil.createBindException(this, "categoryId", "대상 카테고리의 ID가 유효하지 않습니다.");
    }
  }

  @Transactional(readOnly = true)
  public AdminProductResponseDTO get(String id) {
    var item = productService.findById(UUID.fromString(id));
    var category = mvCategoryHierarchyService.findById(item.getCategoryId());

    return AdminProductResponseDTO.fromVO(item, category, cloudFrontProperties.getUrl());
  }

  @Transactional(readOnly = true)
  public Page<AdminProductResponseDTO> getList(
      AdminProductFilterRequestDTO dto, Pageable pageable) {
    // TODO: refactoring
    var mvCategorySearchCond = MvCategoryHierarchyConditionVO.builder()
        .categoryL1Id(StringUtil.stringToUUID(dto.getCategoryL1Id()))
        .categoryL2Id(StringUtil.stringToUUID(dto.getCategoryL2Id()))
        .categoryL3Id(StringUtil.stringToUUID(dto.getCategoryL3Id()))
        .build();
    var categories = mvCategoryHierarchyService
        .findManyByCondition(mvCategorySearchCond);
    return AdminProductResponseDTO.fromVOWithPage(
        productService.findManyByCondition(
            dto.toSearchConditionVO(categories.stream().map(v -> v.getId()).toList()), pageable),
        categories, cloudFrontProperties.getUrl());
  }

  @Transactional
  public AdminProductResponseDTO post(
      AdminProductPostRequestDTO dto) throws BindException {
    validateCategory(dto.getCategoryId());
    var id = UUID.randomUUID();
    var item = productService.create(id, dto.toVO(id, dto.getImages()));
    var category = mvCategoryHierarchyService.findById(item.getCategoryId());
    return AdminProductResponseDTO.fromVO(item, category, cloudFrontProperties.getUrl());
  }

  @Transactional
  public AdminProductResponseDTO put(String productId,
      AdminProductPutRequestDTO dto) throws BindException {
    validateCategory(dto.getCategoryId());

    var id = StringUtil.stringToUUID(productId);
    var item = productService.replace(id, dto.toVO(id, dto.getImages()));

    var category = mvCategoryHierarchyService.findById(item.getCategoryId());

    return AdminProductResponseDTO.fromVO(item, category, cloudFrontProperties.getUrl());
  }

  @Transactional
  public AdminProductResponseDTO patch(String productId,
      AdminProductPatchRequestDTO dto) throws BindException {
    if (!ObjectUtils.isEmpty(dto.getCategoryId())) {
      validateCategory(dto.getCategoryId());
    }
    var id = StringUtil.stringToUUID(productId);
    var item = productService.modify(id, dto.toVO(id, dto.getImages()));
    var category = mvCategoryHierarchyService.findById(item.getCategoryId());
    return AdminProductResponseDTO.fromVO(item, category, cloudFrontProperties.getUrl());
  }

  @Transactional
  public AdminProductResponseDTO delete(String id) {
    var item = productService.delete(UUID.fromString(id));
    var category = mvCategoryHierarchyService.findById(item.getCategoryId());
    return AdminProductResponseDTO.fromVO(item, category, cloudFrontProperties.getUrl());
  }
}
