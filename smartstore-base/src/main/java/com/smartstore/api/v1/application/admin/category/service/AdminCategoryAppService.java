package com.smartstore.api.v1.application.admin.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.application.admin.category.mapper.AdminCategoryListResponseAssembler;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1TreeResponseDTO;
import com.smartstore.api.v1.domain.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCategoryAppService {

  private final CategoryService categoryService;

  @Transactional(readOnly = true)
  public List<AdminCategoryL1TreeResponseDTO> getList() {
    return AdminCategoryListResponseAssembler.fromVO(
        categoryService.findAll());
  }

}
