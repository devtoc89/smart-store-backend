package com.smartstore.api.v1.application.admin.product.service;

import java.util.Collections;
import java.util.Optional;
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
import com.smartstore.api.v1.application.admin.productimage.dto.AdminProductImageResponseDTO;
import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.common.utils.validation.ValidationUtil;
import com.smartstore.api.v1.domain.category.service.CategoryNodeService;
import com.smartstore.api.v1.domain.product.service.ProductImageService;
import com.smartstore.api.v1.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductAppService {

  private final ProductImageService productImageService;

  private final ProductService productService;
  private final CategoryNodeService categoryNodeService;

  private void validateCategory(String categoryId) throws BindException {
    if (ObjectUtils.isEmpty(categoryId) || !categoryNodeService.isExist(StringUtil.stringToUUID(categoryId))) {
      throw ValidationUtil.createBindException(this, "categoryId", "말단 카테고리의 ID가 유효하지 않습니다.");
    }
  }

  @Transactional(readOnly = true)
  public AdminProductResponseDTO get(String id) {
    return AdminProductResponseDTO.fromVO(
        productService.findById(UUID.fromString(id)));
  }

  @Transactional(readOnly = true)
  public Page<AdminProductResponseDTO> getList(
      AdminProductFilterRequestDTO dto, Pageable pageable) {
    return AdminProductResponseDTO.fromVOWithPage(
        productService.findManyByCondition(
            dto.toSearchConditionVO(), pageable));
  }

  @Transactional
  public AdminProductResponseDTO post(
      AdminProductPostRequestDTO dto) throws BindException {
    validateCategory(dto.getCategoryId());
    var id = UUID.randomUUID();
    var response = AdminProductResponseDTO.fromVO(
        productService.create(id,
            dto.toVO()));

    var imagesVO = Optional.ofNullable(dto.makeImageVOList(id)).map(images -> images.stream()
        .map(productImageService::create)
        .map(AdminProductImageResponseDTO::new)
        .toList()).orElse(Collections.emptyList());
    response.setImages(imagesVO);
    return response;
  }

  @Transactional
  public AdminProductResponseDTO put(String productId,
      AdminProductPutRequestDTO dto) throws BindException {
    validateCategory(dto.getCategoryId());
    var id = UUID.fromString(productId);
    var response = AdminProductResponseDTO.fromVO(
        productService.replace(id,
            dto.toVO()));
    productImageService.deleteByProductId(id);
    var imagesVO = Optional.ofNullable(dto.makeImageVOList(id)).map(images -> images.stream()
        .map(productImageService::create)
        .map(AdminProductImageResponseDTO::new)
        .toList()).orElse(Collections.emptyList());
    response.setImages(imagesVO);
    return response;
  }

  @Transactional
  public AdminProductResponseDTO patch(String id,
      AdminProductPatchRequestDTO dto) throws BindException {
    if (!ObjectUtils.isEmpty(dto.getCategoryId())) {
      validateCategory(dto.getCategoryId());
    }
    // TODO: 이미지 관리 방안(처리 플로우가 뭐가 좋을까.)
    return AdminProductResponseDTO.fromVO(
        productService.modify(
            UUID.fromString(id), dto.toVO()));
  }

  @Transactional
  public AdminProductResponseDTO delete(String id) {
    return AdminProductResponseDTO.fromVO(
        productService.delete(
            UUID.fromString(id)));
  }
}
