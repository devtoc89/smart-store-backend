package com.smartstore.api.v1.domain.category.service;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.smartstore.api.v1.domain.category.entity.CategoryL2;
import com.smartstore.api.v1.domain.category.entity.CategoryNode;
import com.smartstore.api.v1.domain.category.repository.CategoryNodeRepository;
import com.smartstore.api.v1.domain.category.vo.CategoryNodeFilterConditionVO;
import com.smartstore.api.v1.domain.category.vo.CategoryNodeVO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryNodeService {

  @PersistenceContext
  private EntityManager entityManager;

  private final CategoryNodeRepository categoryNodeRepository;

  private CategoryNode applyUpdate(CategoryNode categoryNode, CategoryNodeVO vo) {
    categoryNode.setName(vo.getName());
    categoryNode.setCategoryL2(entityManager.getReference(CategoryL2.class, vo.getCategoryL2Id()));
    return categoryNode;
  }

  private CategoryNode applyPartialUpdate(CategoryNode categoryNode, CategoryNodeVO vo) {
    if (!ObjectUtils.isEmpty(vo.getName())) {
      categoryNode.setName(vo.getName());
    }
    if (!ObjectUtils.isEmpty(vo.getCategoryL2Id())) {
      categoryNode.setCategoryL2(entityManager.getReference(CategoryL2.class, vo.getCategoryL2Id()));
    }
    return categoryNode;
  }

  private CategoryNode findByIdOrExcept(UUID id) {
    return categoryNodeRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("해당하는 카테고리(말단)가 존재하지 않습니다."));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean isExist(UUID id) {
    return categoryNodeRepository.existsById(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public CategoryNodeVO findById(UUID id) {
    return CategoryNodeVO.fromEntity(findByIdOrExcept(id));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page<CategoryNodeVO> findManyByCondition(CategoryNodeFilterConditionVO condition, Pageable pageable) {
    return CategoryNodeVO.fromEntityWithPage(categoryNodeRepository.findAll(condition.toSpecification(), pageable));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryNodeVO create(UUID id, CategoryNodeVO vo) {
    var entity = CategoryNode.builder()
        .id(id)
        .name(vo.getName())
        .categoryL2(entityManager.getReference(CategoryL2.class, vo.getCategoryL2Id()))
        .build();

    return CategoryNodeVO.fromEntity(categoryNodeRepository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryNodeVO replace(UUID id, CategoryNodeVO vo) {
    var entity = applyUpdate(findByIdOrExcept(id), vo);

    return CategoryNodeVO.fromEntity(categoryNodeRepository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryNodeVO modify(UUID id, CategoryNodeVO vo) {
    var entity = applyPartialUpdate(findByIdOrExcept(id), vo);

    return CategoryNodeVO.fromEntity(categoryNodeRepository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryNodeVO delete(UUID id) {
    var entity = findByIdOrExcept(id);
    entity.markDelete();

    return CategoryNodeVO.fromEntity(categoryNodeRepository.save(entity));
  }
}