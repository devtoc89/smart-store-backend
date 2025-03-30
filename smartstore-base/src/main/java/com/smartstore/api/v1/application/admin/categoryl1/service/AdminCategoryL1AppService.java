package com.smartstore.api.v1.application.admin.categoryl1.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1FilterRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1PatchRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1PostRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1PutRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1ResponseDTO;
import com.smartstore.api.v1.domain.category.service.CategoryL1Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCategoryL1AppService {

  private final CategoryL1Service categoryL1Service;

  @Transactional(readOnly = true)
  public AdminCategoryL1ResponseDTO getCategoryL1ById(String id) {
    return AdminCategoryL1ResponseDTO.fromVO(
        categoryL1Service.findById(UUID.fromString(id)));
  }

  @Transactional(readOnly = true)
  public Page<AdminCategoryL1ResponseDTO> getList(
      AdminCategoryL1FilterRequestDTO searchRequest, Pageable pageable) {
    return AdminCategoryL1ResponseDTO.fromVOWithPage(
        categoryL1Service.findManyByCondition(
            searchRequest.toSearchConditionVO(), pageable));
  }

  @Transactional
  public AdminCategoryL1ResponseDTO post(
      AdminCategoryL1PostRequestDTO postRequestDTO) {
    var id = UUID.randomUUID();
    return AdminCategoryL1ResponseDTO.fromVO(categoryL1Service.create(id, postRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL1ResponseDTO put(String id,
      AdminCategoryL1PutRequestDTO putRequestDTO) {
    return AdminCategoryL1ResponseDTO.fromVO(
        categoryL1Service.replace(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL1ResponseDTO patch(String id,
      AdminCategoryL1PatchRequestDTO putRequestDTO) {
    return AdminCategoryL1ResponseDTO.fromVO(
        categoryL1Service.modify(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryL1ResponseDTO delete(String id) {
    return AdminCategoryL1ResponseDTO.fromVO(
        categoryL1Service.delete(
            UUID.fromString(id)));
  }
}
