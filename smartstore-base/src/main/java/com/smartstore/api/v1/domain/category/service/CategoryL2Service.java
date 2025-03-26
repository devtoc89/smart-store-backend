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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryL2Service {

  @PersistenceContext
  private EntityManager entityManager;

  private final CategoryL2Repository categoryL2Repository;

  private CategoryL2 applyUpdate(CategoryL2 categoryL2, CategoryL2VO vo) {
    categoryL2.setName(vo.getName());
    categoryL2.setCategoryL1(entityManager.getReference(CategoryL1.class, vo.getCategoryL1Id()));
    return categoryL2;
  }

  private CategoryL2 applyPartialUpdate(CategoryL2 categoryL2, CategoryL2VO vo) {
    if (!ObjectUtils.isEmpty(vo.getName())) {
      categoryL2.setName(vo.getName());
    }
    if (!ObjectUtils.isEmpty(vo.getCategoryL1Id())) {
      categoryL2.setCategoryL1(entityManager.getReference(CategoryL1.class, vo.getCategoryL1Id()));
    }
    return categoryL2;
  }

  private CategoryL2 findByIdOrExcept(UUID id) {
    return categoryL2Repository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 카테고리(중)가 존재하지 않습니다."));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean isExist(UUID id) {
    return categoryL2Repository.existsById(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public CategoryL2VO findById(UUID id) {
    return CategoryL2VO.fromEntity(findByIdOrExcept(id));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page<CategoryL2VO> findManyByCondition(CategoryL2FilterConditionVO condition, Pageable pageable) {
    return CategoryL2VO.fromEntityWithPage(categoryL2Repository.findAll(condition.toSpecification(), pageable));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL2VO create(UUID id, CategoryL2VO vo) {
    var entity = CategoryL2.builder()
        .id(id)
        .name(vo.getName())
        .categoryL1(entityManager.getReference(CategoryL1.class, vo.getCategoryL1Id()))
        .build();

    return CategoryL2VO.fromEntity(categoryL2Repository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL2VO replace(UUID id, CategoryL2VO vo) {
    var entity = applyUpdate(findByIdOrExcept(id), vo);
    return CategoryL2VO.fromEntity(categoryL2Repository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL2VO modify(UUID id, CategoryL2VO vo) {
    var entity = applyPartialUpdate(findByIdOrExcept(id), vo);
    return CategoryL2VO.fromEntity(categoryL2Repository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL2VO delete(UUID id) {
    CategoryL2 categoryL2 = findByIdOrExcept(id);
    categoryL2.markDelete();

    return CategoryL2VO.fromEntity(categoryL2Repository.save(categoryL2));
  }
}