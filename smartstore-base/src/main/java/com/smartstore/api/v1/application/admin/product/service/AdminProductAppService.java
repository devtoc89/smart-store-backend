package com.smartstore.api.v1.application.admin.product.service;

import java.util.UUID;

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
import com.smartstore.api.v1.common.utils.StringUtils;
import com.smartstore.api.v1.common.utils.ValidationUtil;
import com.smartstore.api.v1.domain.category.service.CategoryNodeService;
import com.smartstore.api.v1.domain.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductAppService {

  private final ProductService productService;
  private final CategoryNodeService categoryNodeService;

  private void validateCategory(String categoryId) throws BindException {
    if (!categoryNodeService.isExistsCategoryNodeById(StringUtils.stringToUUID(categoryId))) {
      throw ValidationUtil.createBindException(this, "categoryId", "카테고리(말단)의 ID가 유효하지 않습니다.");
    }
  }

  @Transactional(readOnly = true)
  public AdminProductResponseDTO getProductById(String id) {
    return AdminProductResponseDTO.fromVO(
        productService.findProductById(UUID.fromString(id)));
  }

  @Transactional(readOnly = true)
  public Page<AdminProductResponseDTO> getProductsByFilterWithPaging(
      AdminProductFilterRequestDTO searchRequest, Pageable pageable) {
    return AdminProductResponseDTO.fromVOWithPage(
        productService.findProductsByCondition(
            searchRequest.toSearchConditionVO(), pageable));
  }

  @Transactional
  public AdminProductResponseDTO postProduct(
      AdminProductPostRequestDTO postRequestDTO) throws BindException {
    validateCategory(postRequestDTO.getCategoryId());
    return AdminProductResponseDTO.fromVO(
        productService.createProduct(
            postRequestDTO.toVO()));
  }

  @Transactional
  public AdminProductResponseDTO putProduct(String id,
      AdminProductPutRequestDTO putRequestDTO) throws BindException {
    validateCategory(putRequestDTO.getCategoryId());
    return AdminProductResponseDTO.fromVO(
        productService.replaceProduct(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminProductResponseDTO patchProduct(String id,
      AdminProductPatchRequestDTO putRequestDTO) throws BindException {
    validateCategory(putRequestDTO.getCategoryId());
    return AdminProductResponseDTO.fromVO(
        productService.modifyProduct(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminProductResponseDTO deleteProduct(String id) {
    return AdminProductResponseDTO.fromVO(
        productService.deleteProduct(
            UUID.fromString(id)));
  }
}
