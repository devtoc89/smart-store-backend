package com.smartstore.api.v1.domain.category.service;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.smartstore.api.v1.domain.category.entity.CategoryL1;
import com.smartstore.api.v1.domain.category.repository.CategoryL1Repository;
import com.smartstore.api.v1.domain.category.vo.CategoryL1FilterConditionVO;
import com.smartstore.api.v1.domain.category.vo.CategoryL1VO;

@Service
public class CategoryL1Service {

  private final CategoryL1Repository categoryL1Repository;

  public CategoryL1Service(CategoryL1Repository categoryL1Repository) {
    this.categoryL1Repository = categoryL1Repository;
  }

  private CategoryL1 getExistingCategoryL1ById(UUID id) {
    return categoryL1Repository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 카테고리(대)가 존재하지 않습니다."));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean isExistsCategoryL1ById(UUID id) {
    return categoryL1Repository.existsById(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public CategoryL1VO findCategoryL1ById(UUID id) {
    return CategoryL1VO.fromEntity(getExistingCategoryL1ById(id));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page<CategoryL1VO> findCategoryL1sByCondition(CategoryL1FilterConditionVO condition, Pageable pageable) {
    return CategoryL1VO.fromEntityWithPage(categoryL1Repository.findAll(condition.toSpecification(), pageable));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL1VO createCategoryL1(CategoryL1VO categoryL1VO) {
    CategoryL1 newCategoryL1 = CategoryL1.builder()
        .name(categoryL1VO.getName())
        .build();

    return CategoryL1VO.fromEntity(categoryL1Repository.save(newCategoryL1));
  }

  private void applyUpdate(CategoryL1 categoryL1, CategoryL1VO vo) {
    categoryL1.setName(vo.getName());
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL1VO replaceCategoryL1(UUID id, CategoryL1VO categoryL1VO) {
    CategoryL1 categoryL1 = getExistingCategoryL1ById(id);
    applyUpdate(categoryL1, categoryL1VO);

    return CategoryL1VO.fromEntity(categoryL1Repository.save(categoryL1));
  }

  private void applyPartialUpdate(CategoryL1 categoryL1, CategoryL1VO vo) {
    if (!ObjectUtils.isEmpty(vo.getName())) {
      categoryL1.setName(vo.getName());
    }
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL1VO modifyCategoryL1(UUID id, CategoryL1VO categoryL1VO) {
    CategoryL1 categoryL1 = getExistingCategoryL1ById(id);
    applyPartialUpdate(categoryL1, categoryL1VO);

    return CategoryL1VO.fromEntity(categoryL1Repository.save(categoryL1));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL1VO deleteCategoryL1(UUID id) {
    CategoryL1 categoryL1 = getExistingCategoryL1ById(id);
    categoryL1.markDelete();

    return CategoryL1VO.fromEntity(categoryL1Repository.save(categoryL1));
  }
}