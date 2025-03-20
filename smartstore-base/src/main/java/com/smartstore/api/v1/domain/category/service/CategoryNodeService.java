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

@Service
public class CategoryNodeService {

  @PersistenceContext
  private EntityManager entityManager;

  private final CategoryNodeRepository categoryNodeRepository;

  public CategoryNodeService(CategoryNodeRepository categoryNodeRepository) {
    this.categoryNodeRepository = categoryNodeRepository;
  }

  private CategoryNode getExistingCategoryNodeById(UUID id) {
    return categoryNodeRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("해당하는 카테고리(말단)가 존재하지 않습니다."));
  }

  private void applyUpdate(CategoryNode categoryNode, CategoryNodeVO vo) {
    categoryNode.setName(vo.getName());
    categoryNode.setCategoryL2(entityManager.getReference(CategoryL2.class, vo.getCategoryL2Id()));
  }

  private void applyPartialUpdate(CategoryNode categoryNode, CategoryNodeVO vo) {
    if (!ObjectUtils.isEmpty(vo.getName())) {
      categoryNode.setName(vo.getName());
    }
    if (!ObjectUtils.isEmpty(vo.getCategoryL2Id())) {
      categoryNode.setCategoryL2(entityManager.getReference(CategoryL2.class, vo.getCategoryL2Id()));
    }
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean isExistsCategoryNodeById(UUID id) {
    return categoryNodeRepository.existsById(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public CategoryNodeVO findCategoryNodeById(UUID id) {
    return CategoryNodeVO.fromEntity(getExistingCategoryNodeById(id));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page<CategoryNodeVO> findCategoryNodesByCondition(CategoryNodeFilterConditionVO condition, Pageable pageable) {
    return CategoryNodeVO.fromEntityWithPage(categoryNodeRepository.findAll(condition.toSpecification(), pageable));
  }

  private CategoryNode create(CategoryNodeVO vo) {
    return CategoryNode.builder()
        .name(vo.getName())
        .categoryL2(entityManager.getReference(CategoryL2.class, vo.getCategoryL2Id()))
        .build();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryNodeVO createCategoryNode(CategoryNodeVO vo) {
    return CategoryNodeVO.fromEntity(categoryNodeRepository.save(create(vo)));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryNodeVO replaceCategoryNode(UUID id, CategoryNodeVO vo) {
    CategoryNode categoryNode = getExistingCategoryNodeById(id);
    applyUpdate(categoryNode, vo);

    return CategoryNodeVO.fromEntity(categoryNodeRepository.save(categoryNode));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryNodeVO modifyCategoryNode(UUID id, CategoryNodeVO vo) {
    CategoryNode categoryNode = getExistingCategoryNodeById(id);
    applyPartialUpdate(categoryNode, vo);

    return CategoryNodeVO.fromEntity(categoryNodeRepository.save(categoryNode));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryNodeVO deleteCategoryNode(UUID id) {
    CategoryNode categoryNode = getExistingCategoryNodeById(id);
    categoryNode.markDelete();

    return CategoryNodeVO.fromEntity(categoryNodeRepository.save(categoryNode));
  }
}