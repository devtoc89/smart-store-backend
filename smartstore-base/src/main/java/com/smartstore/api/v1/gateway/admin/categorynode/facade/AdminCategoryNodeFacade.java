package com.smartstore.api.v1.gateway.admin.categorynode.facade;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.domain.category.service.CategoryNodeService;
import com.smartstore.api.v1.gateway.admin.categorynode.dto.AdminCategoryNodeFilterRequestDTO;
import com.smartstore.api.v1.gateway.admin.categorynode.dto.AdminCategoryNodePatchRequestDTO;
import com.smartstore.api.v1.gateway.admin.categorynode.dto.AdminCategoryNodePostRequestDTO;
import com.smartstore.api.v1.gateway.admin.categorynode.dto.AdminCategoryNodePutRequestDTO;
import com.smartstore.api.v1.gateway.admin.categorynode.dto.AdminCategoryNodeResponseDTO;

@Service
public class AdminCategoryNodeFacade {

  private final CategoryNodeService categoryNodeService;

  public AdminCategoryNodeFacade(CategoryNodeService categoryNodeService) {
    this.categoryNodeService = categoryNodeService;
  }

  @Transactional(readOnly = true)
  public AdminCategoryNodeResponseDTO getCategoryNodeById(String id) {
    return AdminCategoryNodeResponseDTO.fromVO(
        categoryNodeService.findCategoryNodeById(UUID.fromString(id)));
  }

  @Transactional(readOnly = true)
  public Page<AdminCategoryNodeResponseDTO> getCategoryNodesByFilterWithPaging(
      AdminCategoryNodeFilterRequestDTO searchRequest, Pageable pageable) {
    return AdminCategoryNodeResponseDTO.fromVOWithPage(
        categoryNodeService.findCategoryNodesByCondition(
            searchRequest.toSearchConditionVO(), pageable));
  }

  @Transactional
  public AdminCategoryNodeResponseDTO postCategoryNode(
      AdminCategoryNodePostRequestDTO postRequestDTO) {
    return AdminCategoryNodeResponseDTO.fromVO(
        categoryNodeService.createCategoryNode(
            postRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryNodeResponseDTO putCategoryNode(String id,
      AdminCategoryNodePutRequestDTO putRequestDTO) {
    return AdminCategoryNodeResponseDTO.fromVO(
        categoryNodeService.replaceCategoryNode(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryNodeResponseDTO patchCategoryNode(String id,
      AdminCategoryNodePatchRequestDTO putRequestDTO) {
    return AdminCategoryNodeResponseDTO.fromVO(
        categoryNodeService.modifyCategoryNode(
            UUID.fromString(id), putRequestDTO.toVO()));
  }

  @Transactional
  public AdminCategoryNodeResponseDTO deleteCategoryNode(String id) {
    return AdminCategoryNodeResponseDTO.fromVO(
        categoryNodeService.deleteCategoryNode(
            UUID.fromString(id)));
  }
}
