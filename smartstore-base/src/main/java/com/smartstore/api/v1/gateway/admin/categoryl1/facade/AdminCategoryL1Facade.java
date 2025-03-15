package com.smartstore.api.v1.gateway.admin.categoryl1.facade;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.domain.category.service.CategoryL1Service;
import com.smartstore.api.v1.gateway.admin.categoryl1.dto.AdminCategoryL1FilterRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl1.dto.AdminCategoryL1PatchRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl1.dto.AdminCategoryL1PostRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl1.dto.AdminCategoryL1PutRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl1.dto.AdminCategoryL1ResponseDTO;

@Service
public class AdminCategoryL1Facade {

  private final CategoryL1Service categoryL1Service;

  public AdminCategoryL1Facade(CategoryL1Service categoryL1Service) {
    this.categoryL1Service = categoryL1Service;
  }

  @Transactional(readOnly = true)
  public AdminCategoryL1ResponseDTO getCategoryL1ById(String id) {
    return AdminCategoryL1ResponseDTO.fromVO(
        categoryL1Service.findCategoryL1ById(UUID.fromString(id)));
  }

  @Transactional(readOnly = true)
  public Page<AdminCategoryL1ResponseDTO> getCategoryL1sByFilterWithPaging(
      AdminCategoryL1FilterRequestDTO searchRequest, Pageable pageable) {
    return AdminCategoryL1ResponseDTO.fromVOWithPage(
        categoryL1Service.findCategoryL1sByCondition(
            searchRequest.toSearchConditionVO(), pageable));
  }

  @Transactional
  public AdminCategoryL1ResponseDTO postCategoryL1(
      AdminCategoryL1PostRequestDTO postRequestDTO) {
    return AdminCategoryL1ResponseDTO.fromVO(
        categoryL1Service.createCategoryL1(
            postRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL1ResponseDTO putCategoryL1(String id,
      AdminCategoryL1PutRequestDTO putRequestDTO) {
    return AdminCategoryL1ResponseDTO.fromVO(
        categoryL1Service.replaceCategoryL1(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL1ResponseDTO patchCategoryL1(String id,
      AdminCategoryL1PatchRequestDTO putRequestDTO) {
    return AdminCategoryL1ResponseDTO.fromVO(
        categoryL1Service.modifyCategoryL1(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL1ResponseDTO deleteCategoryL1(String id) {
    return AdminCategoryL1ResponseDTO.fromVO(
        categoryL1Service.deleteCategoryL1(
            UUID.fromString(id)));
  }
}
