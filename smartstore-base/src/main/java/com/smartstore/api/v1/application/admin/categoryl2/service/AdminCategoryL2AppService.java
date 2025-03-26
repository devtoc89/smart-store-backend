package com.smartstore.api.v1.application.admin.categoryl2.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.application.admin.categoryl2.dto.AdminCategoryL2FilterRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl2.dto.AdminCategoryL2PatchRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl2.dto.AdminCategoryL2PostRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl2.dto.AdminCategoryL2PutRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl2.dto.AdminCategoryL2ResponseDTO;
import com.smartstore.api.v1.domain.category.service.CategoryL2Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCategoryL2AppService {

  private final CategoryL2Service categoryL2Service;

  @Transactional(readOnly = true)
  public AdminCategoryL2ResponseDTO getCategoryL2ById(String id) {
    return AdminCategoryL2ResponseDTO.fromVO(
        categoryL2Service.findById(UUID.fromString(id)));
  }

  @Transactional(readOnly = true)
  public Page<AdminCategoryL2ResponseDTO> getCategoryL2sByFilterWithPaging(
      AdminCategoryL2FilterRequestDTO searchRequest, Pageable pageable) {
    return AdminCategoryL2ResponseDTO.fromVOWithPage(
        categoryL2Service.findManyByCondition(
            searchRequest.toSearchConditionVO(), pageable));
  }

  @Transactional
  public AdminCategoryL2ResponseDTO postCategoryL2(
      AdminCategoryL2PostRequestDTO postRequestDTO) {
    var id = UUID.randomUUID();
    return AdminCategoryL2ResponseDTO.fromVO(categoryL2Service.create(id, postRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL2ResponseDTO putCategoryL2(String id,
      AdminCategoryL2PutRequestDTO putRequestDTO) {
    return AdminCategoryL2ResponseDTO.fromVO(categoryL2Service.replace(UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL2ResponseDTO patchCategoryL2(String id,
      AdminCategoryL2PatchRequestDTO putRequestDTO) {
    return AdminCategoryL2ResponseDTO.fromVO(
        categoryL2Service.modify(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL2ResponseDTO deleteCategoryL2(String id) {
    return AdminCategoryL2ResponseDTO.fromVO(
        categoryL2Service.delete(
            UUID.fromString(id)));
  }
}
