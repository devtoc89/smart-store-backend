package com.smartstore.api.v1.application.admin.productimage.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import com.smartstore.api.v1.application.admin.productimage.dto.AdminProductImageFilterRequestDTO;
import com.smartstore.api.v1.application.admin.productimage.dto.AdminProductImagePostRequestDTO;
import com.smartstore.api.v1.application.admin.productimage.dto.AdminProductImageResponseDTO;
import com.smartstore.api.v1.common.utils.validation.ValidationUtil;
import com.smartstore.api.v1.domain.product.service.ProductImageService;
import com.smartstore.api.v1.domain.product.service.ProductService;
import com.smartstore.api.v1.domain.storedfile.service.StoredFileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductImageAppService {

  private final ProductService productService;
  private final ProductImageService productImageService;
  private final StoredFileService storedFileService;

  private void validateProduct(String productId) throws BindException {
    if (ObjectUtils.isEmpty(productId) || !productService.isExist(UUID.fromString(productId))) {
      throw ValidationUtil.createBindException(this, "productId", "상품의 ID가 유효하지 않습니다.");
    }
  }

  private void validateProductImage(String id) throws BindException {
    if (ObjectUtils.isEmpty(id) || !productImageService.isExist(UUID.fromString(id))) {
      throw ValidationUtil.createBindException(this, "id", "상품 이미지 ID가 유효하지 않습니다.");
    }
  }

  private void validateFile(String fileId) throws BindException {
    if (ObjectUtils.isEmpty(fileId) || !storedFileService.isExist(UUID.fromString(fileId))) {
      throw ValidationUtil.createBindException(this, "fileId", "파일의 ID가 유효하지 않습니다.");
    }
  }

  @Transactional(readOnly = true)
  public AdminProductImageResponseDTO get(String productId, String id) throws BindException {
    validateProduct(productId);
    return AdminProductImageResponseDTO.fromVO(
        productImageService.findById(UUID.fromString(id)));
  }

  @Transactional(readOnly = true)
  public Page<AdminProductImageResponseDTO> getList(String productId,
      AdminProductImageFilterRequestDTO dto, Pageable pageable) {
    return AdminProductImageResponseDTO.fromVOWithPage(
        productImageService.findManyByCondition(
            dto.toSearchConditionVO(productId), pageable));
  }

  @Transactional
  public AdminProductImageResponseDTO post(String productId,
      AdminProductImagePostRequestDTO dto) throws BindException {
    validateProduct(productId);
    validateFile(dto.getFileId());
    // TODO: lamda로 실제 업로드 완료시 체크되도록 고도화 필요
    storedFileService.updateIsUploaded(UUID.fromString(dto.getFileId()));
    return AdminProductImageResponseDTO.fromVO(
        productImageService.create(
            dto.toVO(productId)));
  }

  @Transactional
  public AdminProductImageResponseDTO delete(String productId, String id) throws BindException {
    validateProduct(productId);
    validateProductImage(id);
    return AdminProductImageResponseDTO.fromVO(
        productImageService.delete(
            UUID.fromString(id)));
  }

  @Transactional
  public AdminProductImageResponseDTO patchMain(
      String productId, String id) throws BindException {
    validateProduct(productId);
    validateProductImage(id);

    return AdminProductImageResponseDTO
        .fromVO(productImageService.updateMain(UUID.fromString(productId), UUID.fromString(id)));
  }

  @Transactional
  public List<AdminProductImageResponseDTO> reorderImages(String productId, List<String> sortedImageIds)
      throws BindException {
    validateProduct(productId);
    return productImageService.updateOrder(UUID.fromString(productId), sortedImageIds.stream()
        .map(UUID::fromString)
        .toList()).stream().map(AdminProductImageResponseDTO::new).toList();
  }

}
