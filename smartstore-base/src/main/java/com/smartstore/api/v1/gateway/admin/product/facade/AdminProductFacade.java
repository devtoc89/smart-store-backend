package com.smartstore.api.v1.gateway.admin.product.facade;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.domain.product.service.ProductService;
import com.smartstore.api.v1.gateway.admin.product.dto.AdminProductFilterRequestDTO;
import com.smartstore.api.v1.gateway.admin.product.dto.AdminProductPatchRequestDTO;
import com.smartstore.api.v1.gateway.admin.product.dto.AdminProductPostRequestDTO;
import com.smartstore.api.v1.gateway.admin.product.dto.AdminProductPutRequestDTO;
import com.smartstore.api.v1.gateway.admin.product.dto.AdminProductResponseDTO;

@Service
public class AdminProductFacade {

  private final ProductService productService;

  public AdminProductFacade(ProductService productService) {
    this.productService = productService;
  }

  private boolean checkIsValidCategory(String categoryId) {
    return true;
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
      AdminProductPostRequestDTO postRequestDTO) {
    return AdminProductResponseDTO.fromVO(
        productService.createProduct(
            postRequestDTO.toVO()));
  }

  @Transactional
  public AdminProductResponseDTO putProduct(String id,
      AdminProductPutRequestDTO putRequestDTO) {
    return AdminProductResponseDTO.fromVO(
        productService.replaceProduct(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminProductResponseDTO patchProduct(String id,
      AdminProductPatchRequestDTO putRequestDTO) {
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
