package com.smartstore.api.v1.gateway.admin.categoryl2.facade;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.domain.category.service.CategoryL2Service;
import com.smartstore.api.v1.gateway.admin.categoryl2.dto.AdminCategoryL2FilterRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl2.dto.AdminCategoryL2PatchRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl2.dto.AdminCategoryL2PostRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl2.dto.AdminCategoryL2PutRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl2.dto.AdminCategoryL2ResponseDTO;

@Service
public class AdminCategoryL2Facade {

  private final CategoryL2Service categoryL2Service;

  public AdminCategoryL2Facade(CategoryL2Service categoryL2Service) {
    this.categoryL2Service = categoryL2Service;
  }

  @Transactional(readOnly = true)
  public AdminCategoryL2ResponseDTO getCategoryL2ById(String id) {
    return AdminCategoryL2ResponseDTO.fromVO(
        categoryL2Service.findCategoryL2ById(UUID.fromString(id)));
  }

  @Transactional(readOnly = true)
  public Page<AdminCategoryL2ResponseDTO> getCategoryL2sByFilterWithPaging(
      AdminCategoryL2FilterRequestDTO searchRequest, Pageable pageable) {
    return AdminCategoryL2ResponseDTO.fromVOWithPage(
        categoryL2Service.findCategoryL2sByCondition(
            searchRequest.toSearchConditionVO(), pageable));
  }

  @Transactional
  public AdminCategoryL2ResponseDTO postCategoryL2(
      AdminCategoryL2PostRequestDTO postRequestDTO) {
    return AdminCategoryL2ResponseDTO.fromVO(
        categoryL2Service.createCategoryL2(
            postRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL2ResponseDTO putCategoryL2(String id,
      AdminCategoryL2PutRequestDTO putRequestDTO) {
    return AdminCategoryL2ResponseDTO.fromVO(
        categoryL2Service.replaceCategoryL2(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL2ResponseDTO patchCategoryL2(String id,
      AdminCategoryL2PatchRequestDTO putRequestDTO) {
    return AdminCategoryL2ResponseDTO.fromVO(
        categoryL2Service.modifyCategoryL2(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL2ResponseDTO deleteCategoryL2(String id) {
    return AdminCategoryL2ResponseDTO.fromVO(
        categoryL2Service.deleteCategoryL2(
            UUID.fromString(id)));
  }
}
