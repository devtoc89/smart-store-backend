package com.smartstore.api.v1.application.admin.categorynode.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.application.admin.categorynode.dto.AdminCategoryNodeFilterRequestDTO;
import com.smartstore.api.v1.application.admin.categorynode.dto.AdminCategoryNodePatchRequestDTO;
import com.smartstore.api.v1.application.admin.categorynode.dto.AdminCategoryNodePostRequestDTO;
import com.smartstore.api.v1.application.admin.categorynode.dto.AdminCategoryNodePutRequestDTO;
import com.smartstore.api.v1.application.admin.categorynode.dto.AdminCategoryNodeResponseDTO;
import com.smartstore.api.v1.domain.category.service.CategoryNodeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCategoryNodeAppService {

  private final CategoryNodeService categoryNodeService;

  @Transactional(readOnly = true)
  public AdminCategoryNodeResponseDTO get(String id) {
    return AdminCategoryNodeResponseDTO.fromVO(
        categoryNodeService.findById(UUID.fromString(id)));
  }

  @Transactional(readOnly = true)
  public Page<AdminCategoryNodeResponseDTO> getList(
      AdminCategoryNodeFilterRequestDTO searchRequest, Pageable pageable) {
    return AdminCategoryNodeResponseDTO.fromVOWithPage(
        categoryNodeService.findManyByCondition(
            searchRequest.toSearchConditionVO(), pageable));
  }

  @Transactional
  public AdminCategoryNodeResponseDTO post(
      AdminCategoryNodePostRequestDTO postRequestDTO) {
    var id = UUID.randomUUID();
    return AdminCategoryNodeResponseDTO.fromVO(
        categoryNodeService.create(id,
            postRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryNodeResponseDTO put(String id,
      AdminCategoryNodePutRequestDTO putRequestDTO) {
    return AdminCategoryNodeResponseDTO.fromVO(
        categoryNodeService.replace(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryNodeResponseDTO patch(String id,
      AdminCategoryNodePatchRequestDTO putRequestDTO) {
    return AdminCategoryNodeResponseDTO.fromVO(
        categoryNodeService.modify(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryNodeResponseDTO delete(String id) {
    return AdminCategoryNodeResponseDTO.fromVO(
        categoryNodeService.delete(
            UUID.fromString(id)));
  }
}
