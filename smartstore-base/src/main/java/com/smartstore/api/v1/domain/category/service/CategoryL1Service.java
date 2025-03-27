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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryL1Service {

  private final CategoryL1Repository categoryL1Repository;

  private CategoryL1 applyUpdate(CategoryL1 entity, CategoryL1VO vo) {
    entity.setName(vo.getName());
    entity.setOrderBy(vo.getOrderBy());
    return entity;
  }

  private CategoryL1 applyPartialUpdate(CategoryL1 entity, CategoryL1VO vo) {
    if (!ObjectUtils.isEmpty(vo.getName())) {
      entity.setName(vo.getName());
    }
    if (!ObjectUtils.isEmpty(vo.getOrderBy())) {
      entity.setOrderBy(vo.getOrderBy());
    }
    return entity;
  }

  private CategoryL1 findByIdOrExcept(UUID id) {
    return categoryL1Repository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 카테고리(대)가 존재하지 않습니다."));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean isExists(UUID id) {
    return categoryL1Repository.existsById(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public CategoryL1VO findById(UUID id) {
    return CategoryL1VO.fromEntity(findByIdOrExcept(id));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page<CategoryL1VO> findManyByCondition(CategoryL1FilterConditionVO condition, Pageable pageable) {
    return CategoryL1VO.fromEntityWithPage(categoryL1Repository.findAll(condition.toSpecification(), pageable));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL1VO create(UUID id, CategoryL1VO vo) {
    var entity = CategoryL1.builder()
        .id(id)
        .name(vo.getName())
        .build();

    return CategoryL1VO.fromEntity(categoryL1Repository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL1VO replace(UUID id, CategoryL1VO vo) {
    var entity = applyUpdate(findByIdOrExcept(id), vo);
    return CategoryL1VO.fromEntity(
        categoryL1Repository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL1VO modify(UUID id, CategoryL1VO vo) {
    var entity = applyPartialUpdate(findByIdOrExcept(id), vo);
    return CategoryL1VO.fromEntity(categoryL1Repository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryL1VO delete(UUID id) {
    CategoryL1 entity = findByIdOrExcept(id);
    entity.markDelete();

    return CategoryL1VO.fromEntity(categoryL1Repository.save(entity));
  }
}