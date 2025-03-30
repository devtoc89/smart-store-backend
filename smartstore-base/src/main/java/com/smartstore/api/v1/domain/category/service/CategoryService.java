package com.smartstore.api.v1.domain.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.domain.category.repository.CategoryRepository;
import com.smartstore.api.v1.domain.category.vo.CategoryVO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

  @PersistenceContext
  private EntityManager entityManager;

  private final CategoryRepository categoryRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public CategoryVO findAll() {
    return CategoryVO.fromEntityWithList(categoryRepository.fetchCategoryTree());
  }

}