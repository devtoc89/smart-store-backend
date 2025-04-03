package com.smartstore.api.v1.application.admin.category.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import com.smartstore.api.v1.application.admin.category.dto.AdminCategoryFilterRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.AdminCategoryPatchRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.AdminCategoryPostRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.AdminCategoryPutRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.AdminCategoryResponseDTO;
import com.smartstore.api.v1.application.admin.category.dto.tree.AdminCategoryL1TreePutRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.tree.AdminCategoryL1TreeResponseDTO;
import com.smartstore.api.v1.application.admin.category.mapper.AdminCategoryMapper;
import com.smartstore.api.v1.common.utils.validation.ValidationUtil;
import com.smartstore.api.v1.domain.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCategoryAppService {

  private final CategoryService categoryService;

  private void validateLevelAndParentId(Integer level, UUID parentId) throws BindException {
    // TODO: validation 쪼개고 상세 체크!
    if (level == null && parentId == null) {
      return;
    }
    if (Integer.valueOf(1).equals(level) && parentId == null) {
      return;
    }

    // if (level < 0 || level > 3) {
    // throw ValidationUtil.createBindException(this, "level", "레벨은 1~3 사이의 숫자여야
    // 합니다.");
    // }

    // if (level > 1 && ObjectUtils.isEmpty(parentId)) {
    // throw ValidationUtil.createBindException(this, "parentId", "2차 카테고리 이상은 부모
    // 카테고리 ID가 필수입니다.");
    // }

    if (!categoryService.isExists(parentId)) {
      throw ValidationUtil.createBindException(this, "parentId", "대상 부모 카테고리가 존재하지 않습니다.");
    }
  }

  @Transactional(readOnly = true)
  public List<AdminCategoryL1TreeResponseDTO> getTreeList() {
    return AdminCategoryL1TreeResponseDTO.fromVO(
        categoryService.findAll());
  }

  @Transactional(readOnly = true)
  public Page<AdminCategoryResponseDTO> getList(
      AdminCategoryFilterRequestDTO searchRequest, Pageable pageable, Integer level) {
    return AdminCategoryResponseDTO.fromVOWithPage(
        categoryService.findManyByCondition(
            searchRequest.toSearchConditionVO(level), pageable));
  }

  @Transactional(readOnly = true)
  public AdminCategoryResponseDTO get(String id) {
    return AdminCategoryResponseDTO.fromVO(
        categoryService.findById(UUID.fromString(id)));
  }

  @Transactional
  public void putAll(List<AdminCategoryL1TreePutRequestDTO> requestDTO) {
    categoryService.putAll(AdminCategoryMapper.fromPutDTO(requestDTO));
  }

  @Transactional
  public AdminCategoryResponseDTO post(
      AdminCategoryPostRequestDTO dto) throws BindException {
    validateLevelAndParentId(dto.getLevel(), Optional.ofNullable(dto.getParentId()).map(UUID::fromString).orElse(null));
    var id = UUID.randomUUID();
    return AdminCategoryResponseDTO.fromVO(categoryService.create(id, dto.toVO()));
  }

  @Transactional
  public AdminCategoryResponseDTO put(String id,
      AdminCategoryPutRequestDTO dto) throws BindException {
    validateLevelAndParentId(null, Optional.ofNullable(dto.getParentId()).map(UUID::fromString).orElse(null));
    return AdminCategoryResponseDTO.fromVO(
        categoryService.replace(
            UUID.fromString(id), dto.toVO()));
  }

  @Transactional
  public AdminCategoryResponseDTO patch(String id,
      AdminCategoryPatchRequestDTO dto) throws BindException {

    validateLevelAndParentId(null, Optional.ofNullable(dto.getParentId()).map(UUID::fromString).orElse(null));
    return AdminCategoryResponseDTO.fromVO(
        categoryService.modify(
            UUID.fromString(id), dto.toVO()));
  }

  @Transactional
  public AdminCategoryResponseDTO delete(String id) {
    return AdminCategoryResponseDTO.fromVO(
        categoryService.delete(
            UUID.fromString(id)));
  }

}
