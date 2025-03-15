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
import com.smartstore.api.v1.domain.category.entity.CategoryL2;
import com.smartstore.api.v1.domain.category.repository.CategoryL2Repository;
import com.smartstore.api.v1.domain.category.vo.CategoryL2FilterConditionVO;
import com.smartstore.api.v1.domain.category.vo.CategoryL2VO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CategoryL2Service {

  @PersistenceContext
  private EntityManager entityManager;

  // private final CategoryL1Repository categoryL1Repository;
  private final CategoryL2Repository categoryL2Repository;

  public CategoryL2Service(CategoryL2Repository categoryL2Repository) {
    this.categoryL2Repository = categoryL2Repository;
  }

  private CategoryL2 getExistingCategoryL2ById(UUID id) {
    return categoryL2Repository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 카테고리(중)가 존재하지 않습니다."));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean isExistsCategoryL2ById(UUID id) {
    return categoryL2Repository.existsById(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public CategoryL2VO findCategoryL2ById(UUID id) {
    return CategoryL2VO.fromEntity(getExistingCategoryL2ById(id));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page<CategoryL2VO> findCategoryL2sByCondition(CategoryL2FilterConditionVO condition, Pageable pageable) {
    return CategoryL2VO.fromEntityWithPage(categoryL2Repository.findAll(condition.toSpecification(), pageable));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL2VO createCategoryL2(CategoryL2VO vo) {
    CategoryL2 newCategoryL2 = CategoryL2.builder()
        .name(vo.getName())
        .categoryL1(entityManager.getReference(CategoryL1.class, vo.getCategoryL1Id()))
        .build();

    return CategoryL2VO.fromEntity(categoryL2Repository.save(newCategoryL2));
  }

  private void applyUpdate(CategoryL2 categoryL2, CategoryL2VO vo) {
    categoryL2.setName(vo.getName());
    categoryL2.setCategoryL1(entityManager.getReference(CategoryL1.class, vo.getCategoryL1Id()));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL2VO replaceCategoryL2(UUID id, CategoryL2VO vo) {
    CategoryL2 categoryL2 = getExistingCategoryL2ById(id);
    applyUpdate(categoryL2, vo);
    return CategoryL2VO.fromEntity(categoryL2Repository.save(categoryL2));
  }

  private void applyPartialUpdate(CategoryL2 categoryL2, CategoryL2VO vo) {
    if (!ObjectUtils.isEmpty(vo.getName())) {
      categoryL2.setName(vo.getName());
    }
    if (!ObjectUtils.isEmpty(vo.getCategoryL1Id())) {
      categoryL2.setCategoryL1(entityManager.getReference(CategoryL1.class, vo.getCategoryL1Id()));
    }
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL2VO modifyCategoryL2(UUID id, CategoryL2VO vo) {
    CategoryL2 categoryL2 = getExistingCategoryL2ById(id);
    applyPartialUpdate(categoryL2, vo);
    return CategoryL2VO.fromEntity(categoryL2Repository.save(categoryL2));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL2VO deleteCategoryL2(UUID id) {
    CategoryL2 categoryL2 = getExistingCategoryL2ById(id);
    categoryL2.markDelete();

    return CategoryL2VO.fromEntity(categoryL2Repository.save(categoryL2));
  }
}